import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseForgottenPasswordNewType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class ForgottenPasswordNewProvider extends SingleDataProvider<ResponseForgottenPasswordNewType> {
  get endpointURL(): string {
    return `api/user/forgottenPassword/newPassword`;
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseForgottenPasswordNewType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
