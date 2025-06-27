import { TError, TId } from "../types";

export type ResponseDataType = {
  sensors: SensorsDataType[];
  PLCs: PLCDataType[];
};

export type SensorsDataType = {
  id: string;
  place: string;
  connections: SensorsDataConnectionType[];
};

export type SensorsDataConnectionType = {
  PLCId: string;
  port: number;
};

export type PLCDataType = {
  id: string;
  name: string;
};

export type ResponseSourceType = {
  diagram: string;
};

export type ResponseGooglePlacesType = {
  id: string;
  place: string;
};

export interface ErrorResponse {
  error: string;
  message?: string;
}

export interface SuccessResponse {
  error: undefined;
  userEmail: string;
  userId: string;
  userAvatar: string;
  userFirstName: string;
  userLastName: string;
  authProvider: "local" | "google";
  hintPassed: boolean;
}

export type ResponseLoginType = ErrorResponse | SuccessResponse;
export type LoginData = SuccessResponse;

export type ResponseForgottenPasswordType = TError & {
  message?: string;
};

export type ResponseForgottenPasswordValidateType = TError & {
  message?: string;
};

export type ResponseForgottenPasswordNewType = TError & {
  message?: string;
};

export type ResponseVerifyAccountType = TError & {
  message?: string;
};

export type ResponseRegisterType = TError & {
  message?: string;
  userEmail?: string;
  userId?: string;
};

export type ResponseConnectionDiagramData = {
  diagram: string;
};

export type ResponseSensorsTableTypes = TId<string> & {
  place: string;
  temperature: number;
  averageTemperature: number;
  condition: string;
  lastTemperatureRecords: ResponseSensorsTableLastTemperatureRecordsTypes[];
  connectedPlcs: ResponseSensorsTableConnectedPlcsTypes[];
};

export type ResponseSensorsTableLastTemperatureRecordsTypes = TId<string> & {
  temperature: number;
  date: string;
};

export type ResponseSensorsTableConnectedPlcsTypes = TId<string> & {
  name: string;
  port: number;
};

export type ResponsePLCTableTypes = TId<string> & {
  name: string;
  connectedSensors: ResponsePLCTableConnectedSensorsTypes[];
};

export type ResponsePLCTableConnectedSensorsTypes = TId<string> & {
  id: string;
  port: number;
  place: string;
};

export type ResponseDashboardPLCsTypes = TId<string> & {
  name: string;
};

export type ResponseDashboardPLCTypes = TId<string> & {
  name: string;
  connectedSensors: ResponseDashboardPLCConnectedSensorsTypes[];
};

export type ResponseDashboardPLCConnectedSensorsTypes = {
  id: string;
  port: number;
  place: string;
  temperatures: {
    date: Date;
    temperature: number;
  }[];
};

export type ResponseDashboardContinentTypes = {
  continent: string;
  count: number;
};

export type ResponseDashboardKPIsTypes = {
  activeSensors: number;
  inActiveSensors: number;
  todayHighTemperature: ResponseDashboardKPIsPlaceTypes;
  todayLowTemperature: ResponseDashboardKPIsPlaceTypes;
};

export type ResponseDashboardKPIsPlaceTypes = {
  place: string;
  temperature: number | null;
};

export type ResponseUserUpdateTypes = TError & {
  message?: string;
};
