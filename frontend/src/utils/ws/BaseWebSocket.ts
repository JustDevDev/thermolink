/**
 * BaseWebSocket: A robust, abstract class for managing WebSocket connections.
 *
 * This class encapsulates the core functionality of a WebSocket connection,
 * providing a flexible and extensible base for specific implementations.
 *
 * Key features:
 * 1. Automatic reconnection with configurable attempts and intervals
 * 2. Abstract methods for customizable event handling
 * 3. Type-safe message sending and parsing
 * 4. Connection status tracking
 *
 * Usage: Extend this class to create specialized WebSocket connections,
 * implementing the abstract methods to define specific behavior.
 */

import { WSResponse } from "@/types/ws/messageTypes";

/**
 * WebSocketConfig: Configuration interface for WebSocket connections.
 *
 * @property {string} url - The WebSocket server URL
 * @property {boolean} [autoReconnect] - Enable automatic reconnection (default: true)
 * @property {number} [reconnectInterval] - Time between reconnection attempts in ms (default: 5000)
 * @property {number} [maxReconnectAttempts] - Maximum number of reconnection attempts (default: 5)
 */
export interface WebSocketConfig {
  url: string;
  autoReconnect?: boolean;
  reconnectInterval?: number;
  maxReconnectAttempts?: number;
}

export abstract class BaseWebSocket {
  protected socket: WebSocket | null = null;
  protected isConnected: boolean = false;
  private reconnectAttempts: number = 0;
  private readonly config: WebSocketConfig;

  /**
   * Constructor: Initialize the WebSocket connection with the provided configuration.
   *
   * @param {WebSocketConfig} config - The configuration for the WebSocket connection
   */
  constructor(config: WebSocketConfig) {
    this.config = {
      autoReconnect: true,
      maxReconnectAttempts: 5,
      ...config,
    };
  }

  /**
   * onConnect: Establish a WebSocket connection and set up event handlers.
   *
   * This method creates a new WebSocket instance if one doesn't exist or isn't open.
   */
  onConnect(): void {
    if (this.socket?.readyState === WebSocket.OPEN) return;

    this.socket = new WebSocket(this.config.url);
    this.setupEventHandlers();
  }

  /**
   * setupEventHandlers: Bind WebSocket event handlers to class methods.
   *
   * This method sets up handlers for open, close, error, and message events.
   */
  protected setupEventHandlers(): void {
    if (!this.socket) return;

    this.socket.onopen = this.handleOpen.bind(this);
    this.socket.onclose = this.handleClose.bind(this);
    this.socket.onerror = this.handleError.bind(this);
    this.socket.onmessage = this.handleMessage.bind(this);
  }

  /**
   * handleOpen: Process the WebSocket open event.
   *
   * This method updates the connection status, resets reconnection attempts,
   * and calls the abstract onConnected method.
   */
  protected handleOpen(): void {
    this.isConnected = true;
    this.reconnectAttempts = 0;
    this.onConnected();
  }

  /**
   * handleClose: Process the WebSocket close event.
   *
   * This method updates the connection status, calls the abstract onDisconnected method,
   * and initiates reconnection if configured.
   */
  protected handleClose(): void {
    this.isConnected = false;
    this.onDisconnected();

    if (
      this.config.autoReconnect &&
      this.config.reconnectInterval &&
      this.reconnectAttempts < (this.config.maxReconnectAttempts || 5)
    ) {
      setTimeout(() => {
        this.reconnectAttempts++;
        this.onConnect();
      }, this.config.reconnectInterval);
    }
  }

  /**
   * handleError: Process the WebSocket error event.
   *
   * @param {Event} event - The error event object
   */
  protected handleError(event: Event): void {
    this.onError(event);
  }

  /**
   * handleMessage: Process incoming WebSocket messages.
   *
   * This method parses the incoming message as JSON and calls the abstract onMessage method.
   *
   * @param {MessageEvent} event - The message event containing the received data
   */
  private handleMessage(event: MessageEvent): void {
    try {
      const data = JSON.parse(event.data);
      const response = data as WSResponse;
      this.onMessage(response);
    } catch (error) {
      console.error("Failed to parse WebSocket message:", error);
    }
  }

  /**
   * Abstract methods to be implemented by subclasses:
   *
   * onMessage: Handle parsed WebSocket messages
   * onConnected: React to successful connections
   * onDisconnected: Handle disconnection events
   * onError: Process WebSocket errors
   */
  protected abstract onMessage(response: WSResponse): void;
  protected abstract onConnected(): void;
  protected abstract onDisconnected(): void;
  protected abstract onError(error: Event): void;

  /**
   * onDisconnect: Close the WebSocket connection.
   *
   * This method safely closes the connection and resets the socket.
   */
  public onDisconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }

  /**
   * sendMessage: Send a message through the WebSocket connection.
   *
   * This method stringifies the message and sends it if the connection is open.
   *
   * @param {T} message - The message to send (will be JSON stringified)
   */
  public sendMessage<T>(message: T): void {
    if (this.socket?.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(message));
    } else {
      console.error("WebSocket is not connected");
    }
  }

  /**
   * connectionStatus: Get the current connection status.
   *
   * @returns {boolean} True if connected, false otherwise
   */
  public get connectionStatus(): boolean {
    return this.isConnected;
  }
}
