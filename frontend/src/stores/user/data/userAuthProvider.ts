import { SingleDataProvider } from "@/data/SingleDataProvider";
import { LoginData, ResponseLoginType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class UserAuthProvider extends SingleDataProvider<
  ResponseLoginType,
  LoginData
> {
  get endpointURL(): string {
    return "api/authMe";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): LoginData {
    const resp = this.dataValue!;

    if (resp.error) {
      throw new Error(resp.error);
    }

    return resp as LoginData;
  }
}
