import { makeAutoObservable } from "mobx";

/**
 * Store managing PLC node diagram data and chart visualization
 * Implements MobX state management for reactive updates
 */
export default class DiagramPLCNodeStore {
  /**
   * Simulated PLC data arrays representing different metrics
   * Each array contains 10 data points for chart visualization
   * Values range between 0-9 representing signal strength or similar metric
   * @private
   */
  private chartDataArray: number[][] = [
    [9, 1, 9, 0, 9, 2, 9, 1, 8, 0],
    [9, 0, 9, 1, 9, 0, 8, 9, 1, 9],
    [8, 1, 9, 0, 9, 2, 9, 0, 9, 1],
    [9, 2, 8, 1, 9, 0, 9, 1, 9, 0],
  ];

  /**
   * Initializes the store and makes it observable using MobX
   */
  constructor() {
    makeAutoObservable(this);
  }

  /**
   * Updates chart data for a specific metric index
   * Implements a simple algorithm to generate semi-random values:
   * - If last value > 5, generates low value (0-2)
   * - If last value <= 5, generates high value (8-9)
   * Maintains fixed array length by removing oldest value
   *
   * @param index - Index of the metric array to update
   */
  updateChartData(index: number) {
    const lastValue =
      this.chartDataArray[index][this.chartDataArray[index].length - 1];
    const newValue =
      lastValue > 5 ? Math.floor(Math.random() * 3) : 8 + Math.random();
    this.chartDataArray[index] = [
      ...this.chartDataArray[index].slice(-9),
      newValue,
    ];
  }

  /**
   * Returns the current state of all chart data arrays
   * @returns {number[][]} Array of arrays containing metric values
   */
  get data() {
    return this.chartDataArray;
  }
}
