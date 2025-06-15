import React from "react";
import { rootStore } from "../rootStore";
import { StoreContext } from "../hooks/rootStoreHooks";

export const RootStoreProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  return (
    <StoreContext.Provider value={rootStore}>{children}</StoreContext.Provider>
  );
};
