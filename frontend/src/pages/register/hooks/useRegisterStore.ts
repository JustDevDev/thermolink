import { rootStore } from "../../../stores/root/rootStore";

export const useRegisterStore = () => {
  return rootStore.registerStore;
};
