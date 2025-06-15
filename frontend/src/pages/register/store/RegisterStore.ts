import { makeAutoObservable } from "mobx";
import { RegisterProvider } from "./data/registerProvider";
import { ResponseRegisterType } from "@/types/api/responseTypes";
import { RequestRegisterType } from "@/types/api/requestTypes";
import {
  checkPasswordLength,
  containsSpecialCharacter,
} from "../RegisterUtils";

export enum RegisterEnums {
  passwordLength = 8,
}

/**
 * Store for handling user registration logic.
 * It includes state management, field validation and registration process.
 */
export class RegisterStore {
  constructor() {
    makeAutoObservable(this);
    this.registerProvider = new RegisterProvider();
  }

  // Private fields for user registration details.
  private _email: string = "";
  private _password: string = "";
  private _repeatPassword: string = "";

  // Provider for handling API registration requests.
  registerProvider: RegisterProvider;
  /**
   * Updates the user's email.
   * @param value - The new email address.
   */
  setEmail = (value: string) => {
    this._email = value;
  };

  /**
   * Updates the user's password.
   * @param value - The new password.
   */
  setPassword = (value: string) => {
    this._password = value;
  };

  /**
   * Updates the user's repeated password.
   * @param value - The repeated password.
   */
  setRepeatPassword = (value: string) => {
    this._repeatPassword = value;
  };

  /**
   * Retrieves the current email from the store.
   * @returns The user's email.
   */
  get email(): string {
    return this._email;
  }

  /**
   * Retrieves the current password from the store.
   * @returns The user's password.
   */
  get password(): string {
    return this._password;
  }

  /**
   * Retrieves the current repeated password from the store.
   * @returns The repeated password.
   */
  get repeatPassword(): string {
    return this._repeatPassword;
  }

  /**
   * Compares the password and repeat password fields.
   * @returns True if both passwords match, otherwise false.
   */
  checkPasswords = () => {
    return this._password === this._repeatPassword;
  };

  /**
   * Performs the registration process by sending user data to the backend.
   * @returns A promise that resolves with the server response.
   */
  register = async (): Promise<ResponseRegisterType> => {
    try {
      await this.registerProvider.postData<RequestRegisterType>({
        email: this._email,
        password: this._password,
      });

      this.resetRegisterData();
      return this.registerProvider.data as ResponseRegisterType;
    } catch (err) {
      return { error: (err as Error).message || "Unknown error" };
    }
  };

  /**
   * Resets the registration form fields.
   */
  resetRegisterData = () => {
    this._email = "";
    this._password = "";
    this._repeatPassword = "";
  };

  /**
   * Validates the email format.
   * @returns True if the email format is valid, otherwise false.
   */
  isValidEmail = (): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(this._email);
  };

  /**
   * Checks the validity of the registration form fields.
   * It verifies password match, password length, special character inclusion, and email format.
   * @returns An empty string if all validations pass, otherwise a specific error code.
   */
  checkFields = (): string => {
    if (!this.checkPasswords()) {
      return "error.passwordsDoNotMatch";
    }
    if (!checkPasswordLength(this._password)) {
      return "error.passwordLength";
    }
    if (!containsSpecialCharacter(this._password)) {
      return "error.passwordSpecialCharacter";
    }
    if (!this.isValidEmail()) {
      return "error.invalidEmail";
    }
    return "";
  };
}
