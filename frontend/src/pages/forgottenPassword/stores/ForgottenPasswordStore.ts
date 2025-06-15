import { makeAutoObservable } from "mobx";
import { ForgottenPasswordProvider } from "./data/ForgottenPasswordProvider";
import {
  ResponseForgottenPasswordNewType,
  ResponseForgottenPasswordType,
} from "@/types/api/responseTypes";
import { ForgottenPasswordValidateProvider } from "./data/ForgottenPasswordValidateProvider";
import { ForgottenPasswordNewProvider } from "./data/ForgottenPasswordNewProvider";
import {
  checkPasswordLength,
  containsSpecialCharacter,
} from "@/pages/register/RegisterUtils";
import {
  RequestForgottenPasswordNewType,
  RequestForgottenPasswordType,
} from "@/types/api/requestTypes";

export default class ForgottenPasswordStore {
  constructor() {
    makeAutoObservable(this);

    this.forgottenPasswordProvider = new ForgottenPasswordProvider();
  }

  readonly forgottenPasswordProvider: ForgottenPasswordProvider;
  readonly forgottenPasswordValidateProvider =
    new ForgottenPasswordValidateProvider();
  readonly forgottenPasswordNewProvider = new ForgottenPasswordNewProvider();

  private _newPassword: string = "";
  private _repeatPassword: string = "";
  private _email: string = "";

  get newPassword() {
    return this._newPassword;
  }

  set newPassword(value: string) {
    this._newPassword = value;
  }

  get repeatPassword() {
    return this._repeatPassword;
  }

  set repeatPassword(value: string) {
    this._repeatPassword = value;
  }

  get email() {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  resetData = () => {
    this._email = "";
    this._newPassword = "";
    this._repeatPassword = "";
  };

  /**
   * Validates the email format using a regular expression.
   * This method ensures that the email adheres to a basic format: username@domain.tld
   *
   * @returns {boolean} True if the email format is valid, otherwise false.
   */
  isValidEmail = (): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(this.email);
  };

  /**
   * Checks the validity of the email and returns an error message if invalid.
   *
   * @returns {string} An error message if the email is invalid, otherwise an empty string.
   */
  checkEmail = (): string => {
    if (!this.isValidEmail()) {
      return "error.invalidEmail";
    }
    return "";
  };

  /**
   * Compares the new password and repeat password fields.
   * This method is crucial for ensuring that the user has correctly entered their intended password.
   *
   * @returns {boolean} True if both passwords match, otherwise false.
   */
  checkPasswords = () => {
    return this._newPassword === this._repeatPassword;
  };

  /**
   * Performs comprehensive password validation.
   * This method checks for password match, length, and the presence of special characters.
   * It's a critical security feature to ensure strong password policies.
   *
   * @returns {string} An error message if any validation fails, otherwise an empty string.
   */
  checkPassword = (): string => {
    if (!this.checkPasswords()) {
      return "error.passwordsDoNotMatch";
    }
    if (!checkPasswordLength(this._newPassword)) {
      return "error.passwordLength";
    }
    if (!containsSpecialCharacter(this._newPassword)) {
      return "error.passwordSpecialCharacter";
    }
    return "";
  };

  /**
   * Initiates the password reset process by sending a reset request to the server.
   * This method triggers the email containing the reset token to be sent to the user.
   *
   * @returns {Promise<ResponseForgottenPasswordType>} A promise that resolves with the server response.
   */
  requestPasswordReset = async (): Promise<ResponseForgottenPasswordType> => {
    await this.forgottenPasswordProvider.postData<RequestForgottenPasswordType>(
      { email: this.email }
    );

    return this.forgottenPasswordProvider.data;
  };

  /**
   * Validates the reset token received by the user.
   * This method is a crucial security step to ensure that the reset request is legitimate and hasn't expired.
   *
   * @param {string} token - The reset token received by the user.
   * @returns {Promise<ResponseForgottenPasswordType>} A promise that resolves with the token validation response.
   */
  requestValidateToken = async (token: string) => {
    this.forgottenPasswordValidateProvider.token = token;

    await this.forgottenPasswordValidateProvider.getData();
  };

  /**
   * Submits the new password to the server to complete the password reset process.
   * This method should only be called after successful token validation.
   *
   * @returns {Promise<any>} A promise that resolves with the server response to the password update.
   */
  requestNewPassword = async (
    token: string
  ): Promise<ResponseForgottenPasswordNewType> => {
    await this.forgottenPasswordNewProvider.postData<RequestForgottenPasswordNewType>(
      {
        password: this._newPassword,
        resetPasswordToken: token,
      }
    );

    return this.forgottenPasswordNewProvider.data;
  };
}
