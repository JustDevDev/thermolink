import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseDashboardKPIsTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class DashboardKPIsProvider extends SingleDataProvider<ResponseDashboardKPIsTypes> {
  get endpointURL(): string {
    return "api/dashboard/kpis";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseDashboardKPIsTypes {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
