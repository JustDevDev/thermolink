interface Env {
  VITE_API_URL: string;
  VITE_WS_URL: string;
  VITE_GOOGLE_AUTH_URL: string;
}

declare global {
  interface Window {
    __APP_ENV__?: Partial<Env>;
  }
}

const winEnv = window.__APP_ENV__;

const metaEnv = import.meta.env as Env;

const APP_ENV: Env = {
  VITE_API_URL: winEnv?.VITE_API_URL || metaEnv.VITE_API_URL,
  VITE_WS_URL: winEnv?.VITE_WS_URL || metaEnv.VITE_WS_URL,
  VITE_GOOGLE_AUTH_URL:
    winEnv?.VITE_GOOGLE_AUTH_URL || metaEnv.VITE_GOOGLE_AUTH_URL,
};

export const environment = {
  apiUrl: APP_ENV.VITE_API_URL,
  wsUrl: APP_ENV.VITE_WS_URL,
  googleAuthUrl: APP_ENV.VITE_GOOGLE_AUTH_URL,
} as const;
