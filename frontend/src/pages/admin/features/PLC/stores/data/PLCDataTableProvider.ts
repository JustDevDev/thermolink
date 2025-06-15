import { TableDataProvider } from "@/data/TableDataProvider";
import { ResponsePLCTableTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export default class PLCDataTableProvider extends TableDataProvider<
  ResponsePLCTableTypes,
  string
> {
  get endpointURL(): string {
    return "api/plcs";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }
}
