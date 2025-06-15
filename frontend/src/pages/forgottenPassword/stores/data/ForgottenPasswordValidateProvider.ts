import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseForgottenPasswordValidateType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class ForgottenPasswordValidateProvider extends SingleDataProvider<ResponseForgottenPasswordValidateType> {
  private _token: string = "";

  set token(value: string) {
    this._token = value;
  }

  get endpointURL(): string {
    return `api/user/forgottenPassword/${this._token}/validate`;
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseForgottenPasswordValidateType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
