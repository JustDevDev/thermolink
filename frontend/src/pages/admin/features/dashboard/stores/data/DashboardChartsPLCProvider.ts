import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseDashboardPLCTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class DashboardChartsPLCProvider extends SingleDataProvider<ResponseDashboardPLCTypes> {
  private _plcId: string = "";

  set id(value: string) {
    this._plcId = value;
  }

  get id(): string {
    return this._plcId;
  }

  get endpointURL(): string {
    return `api/dashboard/charts/plc/${this.id}`;
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseDashboardPLCTypes {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
