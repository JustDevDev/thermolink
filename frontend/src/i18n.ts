import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import HttpApi from "i18next-http-backend";
import { InitOptions } from "i18next";

const initI18n = async () => {
  const options: InitOptions = {
    fallbackLng: "en",
    debug: true,
    interpolation: {
      escapeValue: false,
    },
    backend: {
      loadPath: "/locales/{{lng}}.json",
    },
    // React specific options
    react: {
      useSuspense: true,
    },
  };

  await i18n
    .use(initReactI18next)
    .use(LanguageDetector)
    .use(HttpApi)
    .init(options);

  return i18n;
};

export default initI18n;
