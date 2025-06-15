import { makeAutoObservable } from "mobx";
import { VerifyAccountProvider } from "./data/VerifyAccountProvider";
import { ResponseVerifyAccountType } from "@/types/api/responseTypes";

export default class VerifyAccountStore {
  constructor() {
    makeAutoObservable(this);
  }

  /**
   * Instance of VerifyAccountProvider used for API interactions
   */
  readonly verifyAccountProvider = new VerifyAccountProvider();

  /**
   * Attempts to verify an account using the provided token
   *
   * @param token - The verification token received by the user
   * @returns A promise resolving to a ResponseVerifyAccountType object
   */
  verifyAccount = async (token: string): Promise<ResponseVerifyAccountType> => {
    this.verifyAccountProvider.token = token;

    try {
      await this.verifyAccountProvider.getData();
      return { error: "" };
    } catch (error) {
      return { error: (error as Error).message || "Unknown error" };
    }
  };
}
