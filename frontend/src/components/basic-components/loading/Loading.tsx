import { Box } from "@mui/material";
import { keyframes } from "@mui/system";
import { brand } from "@/utils/theme/themePrimitives";
import ShareIcon from "@mui/icons-material/Share";

const spin = keyframes`
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
`;

const Loading = () => {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "93dvh",
        width: "100%",
      }}
    >
      <Box
        sx={{
          animation: `${spin} 4s infinite linear`,
        }}
      >
        <ShareIcon sx={{ fontSize: "10rem", color: brand[500] }} />
      </Box>
    </Box>
  );
};

export default Loading;
