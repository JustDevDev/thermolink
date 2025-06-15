import { useUserStore } from "@/hooks/stores/useUserStore";
import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";

const BasicView = observer(
  ({
    children,
    maxWidth,
  }: {
    children: React.ReactNode;
    maxWidth?: string | false;
  }) => {
    const userStore = useUserStore();
    const finalMaxWidth = maxWidth ?? userStore.basicViewDefaultMaxWidth;

    return (
      <Box
        sx={{
          width: "100%",
          height: "100%",
          maxWidth: finalMaxWidth || "none",
          display: "flex",
          flexDirection: "column",
        }}
      >
        {children}
      </Box>
    );
  }
);

export default BasicView;
