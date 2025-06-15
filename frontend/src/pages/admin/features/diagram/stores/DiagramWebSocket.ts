import {
  WSResponse,
  WSType,
  WSDiagramMessageContent,
} from "@/types/ws/messageTypes";
import { alertManager } from "@/utils/alert/AlertManager";
import { BaseWebSocket, WebSocketConfig } from "@/utils/ws/BaseWebSocket";

/**
 * DiagramWebSocket Class
 *
 * This class extends BaseWebSocket to provide specialized functionality
 * for handling diagram-related WebSocket communications.
 */

export class DiagramWebSocket extends BaseWebSocket {
  /**
   * @private
   * @readonly
   * User's email address for authentication
   */
  private readonly userEmail: string;

  /**
   * Flag to indicate if the disconnection is intentional
   * @private
   */
  private intentionalDisconnect: boolean = false;

  /**
   * @private
   * @readonly
   * Callback function to handle incoming diagram data
   */
  private readonly onDiagramData: (data: WSDiagramMessageContent) => void;

  /**
   * Constructor for ConnectionWebSocket
   *
   * @param {WebSocketConfig} config - Configuration for the WebSocket connection
   * @param {string} userEmail - User's email for authentication
   * @param {Function} onDiagramData - Callback to handle incoming diagram data
   */
  constructor(
    config: WebSocketConfig,
    userEmail: string,
    onDiagramData: (data: WSDiagramMessageContent) => void
  ) {
    super(config);
    this.userEmail = userEmail;
    this.onDiagramData = onDiagramData;
  }

  /**
   * Handles the connection event
   * Sends an authentication message upon successful connection
   */
  protected onConnected(): void {
    const authMessage = {
      type: "auth",
      data: {
        email: this.userEmail,
      },
    };

    this.sendMessage(authMessage);
    console.log("WebSocket connected");
  }

  /**
   * Handles the disconnection event
   */
  protected onDisconnected(): void {
    if (!this.intentionalDisconnect) {
      alertManager.error({
        intld: "error.errorData",
      });
    }
  }

  /**
   * Handles any errors that occur during the WebSocket communication
   *
   * @param {Event} error - The error event
   */
  protected onError(error: Event): void {
    console.error("WebSocket error:", error);
  }

  /**
   * Processes incoming WebSocket messages
   *
   * @param {WSResponse} response - The received WebSocket response
   */
  protected onMessage(response: WSResponse): void {
    switch (response.type) {
      case WSType.DIAGRAM:
        // Invoke the callback with the diagram data
        this.onDiagramData(response.message);
        break;

      default:
        console.log("Unhandled message type:", response.type);
    }
  }

  /**
   * Sets the intentional disconnect flag before disconnecting
   */
  public onDisconnect(): void {
    this.intentionalDisconnect = true;
    super.onDisconnect();
  }

  /**
   * Resets the intentional disconnect flag when reconnecting
   */
  public onConnect(): void {
    this.intentionalDisconnect = false;
    super.onConnect();
  }
}
