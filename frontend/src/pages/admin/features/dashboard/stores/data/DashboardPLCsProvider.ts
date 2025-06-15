import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseDashboardPLCsTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class DashboardPLCsProvider extends SingleDataProvider<
  ResponseDashboardPLCsTypes[] | null
> {
  get endpointURL(): string {
    return "api/dashboard/plcs";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseDashboardPLCsTypes[] | null {
    return this.dataValue;
  }
}
