import { createContext, useContext } from "react";
import { rootStore } from "../../stores/root/rootStore";

export const AuthContext = createContext(rootStore.userStore);

export const useAuth = () => useContext(AuthContext);
