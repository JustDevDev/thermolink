/**
 * AlertManager: A centralized class for managing application alerts using MobX.
 *
 * This class provides a robust system for creating, displaying, and managing alerts
 * across the application. It leverages MobX for state management and reactivity.
 */

import { makeAutoObservable, action } from "mobx";
import { translate } from "@/utils/translate/translate";

// Define the possible types of alerts
export type AlertType = "success" | "error" | "warning" | "info";

// Interface for alert creation options
interface AlertOptions {
  message?: string; // Direct message to display
  intld?: string; // Internationalization key for translated messages
  duration?: number; // Custom duration in milliseconds
  persistent?: boolean; // If true, alert stays until manually closed
}

// Structure of an alert object
interface Alert {
  id: string;
  type: AlertType;
  message: string;
  timestamp: number;
  duration: number;
  persistent: boolean;
}

// Duration (in milliseconds) for which an alert is displayed
const DEFAULT_ALERT_DURATION = 2000;

class AlertManager {
  // Observable array of current alerts
  alerts: Alert[] = [];

  constructor() {
    makeAutoObservable(this);
    this.cleanupAlerts();
  }

  /**
   * Generates a unique ID for each alert
   * @returns {string} A unique identifier
   */
  private generateId(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  /**
   * Sets up an interval to remove expired alerts
   */
  private cleanupAlerts = () => {
    setInterval(
      action("cleanup alerts", () => {
        const now = Date.now();
        this.alerts = this.alerts.filter(
          (alert) => alert.persistent || now - alert.timestamp < alert.duration
        );
      }),
      1000
    );
  };

  /**
   * Creates and displays a new alert
   * @param {AlertType} type - The type of alert
   * @param {AlertOptions} options - Options for creating the alert
   */
  private showAlert(type: AlertType, options: AlertOptions) {
    const { message, intld, duration, persistent = false } = options;

    // Use translated message if intld is provided, otherwise use direct message
    const alertMessage = intld ? translate(intld) : message;

    if (!alertMessage) {
      console.warn("Either message or intld must be provided");
      return;
    }

    const alert: Alert = {
      id: this.generateId(),
      type,
      message: alertMessage,
      timestamp: Date.now(),
      duration: duration || DEFAULT_ALERT_DURATION,
      persistent,
    };

    this.alerts.push(alert);

    // Set up auto-removal of the alert after duration (if not persistent)
    if (!persistent) {
      setTimeout(
        action("remove alert", () => {
          this.removeAlert(alert.id);
        }),
        alert.duration
      );
    }
  }

  /**
   * Removes an alert by its ID
   * @param {string} id - The ID of the alert to remove
   */
  removeAlert = action("remove alert", (id: string) => {
    this.alerts = this.alerts.filter((alert) => alert.id !== id);
  });

  // Public methods for creating different types of alerts
  success = action("success alert", (options: AlertOptions) => {
    this.showAlert("success", options);
  });

  error = action("error alert", (options: AlertOptions) => {
    this.showAlert("error", options);
  });

  warning = action("warning alert", (options: AlertOptions) => {
    this.showAlert("warning", options);
  });

  info = action("info alert", (options: AlertOptions) => {
    this.showAlert("info", options);
  });
}

// Export a singleton instance of AlertManager
export const alertManager = new AlertManager();
