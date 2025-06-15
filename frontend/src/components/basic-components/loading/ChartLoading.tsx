import { Box } from "@mui/material";
import { keyframes } from "@mui/system";
import { brand, gray } from "@/utils/theme/themePrimitives";
import ShareIcon from "@mui/icons-material/Share";
import { useUserStore } from "@/hooks/stores/useUserStore";

const spin = keyframes`
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
`;

const ChartLoading = () => {
  const userStore = useUserStore();

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100%",
        width: "100%",
        position: "absolute",
        top: 0,
        left: 0,
        backgroundColor: userStore.getTheme === "light" ? "white" : gray[800],
        zIndex: 10,
      }}
    >
      <Box
        sx={{
          animation: `${spin} 4s infinite linear`,
        }}
      >
        <ShareIcon sx={{ fontSize: "3rem", color: brand[500] }} />
      </Box>
    </Box>
  );
};

export default ChartLoading;
