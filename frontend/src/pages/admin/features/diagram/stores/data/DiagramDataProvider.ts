import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseDataType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class DiagramDataProvider extends SingleDataProvider<ResponseDataType> {
  get endpointURL(): string {
    return "api/diagram/data";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseDataType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
