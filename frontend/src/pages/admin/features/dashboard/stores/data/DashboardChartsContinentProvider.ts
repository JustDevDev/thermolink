import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseDashboardContinentTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class DashboardChartsContinentProvider extends SingleDataProvider<
  ResponseDashboardContinentTypes[]
> {
  get endpointURL(): string {
    return "api/dashboard/charts/continents";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseDashboardContinentTypes[] {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
