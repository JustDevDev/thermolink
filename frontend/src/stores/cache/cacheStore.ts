import { makeAutoObservable } from "mobx";

/**
 * Type for values that can be stored in localStorage
 * Note: localStorage can only store strings, so we need to handle serialization
 */
export type StorableValue = string | number | boolean | object | null;

/**
 * CacheStore is a class that provides methods to save, load and remove data from the local storage.
 * @class CacheStore
 * @exports CacheStore
 * @constructor
 * @method saveToLocalStorage - Saves data to the local storage.
 * @method loadFromLocalStorage - Loads data from the local storage.
 * @method removeFromLocalStorage - Removes data from the local storage.
 */

export class CacheStore {
  constructor() {
    makeAutoObservable(this);
  }

  /**
   * Saves data to the local storage.
   * @param key - The key to save the data.
   * @param value - The value to save.
   */
  saveToLocalStorage(key: string, value: StorableValue) {
    try {
      const serializedValue =
        typeof value === "string" ? value : JSON.stringify(value);
      localStorage.setItem(key, serializedValue);
    } catch (error) {
      console.error("Error saving to localStorage:", error);
    }
  }

  /**
   * Loads data from the local storage.
   * @param key - The key to load the data.
   * @returns The parsed value from localStorage, or null if not found
   */
  loadFromLocalStorage(key: string): StorableValue {
    try {
      const item = localStorage.getItem(key);
      if (item === null) return null;

      try {
        return JSON.parse(item);
      } catch {
        // If parsing fails, return the raw string
        return item;
      }
    } catch (error) {
      console.error("Error loading from localStorage:", error);
      return null;
    }
  }

  /**
   * Removes data from the local storage.
   * @param key - The key to remove the data.
   */
  removeFromLocalStorage(key: string) {
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.error("Error removing from localStorage:", error);
    }
  }
}
