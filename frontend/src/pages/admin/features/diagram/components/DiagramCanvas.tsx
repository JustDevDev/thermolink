import { useRef, useState, useEffect } from "react";
import {
  ReactFlow,
  Controls,
  Background,
  applyNodeChanges,
  applyEdgeChanges,
  ReactFlowInstance,
  Node,
  NodeChange,
  Connection,
  Edge,
} from "@xyflow/react";
import { observer } from "mobx-react-lite";
import { Box } from "@mui/material";
import { gray } from "@/utils/theme/themePrimitives";
import { useDiagramStore } from "../hooks/useDiagramStore";
import { useUserStore } from "@/hooks/stores/useUserStore";
import DiagramMenu from "./DiagramMenu";
import DiagramSensorNode from "./nodes/DiagramSensorNode";
import DiagramPLCNode from "./nodes/DiagramPLCNode";
import "@xyflow/react/dist/style.css";
import "./DiagramCanvas.css";
import { NodeData } from "../stores/DiagramStore";

const nodeTypes = {
  sensorNode: DiagramSensorNode,
  plcNode: DiagramPLCNode,
};

const DiagramCanvas = observer(() => {
  const store = useDiagramStore();
  const userStore = useUserStore();
  const isLight = userStore.getTheme === "light";

  const reactFlowInstanceRef = useRef<ReactFlowInstance<
    Node<NodeData>,
    Edge
  > | null>(null);
  const [viewport, setViewport] = useState({ x: 0, y: 0, zoom: 1 });
  const canvasRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!store.editMode) {
      reactFlowInstanceRef.current?.setNodes((nodes) =>
        nodes.map((node) => ({ ...node, selected: false }))
      );
    }
  }, [store.editMode]);

  const project = (point: { x: number; y: number }) => {
    return {
      x: (point.x - viewport.x) / viewport.zoom,
      y: (point.y - viewport.y) / viewport.zoom,
    };
  };

  const getCanvasCenterPosition = () => {
    if (canvasRef.current) {
      const bounds = canvasRef.current.getBoundingClientRect();
      const center = { x: bounds.width / 2, y: bounds.height / 2 };
      return project(center);
    }
    return { x: 150, y: 150 };
  };

  const onDrop = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    const nodeType = event.dataTransfer.getData("application/reactflow");
    if (!nodeType || !canvasRef.current) return;

    const bounds = canvasRef.current.getBoundingClientRect();
    const dropPosition = {
      x: event.clientX - bounds.left,
      y: event.clientY - bounds.top,
    };
    const position = project(dropPosition);

    store.addNode({
      type: nodeType,
      position,
      data:
        nodeType === "sensorNode"
          ? {
              name: "diagram.temperatureSensor",
              city: {
                id: "",
                city: "",
              },
              temperature: 0,
              averageTemperature: 0,
              condition: "",
              editMode: store.editMode,
            }
          : {
              name: "PLC",
              editMode: store.editMode,
            },
    });
  };

  const onDragOver = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  };

  const onNodesChange = (changes: NodeChange[]) => {
    if (!store.editMode) {
      changes = changes.filter((change) => change.type !== "remove");
    }

    if (changes.length > 0) {
      const updatedNodes = applyNodeChanges<Node<NodeData>>(
        changes as NodeChange<Node<NodeData>>[],
        store.nodes
      );

      if (JSON.stringify(updatedNodes) !== JSON.stringify(store.nodes)) {
        store.updateNodes(updatedNodes);
      }
    }
  };

  const onConnect = (params: Connection) => {
    if (store.editMode) {
      store.addEdge(params);
    }
  };

  return (
    <Box
      ref={canvasRef}
      onDrop={onDrop}
      onDragOver={onDragOver}
      sx={{ width: "100%", position: "relative", flex: 1 }}
    >
      <DiagramMenu getCanvasCenterPosition={getCanvasCenterPosition} />
      <ReactFlow
        onInit={(instance) => {
          reactFlowInstanceRef.current = instance;
        }}
        onMoveEnd={(_, newViewport) => setViewport(newViewport)}
        colorMode={isLight ? "light" : "dark"}
        nodes={store.nodes}
        edges={store.edges}
        onConnect={onConnect}
        onNodesChange={onNodesChange}
        onEdgesChange={(changes) => {
          if (!store.editMode) {
            return;
          }
          const updatedEdges = applyEdgeChanges(changes, store.edges);
          store.updateEdges(updatedEdges);
        }}
        proOptions={{ hideAttribution: true }}
        fitView
        nodeTypes={nodeTypes}
        nodesDraggable={store.editMode}
        nodesConnectable={store.editMode}
        nodesFocusable={store.editMode}
        edgesFocusable={store.editMode}
        elementsSelectable={store.editMode}
        deleteKeyCode={["Delete", "Backspace"]}
      >
        <Controls showInteractive={false} />
        <Background
          gap={12}
          size={2}
          color={isLight ? gray[200] : gray[700]}
          bgColor="transparent"
        />
      </ReactFlow>
    </Box>
  );
});

export default DiagramCanvas;
