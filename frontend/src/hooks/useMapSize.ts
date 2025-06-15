import { sizeElements } from "@/stores/setting/settingStore";
import { useUserStore } from "@/hooks/stores/useUserStore";

type MuiSize = "small" | "medium";

export const useMapSize = () => {
  const userStore = useUserStore();

  const mapSizeToMuiSize = (size: string): MuiSize => {
    switch (size) {
      case sizeElements.SMALL:
        return "small";
      case sizeElements.MEDIUM:
        return "medium";
      default:
        return "medium";
    }
  };

  const size = mapSizeToMuiSize(userStore.sizeElement);

  return { size, mapSizeToMuiSize };
};
