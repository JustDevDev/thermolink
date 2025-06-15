import { ResponseSensorsTableTypes, TId } from "@/types/types";
import { SingleDataProvider } from "./SingleDataProvider";

/**
 * Abstract class for providing table data functionality.
 * Extends SingleDataProvider to handle table-specific operations.
 *
 * @template ServerItem - The type of individual items returned from the server.
 * @template T - The type of the ID field (defaults to number).
 * @template MappedModel - The type of data after mapping (defaults to ServerItem).
 */
export abstract class TableDataProvider<
  ServerItem extends TId<T>,
  T extends number | string = number,
  MappedModel = ServerItem
> extends SingleDataProvider<
  ResponseSensorsTableTypes<ServerItem>,
  ResponseSensorsTableTypes<MappedModel>
> {
  private _page: number = 0;
  private _rowsPerPage: number = 5;
  private _sort: string = "";

  /**
   * Constructs the full URL for the API request, including pagination and sorting parameters.
   * @returns {string} The complete URL for the API request.
   */
  get url(): string {
    const params = new URLSearchParams();
    params.append("page", this._page.toString());
    params.append("size", this._rowsPerPage.toString());
    if (this._sort) {
      params.append("sort", this._sort);
    }
    return `${this.apiURL}${this.endpointURL}?${params.toString()}`;
  }

  /**
   * Retrieves the mapped data, throwing an error if no data is available.
   * @returns {ResponseSensorsTableTypes<MappedModel>} The mapped table data.
   * @throws {Error} If no data is available.
   */
  get data(): ResponseSensorsTableTypes<MappedModel> {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.getMappedData();
  }

  /**
   * Sets the current page number for pagination.
   * @param {number} value - The page number to set.
   */
  set page(value: number) {
    this._page = value;
  }

  /**
   * Sets the number of rows per page for pagination.
   * @param {number} value - The number of rows per page to set.
   */
  set rowsPerPage(value: number) {
    this._rowsPerPage = value;
  }

  /**
   * Sets the sorting criteria for the table data.
   * @param {string} value - The sorting criteria to apply.
   */
  set sort(value: string) {
    this._sort = value;
  }
}
