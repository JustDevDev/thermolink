import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseSourceType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class DiagramSourceDataProvider extends SingleDataProvider<ResponseSourceType> {
  get endpointURL(): string {
    return "api/diagram/source";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseSourceType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
