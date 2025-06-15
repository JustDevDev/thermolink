import { makeAutoObservable, reaction } from "mobx";
import { DashboardChartsPLCProvider } from "./data/DashboardChartsPLCProvider";
import { DashboardPLCsProvider } from "./data/DashboardPLCsProvider";
import {
  ResponseDashboardContinentTypes,
  ResponseDashboardKPIsTypes,
  ResponseDashboardPLCsTypes,
  ResponseDashboardPLCTypes,
} from "@/types/api/responseTypes";
import { DashboardChartsContinentProvider } from "./data/DashboardChartsContinentProvider";
import { DashboardKPIsProvider } from "./data/DashboardKPIsProvider";

/**
 * DashboardStore: A centralized state management class for the dashboard
 *
 * This class encapsulates the logic and data required for the dashboard,
 * utilizing MobX for state management and reactivity.
 */
export default class DashboardStore {
  /**
   * Constructor: Initializes the store and sets up reactions
   *
   * We use makeAutoObservable to automatically make all properties and methods observable.
   * This allows MobX to track changes and trigger re-renders when necessary.
   */
  constructor() {
    makeAutoObservable(this);

    // Initialize data providers
    this.chartsPLCsProvider = new DashboardChartsPLCProvider();
    this.plcsProvider = new DashboardPLCsProvider();
    this.continentProvider = new DashboardChartsContinentProvider();
    this.kpisProvider = new DashboardKPIsProvider();

    // Set up a reaction to load chart data when the selected PLC changes
    reaction(
      () => this.selectedPLCId,
      () => {
        this.loadChartsPLCs();
      }
    );
  }

  // Data providers
  chartsPLCsProvider: DashboardChartsPLCProvider;
  plcsProvider: DashboardPLCsProvider;
  continentProvider: DashboardChartsContinentProvider;
  kpisProvider: DashboardKPIsProvider;

  // Private state variables
  private _selectedPLCId: string = "";
  private _plcs: ResponseDashboardPLCsTypes[] = [];
  private _plc: ResponseDashboardPLCTypes = {
    id: "",
    name: "",
    connectedSensors: [],
  };
  private _continents: ResponseDashboardContinentTypes[] = [];
  private _KPIs: ResponseDashboardKPIsTypes = {
    activeSensors: 0,
    inActiveSensors: 0,
    todayHighTemperature: {
      place: "",
      temperature: null,
    },
    todayLowTemperature: {
      place: "",
      temperature: null,
    },
  };

  /**
   * loadData: Asynchronously loads all required data for the dashboard
   *
   * This method orchestrates the loading of PLCs, continents, and KPIs.
   * It's designed to be called when the dashboard is first mounted.
   */
  loadData = async () => {
    await this.loadContinents();
    await this.loadPLCs();
    await this.loadKPIs();
  };

  /**
   * loadPLCs: Fetches PLC data and sets the initial selected PLC
   *
   * After loading, it sets the first PLC as the selected one if available.
   */
  loadPLCs = async () => {
    await this.plcsProvider.getData();
    const newSelectedId = this.plcsProvider.data?.[0]?.id ?? "";

    // If the ID changed, we'll set it and reaction will handle calling loadChartsPLCs
    // If it didn't change, we need to call loadChartsPLCs explicitly
    if (newSelectedId !== this.selectedPLCId) {
      this.selectedPLCId = newSelectedId;
    } else {
      await this.loadChartsPLCs();
    }

    this.plcs = this.plcsProvider.data ?? [];
  };

  /**
   * loadChartsPLCs: Loads chart data for the selected PLC
   *
   * This method is triggered when the selected PLC changes.
   */
  loadChartsPLCs = async () => {
    this.chartsPLCsProvider.id = this.selectedPLCId;
    await this.chartsPLCsProvider.getData();
    this.plc = this.chartsPLCsProvider.data;
  };

  /**
   * loadContinents: Fetches and sets continent data
   */
  loadContinents = async () => {
    await this.continentProvider.getData();
    this.continents = this.continentProvider.data;
  };

  /**
   * loadKPIs: Fetches and sets Key Performance Indicators
   */
  loadKPIs = async () => {
    await this.kpisProvider.getData();
    this.KPIs = this.kpisProvider.data;
  };

  /**
   * totalSensors: Computes the total number of sensors across all continents
   *
   * @returns {number} The sum of sensor counts from all continents
   */
  totalSensors = (): number => {
    return this.continents.reduce((acc, item) => acc + item.count, 0);
  };

  // Getters and setters for private variables
  // These allow controlled access to the store's state

  set selectedPLCId(value: string) {
    this._selectedPLCId = value;
  }

  get selectedPLCId(): string {
    return this._selectedPLCId;
  }

  get plcs(): ResponseDashboardPLCsTypes[] {
    return this._plcs;
  }

  set plcs(value: ResponseDashboardPLCsTypes[]) {
    this._plcs = value;
  }

  get plc(): ResponseDashboardPLCTypes {
    return this._plc;
  }

  set plc(value: ResponseDashboardPLCTypes) {
    this._plc = value;
  }

  get continents(): ResponseDashboardContinentTypes[] {
    return this._continents;
  }

  set continents(value: ResponseDashboardContinentTypes[]) {
    this._continents = value;
  }

  get KPIs(): ResponseDashboardKPIsTypes | null {
    return this._KPIs;
  }

  set KPIs(value: ResponseDashboardKPIsTypes) {
    this._KPIs = value;
  }
}
