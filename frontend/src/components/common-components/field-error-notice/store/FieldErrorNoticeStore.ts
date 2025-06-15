import { makeAutoObservable } from "mobx";

export class FieldErrorNoticeStore {
  constructor() {
    makeAutoObservable(this);
  }

  collapse: boolean = false;
  requiredValues = new Map<string, string | number | null | undefined>();
  errorMessage: string = "";

  setCollapse = (collapse: boolean, message?: string) => {
    this.setErrorMessage(message || "");
    this.collapse = collapse;
  };

  setErrorMessage = (message: string) => {
    this.errorMessage = message;
  };

  addRequiredValue = (
    key: string,
    value: string | number | null | undefined
  ) => {
    this.requiredValues.set(key, value);
  };

  clearRequiredValues = () => {
    this.requiredValues.clear();
  };

  getRequiredValues = () => {
    return this.requiredValues;
  };

  deleteRequiredValue = (key: string) => {
    this.requiredValues.delete(key);
  };

  checkRequiredValues = (): boolean => {
    for (const value of this.getRequiredValues().values()) {
      if (value === "" || value === null || value === undefined) {
        this.setCollapse(true);
        return false;
      }
    }
    return true;
  };
}
