import { post, put, del, patch, get } from "@/utils/api/apiClient";
import { DataMapper } from "@/types/mapperTypes";
import { BasicDataProvider } from "./BasicDataProvider";

/**
 * Abstract class for providing single data functionality.
 * Extends BasicDataProvider to handle single data operations.
 *
 * @template ServerModel - The type of data returned from the server.
 * @template MappedModel - The type of data after mapping (defaults to ServerModel if not specified).
 */
export abstract class SingleDataProvider<
  ServerModel,
  MappedModel = ServerModel
> extends BasicDataProvider<ServerModel> {
  /** Stores the current data value */
  protected dataValue: ServerModel | null = null;
  /** Optional mapper to transform server data */
  protected mapper?: DataMapper<ServerModel, MappedModel>;

  /**
   * Constructs a new SingleDataProvider.
   *
   * @param {DataMapper<ServerModel, MappedModel>} [mapper] - Optional mapper to transform server data.
   */
  constructor(mapper?: DataMapper<ServerModel, MappedModel>) {
    super();
    this.mapper = mapper;
  }

  /** Abstract getter for accessing the mapped data */
  abstract get data(): MappedModel;

  /**
   * Updates the internal data value.
   *
   * @param {ServerModel} response - The new data value to set.
   */
  protected updateData(response: ServerModel) {
    this.dataValue = response;
  }

  /**
   * Retrieves the mapped data, applying the mapper if present.
   *
   * @returns {MappedModel} The mapped data.
   * @throws {Error} If no data is available.
   */
  protected getMappedData(): MappedModel {
    if (!this.dataValue) {
      throw new Error("errorData");
    }
    return this.mapper
      ? this.mapper.mapData(this.dataValue)
      : (this.dataValue as unknown as MappedModel);
  }

  /**
   * Executes a request and handles the response.
   *
   * @param {Function} requestFn - The function to execute the request.
   * @param {boolean} [skipDataUpdate=false] - Whether to skip updating the data.
   * @returns {Promise<void>}
   */
  private async executeRequest(
    requestFn: () => Promise<{
      error?: string | null;
      data: ServerModel | null;
      status?: number;
    }>,
    skipDataUpdate: boolean = false
  ): Promise<void> {
    this.setLoading(true);
    try {
      const response = await requestFn();

      if (!skipDataUpdate) {
        if (response.error) {
          throw new Error(response.error || "Error occurred");
        }
        if (response.data === null && response.status !== 204) {
          throw new Error("No data received");
        }
        if (response.data !== null) {
          this.updateData(response.data);
        }
      }
    } finally {
      this.setLoading(false);
    }
  }

  /**
   * Fetches data from the server.
   *
   * @returns {Promise<void>}
   */
  getData = async (): Promise<void> => {
    return this.executeRequest(() => get<ServerModel>(this.url));
  };

  /**
   * Sends a POST request to the server.
   *
   * @template RequestModel
   * @param {RequestModel} body - The data to send in the request body.
   * @returns {Promise<void>}
   */
  postData = async <RequestModel = undefined>(
    body: RequestModel
  ): Promise<void> => {
    const skipDataUpdate = this.endpointURL === "api/logout";
    try {
      await this.executeRequest(
        () => post<RequestModel, ServerModel>(this.url, body),
        skipDataUpdate
      );
    } catch (error) {
      throw error;
    }
  };

  /**
   * Sends a PUT request to the server.
   *
   * @template RequestModel
   * @param {RequestModel} body - The data to send in the request body.
   * @returns {Promise<void>}
   */
  putData = async <RequestModel = undefined>(
    body: RequestModel
  ): Promise<void> => {
    return this.executeRequest(() =>
      put<RequestModel, ServerModel>(this.url, body)
    );
  };

  /**
   * Sends a PATCH request to the server.
   *
   * @template RequestModel
   * @param {RequestModel} body - The data to send in the request body.
   * @returns {Promise<void>}
   */
  patchData = async <RequestModel = undefined>(
    body: RequestModel
  ): Promise<void> => {
    return this.executeRequest(() =>
      patch<RequestModel, ServerModel>(this.url, body)
    );
  };

  /**
   * Sends a DELETE request to the server.
   *
   * @param {string | number} id - The ID of the resource to delete.
   * @returns {Promise<void>}
   */
  deleteData = async (id: string | number): Promise<void> => {
    return this.executeRequest(() =>
      del<undefined, ServerModel>(`${this.url}/${id}`)
    );
  };
}
