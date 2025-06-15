/**
 * @file TableStore.tsx
 * @description Defines an abstract class for managing table data with MobX state management.
 */

import { TableDataProvider } from "@/data/TableDataProvider";
import { TId } from "@/types/types";
import {
  computed,
  makeObservable,
  observable,
  reaction,
  action,
  runInAction,
} from "mobx";

/**
 * Type alias for private variables used in MobX observables
 */
type privateVariables =
  | "_items"
  | "_rowsPerPage"
  | "_currentPage"
  | "_totalItems";

/**
 * Abstract class for managing table data with sorting, pagination, and data fetching capabilities.
 * @template ServerModel - The data model returned by the server
 * @template MappedModel - The transformed data model (defaults to ServerModel)
 * @template Provider - The data provider class (extends TableDataProvider)
 */
export abstract class TableStore<
  ServerModel extends TId<string>,
  MappedModel = ServerModel,
  Provider extends TableDataProvider<
    ServerModel,
    string,
    MappedModel
  > = TableDataProvider<ServerModel, string, MappedModel>
> {
  constructor(provider: Provider) {
    // Initialize MobX observables and actions
    makeObservable<
      TableStore<ServerModel, MappedModel, Provider>,
      privateVariables | "_sortColumn" | "_sortDirection"
    >(this, {
      _items: observable,
      _rowsPerPage: observable,
      _currentPage: observable,
      _totalItems: observable,
      _sortColumn: observable,
      _sortDirection: observable,
      currentPage: computed,
      totalItems: computed,
      items: computed,
      handleSort: action,
      changeRowsPerPage: action,
      changePage: action,
    });
    this.tableProvider = provider;

    // Set up a reaction to fetch data when relevant parameters change
    reaction(
      () => [
        this._currentPage,
        this._rowsPerPage,
        this._sortColumn,
        this._sortDirection,
      ],
      () => {
        if (
          this._sortColumn ||
          this._currentPage >= 0 ||
          this._rowsPerPage !== 5
        ) {
          this.getData();
        }
      }
    );
  }

  // Private observable properties
  private _items: MappedModel[] = [];
  private _rowsPerPage: number = 5;
  private _rowsPerPageOptions = [5, 10, 25];
  private _currentPage: number = 0;
  private _totalItems: number = 0;
  private _sortColumn: string = "";
  private _sortDirection: "asc" | "desc" | "none" = "none";
  readonly tableProvider: Provider;

  // Getter methods for computed and observable properties
  get rowsPerPageOptions() {
    return this._rowsPerPageOptions;
  }

  get currentPage(): number {
    return this._currentPage;
  }

  get items(): MappedModel[] {
    return this._items;
  }

  get rowsPerPage(): number {
    return this._rowsPerPage;
  }

  get totalItems(): number {
    return this._totalItems;
  }

  get loading(): boolean {
    return this.tableProvider.loading;
  }

  // Setter methods for observable properties
  set currentPage(value: number) {
    this._currentPage = value;
  }

  set rowsPerPage(value: number) {
    this._rowsPerPage = value;
  }

  set items(value: MappedModel[]) {
    this._items = value;
  }

  set totalItems(value: number) {
    this._totalItems = value;
  }

  /**
   * Adjusts the current page when the number of rows per page changes
   * to ensure the current page is still valid.
   */
  private adjustCurrentPageAfterRowsChange = () => {
    const totalPages = Math.ceil(this.totalItems / this.rowsPerPage);
    if (this.currentPage > totalPages - 1) {
      this.currentPage = Math.max(totalPages - 1, 0);
      this.tableProvider.page = this.currentPage;
    }
  };

  /**
   * Fetches data from the table provider and updates the store
   */
  getData = async () => {
    await this.tableProvider.getData();
    this.items = this.tableProvider.data.content;
    this.totalItems = this.tableProvider.data.totalElements;
  };

  /**
   * Changes the current page and updates the table provider
   * @param page - The new page number
   */
  changePage = (page: number) => {
    this.tableProvider.page = page;
    this.currentPage = page;
  };

  /**
   * Changes the number of rows per page and adjusts the current page if necessary
   * @param rowsPerPage - The new number of rows per page
   */
  changeRowsPerPage = (rowsPerPage: number) => {
    this.tableProvider.rowsPerPage = rowsPerPage;
    this.rowsPerPage = rowsPerPage;
    this.adjustCurrentPageAfterRowsChange();
  };

  /**
   * Handles sorting of the table data
   * @param columnName - The name of the column to sort by
   * @param direction - The sort direction ("asc", "desc", or "none")
   */
  handleSort = (columnName: string, direction: "asc" | "desc" | "none") => {
    runInAction(() => {
      this._sortColumn = direction === "none" ? "" : columnName;
      this._sortDirection = direction;
      this.tableProvider.sort =
        direction === "none" ? "" : `${columnName},${direction}`;

      if (direction === "none") {
        this.getData();
      }
    });
  };
}
