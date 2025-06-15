import { TableStore } from "@/components/basic-components/table/data/TableStore";
import SensorsDataTableProvider from "./data/SensorsDataTableProvider";
import { ResponseSensorsTableTypes } from "@/types/api/responseTypes";

export default class SensorsDataTableStore extends TableStore<
  ResponseSensorsTableTypes,
  ResponseSensorsTableTypes,
  SensorsDataTableProvider
> {
  constructor() {
    super(new SensorsDataTableProvider());
  }
}
