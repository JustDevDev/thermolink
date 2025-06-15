import {
  action,
  computed,
  makeObservable,
  observable,
  runInAction,
} from "mobx";
import { SettingStore } from "../setting/settingStore";
import { LoginData } from "../../types/api/responseTypes";
import { UserAuthProvider } from "./data/userAuthProvider";
import { UserUpdateProvider } from "./data/userUpdateProvider";
import { alertManager } from "@/utils/alert/AlertManager";
import { RequestUserUpdateTypes } from "@/types/api/requestTypes";

// List of private fields observed by MobX
type privateVariables =
  | "_isLoaded"
  | "_userEmail"
  | "_userId"
  | "_avatar"
  | "_userFirstName"
  | "_userLastName"
  | "_resetUserData"
  | "_authProvider"
  | "_hintPassed";

/**
 * Singleton store responsible for managing authenticated user data.
 * Extends the application's base SettingStore.
 * @class
 */
export class UserStore extends SettingStore {
  // Holds the single instance of UserStore
  private static instance: UserStore;

  /**
   * Initializes the UserStore instance, setting up MobX observables and enforcing singleton pattern.
   */
  constructor() {
    if (UserStore.instance) {
      return UserStore.instance;
    }
    super();
    makeObservable<UserStore, privateVariables>(this, {
      _isLoaded: observable,
      _userEmail: observable,
      _avatar: observable,
      _userFirstName: observable,
      _userLastName: observable,
      _userId: observable,
      _authProvider: observable,
      _hintPassed: observable,
      _resetUserData: action,
      saveLoginData: action,
      isLoggedIn: computed,
    });
    UserStore.instance = this;
  }

  // --- Private user state ---
  private _isLoaded: boolean = false; // Indicates if the login state has been loaded
  private _userEmail: string = "";
  private _userId: string = "";
  private _avatar?: string;
  private _userFirstName: string = "";
  private _userLastName: string = "";
  private _authProvider: "local" | "google" = "local";
  private _hintPassed: boolean = false;

  // --- API data providers ---
  readonly userAuthProvider: UserAuthProvider = new UserAuthProvider();
  readonly userUpdateProvider: UserUpdateProvider = new UserUpdateProvider();

  /**
   * Returns whether the store has finished loading the user state.
   * @returns {boolean} True if loading is complete.
   */
  get isLoaded(): boolean {
    return this._isLoaded;
  }

  /**
   * Sets the store's loaded state.
   * @param {boolean} value - The new loaded state.
   */
  set isLoaded(value: boolean) {
    this._isLoaded = value;
  }

  /**
   * Indicates if a user is currently logged in.
   * @returns {boolean} True if userId is non-empty.
   */
  get isLoggedIn(): boolean {
    return !!this._userId;
  }

  /**
   * Returns the authentication provider used by the current user.
   * @returns {"local"|"google"} The auth provider.
   */
  get authProvider(): "local" | "google" {
    return this._authProvider;
  }

  /**
   * Stores the successful login data into the store.
   * @param {LoginData} userData - The login data returned from the API.
   */
  saveLoginData(userData: LoginData): void {
    this._userEmail = userData.userEmail;
    this._userId = userData.userId;
    this._avatar = userData.userAvatar;
    this._userFirstName = userData.userFirstName;
    this._userLastName = userData.userLastName;
    this._authProvider = userData.authProvider;
    this._hintPassed = userData.hintPassed;
  }

  /**
   * Clears all stored user data back to default values.
   * @private
   */
  private _resetUserData(): void {
    this._userEmail = "";
    this._userId = "";
    this._avatar = undefined;
    this._userFirstName = "";
    this._userLastName = "";
    this._authProvider = "local";
  }

  // --- Public getters for user data ---
  /**
   * Gets the user's email.
   * @returns {string} The stored user email.
   */
  get userEmail(): string {
    return this._userEmail;
  }

  /**
   * Gets the user's ID.
   * @returns {string} The stored user ID.
   */
  get userId(): string {
    return this._userId;
  }

  /**
   * Gets the user's avatar URL.
   * @returns {string|undefined} The stored avatar URL, or undefined.
   */
  get avatar(): string | undefined {
    return this._avatar;
  }

  /**
   * Gets the user's first name.
   * @returns {string} The stored first name.
   */
  get userFirstName(): string {
    return this._userFirstName;
  }

  /**
   * Gets the user's last name.
   * @returns {string} The stored last name.
   */
  get userLastName(): string {
    return this._userLastName;
  }

  /**
   * Gets the hint passed state.
   * @returns {boolean} The stored hint passed state.
   */
  get hintPassed(): boolean {
    return this._hintPassed;
  }

  /**
   * Attempts to load the current user's login state from the API.
   * If not authenticated, resets the user data.
   * @async
   * @returns {Promise<void>} Resolves when loading is complete.
   */
  async checkIsLoaded(): Promise<void> {
    try {
      // Fetches data; provider.data throws if not authenticated
      await this.userAuthProvider.getData();
      const data = this.userAuthProvider.data;
      runInAction(() => {
        this.saveLoginData(data);
        this.isLoaded = true;
      });
    } catch (err) {
      console.error("Failed to load user state:", err);
      runInAction(() => {
        this._resetUserData();
        this.isLoaded = true;
      });
    }
  }

  /**
   * Updates user profile data via the API.
   * Builds a payload of changed fields and sends a PUT request.
   * @async
   * @param {Partial<RequestUserUpdateTypes>} data - The data to update.
   * @returns {Promise<{error: string}>} Resolves with an object containing an error message, if any.
   */
  async updateUserData(
    data: Partial<RequestUserUpdateTypes>,
    showAlert: boolean = true
  ): Promise<{ error: string }> {
    try {
      if (Object.keys(data).length > 0) {
        await this.userUpdateProvider.putData<RequestUserUpdateTypes>(data);
        if (showAlert) {
          alertManager.success({ intld: "success.updateUserData" });
        }
        runInAction(() => {
          if (data.userAvatar !== undefined) this._avatar = data.userAvatar;
          if (data.userFirstName !== undefined)
            this._userFirstName = data.userFirstName;
          if (data.userLastName !== undefined)
            this._userLastName = data.userLastName;
          if (data.hintPassed !== undefined) this._hintPassed = data.hintPassed;
        });
      }
      return { error: "" };
    } catch (error) {
      return { error: (error as Error).message || "Unknown error" };
    }
  }

  /**
   * Logs out the user locally by clearing stored data.
   * @returns {void}
   */
  logout(): void {
    this._resetUserData();
  }
}
