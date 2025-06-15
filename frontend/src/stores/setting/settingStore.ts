import { action, makeObservable, observable } from "mobx";
import { CacheStore, StorableValue } from "../cache/cacheStore";
import i18n from "i18next";

export enum sizeElements {
  SMALL = "small",
  MEDIUM = "medium",
}

export enum ColorTheme {
  LIGHT = "light",
  DARK = "dark",
}

export enum Language {
  EN = "en",
  CS = "cs",
}

const LANGUAGE_STORAGE_KEY = "i18nextLng";

export class SettingStore {
  sizeElement: string = "small";
  theme: string = ColorTheme.LIGHT;
  language: string = Language.EN;
  showResponsiveMenu: boolean = false;
  basicViewDefaultMaxWidth: string | false = "1700px";

  private _cacheStore: CacheStore;
  private _i18nInitialized: boolean = false;

  constructor() {
    makeObservable(this, {
      sizeElement: observable,
      theme: observable,
      language: observable,
      showResponsiveMenu: observable,
      setResponsiveMenu: action,
      toggleTheme: action,
      setLanguage: action,
      basicViewDefaultMaxWidth: observable,
      setBasicViewDefaultMaxWidth: action,
    });

    this._cacheStore = new CacheStore();

    this._loadTheme();
    this._loadLanguageWithoutI18n();
  }

  toggleTheme = () => {
    this.setTheme =
      this.theme === ColorTheme.LIGHT ? ColorTheme.DARK : ColorTheme.LIGHT;
  };

  set setTheme(theme: string) {
    this.theme = theme;
    this.saveToCache("theme", theme);
  }

  get getTheme() {
    return this.theme;
  }

  setLanguage = (lang: string) => {
    this.language = lang;

    // Změníme jazyk v i18next pouze pokud je inicializován
    if (i18n.isInitialized) {
      i18n.changeLanguage(lang).catch((error) => {
        console.error("Error changing language:", error);
      });
      // i18next si samo ukládá jazyk do localStorage, takže nemusíme volat saveToCache
    } else {
      // Pokud i18next není inicializován, uložíme jazyk do localStorage sami
      this.saveToCache(LANGUAGE_STORAGE_KEY, lang);
    }
  };

  // Metoda, která bude volána po inicializaci i18next
  initializeI18n = () => {
    this._i18nInitialized = true;
    // Nyní můžeme bezpečně nastavit jazyk v i18next
    if (i18n.isInitialized) {
      i18n.changeLanguage(this.language).catch((error) => {
        console.error("Error initializing language:", error);
      });
    }
  };

  get getLanguage() {
    return this.language;
  }

  // Load from cache
  loadFromCache(key: string): StorableValue {
    return this._cacheStore.loadFromLocalStorage(key);
  }

  // Save to cache
  saveToCache(key: string, value: StorableValue): void {
    this._cacheStore.saveToLocalStorage(key, value);
  }

  // Remove from cache
  removeFromCache(key: string): void {
    localStorage.removeItem(key);
  }

  // Load theme from cache
  private _loadTheme(): void {
    const theme = this.loadFromCache("theme");
    if (theme && typeof theme === "string") {
      this.setTheme = theme;
    } else {
      this._loadThemeFromOS();
    }
  }

  // Load language from cache without changing i18n
  private _loadLanguageWithoutI18n(): void {
    // Nejprve zkusíme načíst z i18next klíče
    const language = this.loadFromCache(LANGUAGE_STORAGE_KEY);
    if (language && typeof language === "string") {
      this.language = language;
    } else {
      // Zkusíme načíst ze starého klíče pro zpětnou kompatibilitu
      const oldLanguage = this.loadFromCache("language");
      if (oldLanguage && typeof oldLanguage === "string") {
        this.language = oldLanguage;
        // Přesuneme hodnotu do nového klíče
        this.saveToCache(LANGUAGE_STORAGE_KEY, oldLanguage);
        // Odstraníme starý klíč
        this.removeFromCache("language");
      } else {
        this._loadLanguageFromBrowserWithoutI18n();
      }
    }
  }

  // Load theme from OS
  private _loadThemeFromOS(): void {
    const darkModeMediaQuery = window.matchMedia(
      "(prefers-color-scheme: dark)"
    );
    this.setTheme = darkModeMediaQuery.matches
      ? ColorTheme.DARK
      : ColorTheme.LIGHT;
  }

  // Load language from browser without changing i18n
  private _loadLanguageFromBrowserWithoutI18n(): void {
    const browserLang = navigator.language.split("-")[0];
    this.language = browserLang === "cs" ? Language.CS : Language.EN;
  }

  setResponsiveMenu = (value: boolean): void => {
    this.showResponsiveMenu = value;
  };

  setBasicViewDefaultMaxWidth = () => {
    this.basicViewDefaultMaxWidth =
      this.basicViewDefaultMaxWidth === false ? "1700px" : false;
  };
}
