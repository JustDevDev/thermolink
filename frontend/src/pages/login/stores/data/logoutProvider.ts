import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseLoginType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class LogoutProvider extends SingleDataProvider<ResponseLoginType> {
  get endpointURL(): string {
    return "api/logout";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseLoginType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
