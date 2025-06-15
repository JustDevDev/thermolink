import { TableDataProvider } from "@/data/TableDataProvider";
import { ResponseSensorsTableTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export default class SensorsDataTableProvider extends TableDataProvider<
  ResponseSensorsTableTypes,
  string
> {
  get endpointURL(): string {
    return "api/sensors";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }
}
