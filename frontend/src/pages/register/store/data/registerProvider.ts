import { SingleDataProvider } from "@/data/SingleDataProvider";
import { ResponseRegisterType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";

/**
 * RegisterProvider is a class that extends BasicDataProvider and provides the endpoint URL for the Register component.
 */
export class RegisterProvider extends SingleDataProvider<ResponseRegisterType> {
  get endpointURL(): string {
    return "api/register";
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): ResponseRegisterType {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.dataValue;
  }
}
