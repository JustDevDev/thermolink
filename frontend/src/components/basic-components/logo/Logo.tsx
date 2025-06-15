import { Box, SxProps, Theme } from "@mui/material";
import Title from "../typography/Title";
import { observer } from "mobx-react-lite";
import { brand } from "@/utils/theme/themePrimitives";
import ShareIcon from "@mui/icons-material/Share";

interface LogoProps {
  width: string;
  className?: string;
  sx?: SxProps<Theme>;
}

export const Logo = observer((props: LogoProps) => {
  const { sx } = props;
  return (
    <Box sx={{ gap: "0.3rem", alignItems: "center", display: "flex", ...sx }}>
      <ShareIcon sx={{ fontSize: "2rem", color: brand[500] }} />
      <Title variant="h5" sx={{ fontWeight: "bold" }} intld="projectTitle" />
    </Box>
  );
});
