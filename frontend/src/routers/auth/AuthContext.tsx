import React, { useEffect } from "react";
import { rootStore } from "../../stores/root/rootStore";
import { AuthContext } from "./authHooks";

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const userStore = rootStore.userStore;

  useEffect(() => {
    if (!userStore.isLoaded) {
      userStore.checkIsLoaded();
    }
  }, [userStore.isLoaded, userStore]);

  return (
    <AuthContext.Provider value={userStore}>{children}</AuthContext.Provider>
  );
};
