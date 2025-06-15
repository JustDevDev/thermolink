import { TableStore } from "@/components/basic-components/table/data/TableStore";
import PLCDataTableProvider from "./data/PLCDataTableProvider";
import { ResponsePLCTableTypes } from "@/types/api/responseTypes";

export default class SensorsDataTableStore extends TableStore<
  ResponsePLCTableTypes,
  ResponsePLCTableTypes,
  PLCDataTableProvider
> {
  constructor() {
    super(new PLCDataTableProvider());
  }
}
