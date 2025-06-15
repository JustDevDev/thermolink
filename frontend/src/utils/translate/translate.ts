import i18next from "i18next";
import { useTranslation } from "react-i18next";

/**
 * Translate function for use outside of React components.
 * Note: This will NOT cause re-renders when language changes.
 * Use useAppTranslation() hook inside React components instead.
 *
 * @param key - key for translation.
 * @param options - options for translation.
 * @returns {string} - translated string.
 */
export function translate(key: string, options = {}): string {
  return i18next.t(key, options);
}

/**
 * Hook for using translations in React components.
 * This WILL cause components to re-render when language changes.
 * Always use this inside React components instead of the translate() function.
 *
 * @returns The translation functions and i18n instance.
 */
export function useAppTranslation() {
  return useTranslation();
}
