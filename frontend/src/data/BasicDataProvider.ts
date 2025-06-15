import { get } from "@/utils/api/apiClient";
import { makeObservable, observable, action } from "mobx";
/**
 * Type definition for private variables to be made observable in MobX.
 */
type privateVariables = "_loading";

/**
 * Abstract base class for data providers.
 * @template ServerModel - The type of data returned from the server.
 */
export abstract class BasicDataProvider<ServerModel> {
  /**
   * Constructor initializes MobX observables and actions.
   */
  constructor() {
    makeObservable<BasicDataProvider<ServerModel>, privateVariables>(this, {
      _loading: observable,
      setLoading: action,
    });
  }

  /**
   * Private loading state.
   * @private
   */
  private _loading: boolean = false;

  /**
   * Abstract getter for the endpoint URL.
   * @abstract
   * @protected
   */
  protected abstract get endpointURL(): string;

  /**
   * Abstract getter for the API URL.
   * @abstract
   * @protected
   */
  protected abstract get apiURL(): string;

  /**
   * Abstract method to update data after fetching.
   * @abstract
   * @protected
   * @param {ServerModel} response - The data received from the server.
   */
  protected abstract updateData(response: ServerModel): void;

  /**
   * Getter for the loading state.
   * @returns {boolean} The current loading state.
   */
  get loading(): boolean {
    return this._loading;
  }

  /**
   * Setter for the loading state.
   * @param {boolean} value - The new loading state.
   */
  setLoading(value: boolean) {
    this._loading = value;
  }

  /**
   * Private method to fetch data from the server.
   * @private
   * @async
   */
  private _fetch = async () => {
    this.setLoading(true);

    try {
      const response = await get<ServerModel>(this.url);
      if (response.error) {
        throw new Error(response.error || "Error occurred");
      }
      if (response.data === null && response.status !== 204) {
        throw new Error("No data received");
      }
      if (response.data !== null) {
        this.updateData(response.data);
      }
    } finally {
      this.setLoading(false);
    }
  };

  /**
   * Getter for the full URL (API URL + endpoint URL).
   * @returns {string} The full URL for the API request.
   */
  get url(): string {
    return `${this.apiURL}${this.endpointURL}`;
  }

  /**
   * Getter that triggers the fetch operation.
   * @returns {Promise<void>} A promise that resolves when the fetch operation is complete.
   */
  get fetch() {
    return this._fetch();
  }
}
