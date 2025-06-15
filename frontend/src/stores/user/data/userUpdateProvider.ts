import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseUserUpdateTypes } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class UserUpdateProvider extends SingleDataProvider<ResponseUserUpdateTypes> {
  get endpointURL(): string {
    return "api/user";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseUserUpdateTypes {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
