import { createContext, useContext } from "react";
import { RootStore } from "../rootStore";

export const StoreContext = createContext<RootStore | null>(null);

export const useStores = () => {
  const context = useContext(StoreContext);
  if (!context) {
    throw new Error("useStores must be used within a StoreProvider.");
  }
  return context;
};
