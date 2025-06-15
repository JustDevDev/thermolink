export enum WSType {
  DIAGRAM = "diagram",
}

type WSDiagramMessageModel = {
  type: WSType.DIAGRAM;
  message: WSDiagramMessageContent;
};

export type WSDiagramMessageContent = {
  content: WSDiagramMessageContentModel[];
};

export type WSDiagramMessageContentModel = {
  id: string;
  temperature: number;
  averageTemperature: number;
  condition: string;
};

export type WSResponse = WSDiagramMessageModel;
