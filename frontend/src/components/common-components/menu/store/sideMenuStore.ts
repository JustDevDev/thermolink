import { makeAutoObservable } from "mobx";

/**
 * SideMenuStore class
 * Manages the state of various menu and dialog components
 */
export default class SideMenuStore {
  private menuOpen = false;
  private menuOptionOpen = false;
  private settingDialogOpen = false;
  private helpDialogOpen = false;
  private aboutUsDialogOpen = false;

  constructor() {
    makeAutoObservable(this);
  }

  // Getters
  get isMenuOpen() {
    return this.menuOpen;
  }

  get isMenuOptionOpen() {
    return this.menuOptionOpen;
  }

  get isSettingDialogOpen() {
    return this.settingDialogOpen;
  }

  get isHelpDialogOpen() {
    return this.helpDialogOpen;
  }

  get isAboutUsDialogOpen() {
    return this.aboutUsDialogOpen;
  }

  // Setters
  setMenuOpen = (value: boolean) => {
    this.menuOpen = value;
  };

  setMenuOptionOpen = (value: boolean) => {
    this.menuOptionOpen = value;
  };

  setSettingDialogOpen = (value: boolean) => {
    this.settingDialogOpen = value;
  };

  setHelpDialogOpen = (value: boolean) => {
    this.helpDialogOpen = value;
  };

  setAboutUsDialogOpen = (value: boolean) => {
    this.aboutUsDialogOpen = value;
  };
}
