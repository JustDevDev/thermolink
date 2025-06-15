import { rootStore } from "@/stores/root/rootStore";

export const useUserStore = () => {
  return rootStore.userStore;
};
