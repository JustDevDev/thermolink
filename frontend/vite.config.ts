// vite.config.ts
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";

export default defineConfig({
  plugins: [react({ jsxRuntime: "automatic" })],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
      // explicitně namapujeme JSX runtime na ESM soubor
      "react/jsx-runtime": path.resolve(
        __dirname,
        "node_modules/react/jsx-runtime.js"
      ),
      "react/jsx-dev-runtime": path.resolve(
        __dirname,
        "node_modules/react/jsx-dev-runtime.js"
      ),
    },
    dedupe: ["react", "react-dom"],
  },
  optimizeDeps: {
    include: ["mobx-react-lite"],
  },
  server: { port: 3001 },
  build: {
    commonjsOptions: {
      transformMixedEsModules: true,
    },
  },
});
