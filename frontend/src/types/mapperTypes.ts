import { ResponseGooglePlacesType } from "./api/responseTypes";

export type DataMapper<TResponse, TMapped> = {
  mapData: (response: TResponse) => TMapped;
};

export type MappedGooglePlace = {
  id: string;
  city: string;
};

export const googlePlacesMapper: DataMapper<
  ResponseGooglePlacesType[],
  MappedGooglePlace[]
> = {
  mapData: (response: ResponseGooglePlacesType[]): MappedGooglePlace[] => {
    return response.map((data) => ({
      id: data.id,
      city: data.place,
    }));
  },
};
