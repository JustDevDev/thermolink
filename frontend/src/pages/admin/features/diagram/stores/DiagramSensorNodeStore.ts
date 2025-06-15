import { makeAutoObservable } from "mobx";
import { MappedGooglePlace } from "@/types/mapperTypes";
import { Node } from "@xyflow/react";
import { NodeData } from "./DiagramStore";
import { DiagramSensorNodeData } from "../components/nodes/DiagramSensorNode";

/**
 * DiagramSensorNodeStore class
 * @class
 * @description Handles operations specific to sensor nodes in the Diagram diagram.
 */
export default class DiagramSensorNodeStore {
  /**
   * @constructor
   * @description Initializes the store and makes it observable for MobX.
   */
  constructor() {
    makeAutoObservable(this);
  }

  /**
   * Updates the location of a specific sensor node.
   * @param {Node<NodeData>[]} nodes - Array of all nodes in the diagram.
   * @param {string} nodeId - ID of the node to be updated.
   * @param {MappedGooglePlace | null} location - New location data for the node.
   * @returns {Node<NodeData>[]} Updated array of nodes.
   */
  updateNodeLocation(
    nodes: Node<NodeData>[],
    nodeId: string,
    location: MappedGooglePlace | null
  ): Node<NodeData>[] {
    return nodes.map((node) => {
      if (node.id === nodeId && node.type === "sensorNode") {
        return {
          ...node,
          data: {
            ...node.data,
            city: location || { id: "", city: "" },
          },
        } as Node<DiagramSensorNodeData>;
      }
      return node;
    });
  }
}
