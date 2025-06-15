import { Suspense } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.tsx";
import { RootStoreProvider } from "./stores/root/data/RootStoreProvider";
import { AuthProvider } from "./routers/auth/AuthContext";
import { BrowserRouter } from "react-router-dom";
import Loading from "@/components/basic-components/loading/Loading";
import initI18n from "./i18n";
import "./index.css";
import { rootStore } from "./stores/root/rootStore";

initI18n().then(() => {
  rootStore.userStore.initializeI18n();

  createRoot(document.getElementById("root")!).render(
    <>
      <Suspense fallback={<Loading />}>
        <RootStoreProvider>
          <BrowserRouter>
            <AuthProvider>
              <App />
            </AuthProvider>
          </BrowserRouter>
        </RootStoreProvider>
      </Suspense>
    </>
  );
});
