import { ResponseGooglePlacesType } from "@/types/api/responseTypes";
import { environment } from "@/utils/environment";
import { MappedGooglePlace, googlePlacesMapper } from "@/types/mapperTypes";
import { MultiDataSelectProvider } from "@/data/MultiDataSelectProvider";

export class DiagramPlaceDataProvider extends MultiDataSelectProvider<
  ResponseGooglePlacesType[],
  MappedGooglePlace[]
> {
  constructor() {
    super(googlePlacesMapper);
  }

  get endpointURL(): string {
    return `api/place?place=${encodeURIComponent(this.option)}`;
  }

  get apiURL(): string {
    return environment.apiUrl;
  }

  get data(): MappedGooglePlace[] {
    return this.getMappedData();
  }
}
