import { rootStore } from "../../../stores/root/rootStore";

export const useLoginStore = () => {
  return rootStore.loginStore;
};
