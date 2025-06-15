import { environment } from "@/utils/environment";
import { makeAutoObservable, action, runInAction } from "mobx";
import { UserStore } from "@/stores/user/userStore";
import { WSDiagramMessageContent } from "@/types/ws/messageTypes";
import { Edge, Node } from "@xyflow/react";
import { v4 as uuidv4 } from "uuid";
import { PLCDataType, SensorsDataType } from "@/types/api/responseTypes";
import { alertManager } from "@/utils/alert/AlertManager";
import { DiagramWebSocket } from "./DiagramWebSocket";
import { DiagramSensorNodeData } from "../components/nodes/DiagramSensorNode";
import { DiagramPLCNodeData } from "../components/nodes/DiagramPLCNode";
import { DiagramSourceDataProvider } from "./data/DiagramSourceDataProvider";
import { DiagramDataProvider } from "./data/DiagramDataProvider";

/**
 * Type definition for node data in the diagram
 */
export type NodeData = DiagramSensorNodeData | DiagramPLCNodeData;

/**
 * DiagramStore class
 *
 * This class manages the state and operations for the diagram.
 * It handles nodes, edges, edit mode, and communication with the server.
 */
export default class DiagramStore {
  private userStore: UserStore;
  private wsDiagram: DiagramWebSocket;
  private readonly sourceDataProvider: DiagramSourceDataProvider;
  private readonly dataProvider: DiagramDataProvider;

  /** Flag to indicate if the diagram is in edit mode */
  editMode: boolean = false;
  /** Array of nodes in the diagram */
  nodes: Node<NodeData>[] = [];
  /** Array of edges in the diagram */
  edges: Edge[] = [];

  /**
   * Constructor for ConnectionStore
   * Initializes the store and sets up necessary connections and providers
   */
  constructor() {
    makeAutoObservable(this);
    this.userStore = new UserStore();
    this.sourceDataProvider = new DiagramSourceDataProvider();
    this.dataProvider = new DiagramDataProvider();
    this.wsDiagram = new DiagramWebSocket(
      {
        url: environment.wsUrl,
      },
      this.userStore.userEmail,
      this.handleDiagramData
    );
  }

  /**
   * Toggles the edit mode of the diagram
   * Updates nodes and edges accordingly, and saves data when exiting edit mode
   */
  toggleEditMode = () => {
    this.editMode = !this.editMode;
    this.updateNodesEditMode();
    this.updateEdgesAnimation();
    if (!this.editMode) {
      this.saveDiagramData();
    }
  };

  /**
   * Updates the edit mode status for all nodes
   */
  updateNodesEditMode = () => {
    this.nodes = this.nodes.map((node) => ({
      ...node,
      data: {
        ...node.data,
        editMode: this.editMode,
      },
    }));
  };

  /**
   * Adds a new node to the diagram
   * @param node - Partial node object to be added
   */
  addNode = (node: Partial<Node<NodeData>>) => {
    const newNode: Node<NodeData> = {
      id: uuidv4(),
      type: node.type || "default",
      position: node.position || { x: 150, y: 150 },
      data: node.data || {
        name: "",
        editMode: this.editMode,
        ...(node.type === "sensorNode"
          ? {
              city: {
                id: "",
                city: "",
              },
              temperature: 0,
              averageTemperature: 0,
              condition: "",
            }
          : {
              numberOfSensors: "",
            }),
      },
    };
    this.nodes = [...this.nodes, newNode];
  };

  /**
   * Adds a new edge to the diagram
   * @param edge - Partial edge object to be added
   */
  addEdge = (edge: Partial<Edge>) => {
    const newEdge: Edge = {
      id: uuidv4(),
      source: edge.source!,
      target: edge.target!,
      type: edge.type || "default",
      animated: !this.editMode,
      ...edge,
    };
    this.edges = [...this.edges, newEdge];
  };

  /**
   * Updates the nodes in the diagram
   * @param newNodes - New array of nodes to replace the current ones
   */
  updateNodes = action("updateNodes", (newNodes: Node<NodeData>[]) => {
    this.nodes = newNodes;
  });

  /**
   * Updates the edges in the diagram
   * @param newEdges - New array of edges to replace the current ones
   */
  updateEdges = action("updateEdges", (newEdges: Edge[]) => {
    this.edges = newEdges;
  });

  /**
   * Updates the animation status of all edges based on edit mode
   */
  updateEdgesAnimation = () => {
    this.edges = this.edges.map((edge) => ({
      ...edge,
      animated: !this.editMode,
    }));
  };

  /**
   * Checks if a node is connected to any edge
   * @param nodeId - ID of the node to check
   * @returns boolean indicating if the node is connected
   */
  isNodeConnected = (nodeId: string): boolean => {
    return this.edges.some(
      (edge) => edge.source === nodeId || edge.target === nodeId
    );
  };

  /**
   * Saves the current diagram data to the server
   */
  saveDiagramData = async () => {
    try {
      await this.sourceDataProvider.postData({
        diagram: JSON.stringify({
          nodes: this.nodes,
          edges: this.edges,
        }),
      });

      const data = this.createNodesObject();

      await this.dataProvider.postData({
        sensors: data.sensors,
        PLCs: data.PLCs,
      });
      alertManager.success({
        intld: "diagram.diagramSuccessSavedMessage",
      });
    } catch (error) {
      alertManager.error({
        intld: "error.diagramErrorSavedMessage",
      });
      throw error;
    }
  };

  /**
   * Retrieves the source diagram data from the server and updates the local state
   */
  getSourceDiagramData = async () => {
    await this.sourceDataProvider.getData();
    const data = this.sourceDataProvider.data;

    runInAction(() => {
      this.nodes = data.diagram ? JSON.parse(data.diagram).nodes : [];
      this.edges = data.diagram ? JSON.parse(data.diagram).edges : [];
    });
  };

  /**
   * Creates an object representation of the current nodes and their connections
   * @returns Object containing sensors and PLCs data
   */
  private createNodesObject() {
    const result: {
      sensors: SensorsDataType[];
      PLCs: PLCDataType[];
    } = {
      sensors: [],
      PLCs: [],
    };

    const sensorNodes = this.nodes.filter(
      (node): node is Node<DiagramSensorNodeData> => node.type === "sensorNode"
    );
    result.sensors = sensorNodes.map((node) => ({
      id: node.id,
      place: node.data.city.city,
      connections: this.edges
        .filter((edge) => edge.source === node.id)
        .map((edge) => ({
          PLCId: edge.target,
          port: parseInt(edge.targetHandle?.replace("port-", "") || "0", 10),
        })),
    }));

    const plcNodes = this.nodes.filter(
      (node): node is Node<DiagramPLCNodeData> => node.type === "plcNode"
    );
    result.PLCs = plcNodes.map((node) => ({
      id: node.id,
      name: node.data.name,
    }));

    return result;
  }

  /**
   * Removes a node and its connected edges from the diagram
   * @param nodeId - ID of the node to be removed
   */
  removeNode = (nodeId: string) => {
    // Remove all edges connected to this node
    this.edges = this.edges.filter(
      (edge) => edge.source !== nodeId && edge.target !== nodeId
    );

    // Remove the node
    this.nodes = this.nodes.filter((node) => node.id !== nodeId);
  };

  // --------- WebSocket Methods --------- //

  /**
   * Handles incoming diagram data from WebSocket
   * @param data - Diagram message content received from WebSocket
   */
  private handleDiagramData = (data: WSDiagramMessageContent) => {
    data.content.map((sensor) => {
      this.updateNodes(
        this.nodes.map((node) => {
          if (node.id === sensor.id) {
            return {
              ...node,
              data: {
                ...node.data,
                temperature: sensor.temperature,
                averageTemperature: sensor.averageTemperature,
                condition: sensor.condition,
              },
            };
          }
          return node;
        })
      );
    });
  };

  /**
   * Getter for WebSocket connection status
   */
  get isConnected(): boolean {
    return this.wsDiagram.connectionStatus;
  }

  /**
   * Initiates WebSocket connection
   */
  onConnect = () => {
    this.wsDiagram.onConnect();
  };

  /**
   * Disconnects WebSocket
   */
  onDisconnect = () => {
    this.wsDiagram.onDisconnect();
  };

  /**
   * Sends a message through WebSocket
   * @param message - Message to be sent
   */
  sendMessage = <T>(message: T): void => {
    this.wsDiagram.sendMessage<T>(message);
  };
}
