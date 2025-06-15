import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseForgottenPasswordType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class ForgottenPasswordProvider extends SingleDataProvider<ResponseForgottenPasswordType> {
  get endpointURL(): string {
    return "api/user/forgottenPassword";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseForgottenPasswordType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
