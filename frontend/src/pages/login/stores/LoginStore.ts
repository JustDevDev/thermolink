import { makeAutoObservable } from "mobx";
import { environment } from "@/utils/environment";
import { LoginProvider } from "./data/loginProvider";
import { ResponseLoginType, SuccessResponse } from "@/types/api/responseTypes";
import { RequestLoginType, RequestLogoutType } from "@/types/api/requestTypes";
import { UserStore } from "@/stores/user/userStore";
import { LogoutProvider } from "./data/logoutProvider";

export class LoginStore {
  // Singleton instance
  private static instance: LoginStore;

  constructor() {
    // Ensure only one instance of LoginStore is created
    if (LoginStore.instance) {
      return LoginStore.instance;
    }

    this._loginProvider = new LoginProvider();
    this._logoutProvider = new LogoutProvider();
    makeAutoObservable(this);

    LoginStore.instance = this;
  }

  // Private fields
  private _email: string = "";
  private _password: string = "";
  private _loginProvider!: LoginProvider;
  private _logoutProvider!: LogoutProvider;
  private readonly _userStore = new UserStore();

  // Getter for login provider
  get loginProvider() {
    return this._loginProvider;
  }

  /**
   * Attempts to log in the user
   * @returns A promise that resolves to ResponseLoginType
   */
  login = async (): Promise<ResponseLoginType> => {
    try {
      // Send login request
      await this._loginProvider.postData<RequestLoginType>({
        email: this._email,
        password: this._password,
      });

      const response = this._loginProvider.data;

      if (response.error) {
        return { error: response.error };
      } else {
        this.resetLoginData();
      }

      // Return user data if login successful
      const successResp: SuccessResponse = {
        error: undefined,
        userEmail: (response as SuccessResponse).userEmail,
        userId: (response as SuccessResponse).userId,
        userAvatar: (response as SuccessResponse).userAvatar,
        userFirstName: (response as SuccessResponse).userFirstName,
        userLastName: (response as SuccessResponse).userLastName,
        authProvider: (response as SuccessResponse).authProvider,
        hintPassed: (response as SuccessResponse).hintPassed,
      };

      return successResp;
    } catch (err) {
      return { error: (err as Error).message || "Unknown error" };
    }
  };

  /**
   * Initiates Google OAuth login
   */
  loginGoogle = async () => {
    window.location.href = environment.googleAuthUrl;
  };

  /**
   * Logs out the current user
   */
  logoutUser = async () => {
    try {
      await this._logoutProvider.postData<RequestLogoutType>({
        userId: this._userStore.userId,
      });

      this._userStore.logout();
      this.resetLoginData();
    } catch (error) {
      console.error("Logout request failed:", error);
    }
  };

  // Getters and setters
  get email() {
    return this._email;
  }

  get password() {
    return this._password;
  }

  setEmail = (email: string) => {
    this._email = email;
  };

  setPassword = (password: string) => {
    this._password = password;
  };

  /**
   * Resets login data after successful login or logout
   */
  resetLoginData() {
    this._email = "";
    this._password = "";
  }

  /**
   * Validates email format
   * @returns boolean indicating if email is valid
   */
  isValidEmail = (): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(this._email);
  };

  /**
   * Checks if all fields are valid
   * @returns An error message if fields are invalid, empty string otherwise
   */
  checkFields = (): string => {
    if (!this.isValidEmail()) {
      return "error.invalidEmail";
    }
    return "";
  };
}
