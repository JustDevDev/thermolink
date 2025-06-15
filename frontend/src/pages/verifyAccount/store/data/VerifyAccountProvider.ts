import { SingleDataProvider } from "@/data/SingleDataProvider";
import {
  ResponseForgottenPasswordNewType,
  ResponseVerifyAccountType,
} from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

export class VerifyAccountProvider extends SingleDataProvider<ResponseVerifyAccountType> {
  private _token: string = "";

  set token(value: string) {
    this._token = value;
  }

  get endpointURL(): string {
    return `api/verifyAccount/${this._token}/validate`;
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
