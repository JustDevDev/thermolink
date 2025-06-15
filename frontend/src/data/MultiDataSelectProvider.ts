import { DataMapper } from "@/types/mapperTypes";
import { SingleDataProvider } from "./SingleDataProvider";

/**
 * Abstract class for providing multi-data select functionality.
 * Extends SingleDataProvider to handle multiple data selections.
 *
 * @template ServerModel - The type of data returned from the server.
 * @template MappedModel - The type of data after mapping (defaults to ServerModel if not specified).
 */
export abstract class MultiDataSelectProvider<
  ServerModel,
  MappedModel = ServerModel
> extends SingleDataProvider<ServerModel, MappedModel> {
  /**
   * Constructs a new MultiDataSelectProvider.
   *
   * @param {DataMapper<ServerModel, MappedModel>} [mapper] - Optional mapper to transform server data.
   */
  constructor(mapper?: DataMapper<ServerModel, MappedModel>) {
    super(mapper);
    this._option = "";
  }

  /**
   * Protected field to store the current option.
   */
  protected _option: string;

  /**
   * Setter for the option field.
   *
   * @param {string} option - The new option value to set.
   */
  set option(option: string) {
    this._option = option;
  }

  /**
   * Getter for the option field.
   *
   * @returns {string} The current option value.
   */
  get option(): string {
    return this._option;
  }
}
