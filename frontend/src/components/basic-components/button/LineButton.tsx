import { Typography } from "../typography/Typography";
import { useUserStore } from "../../../hooks/stores/useUserStore";
import { ColorTheme } from "../../../stores/setting/settingStore";
import { Box, SxProps, Theme } from "@mui/material";
import { gray } from "@/utils/theme/themePrimitives";

type LineButtonProps = {
  intld: string;
  className?: string;
  sx?: SxProps<Theme>;
  onClick?: () => void;
};

const LineButton = ({ intld, className, sx, onClick }: LineButtonProps) => {
  const userStore = useUserStore();

  return (
    <Box onClick={onClick}>
      <Typography
        type={"body2"}
        className={className}
        sx={{
          cursor: "pointer",
          fontWeight: "600",
          transition: "color 0.2s ease",
          "&:hover": {
            "&:after": {
              width: "0%",
            },
          },
          "&:after": {
            content: '""',
            display: "block",
            width: "100%",
            height: "0.5px",
            background:
              userStore.theme === ColorTheme.LIGHT ? gray[400] : gray[700],
            transition: "width 0.3s",
          },
          ...sx,
        }}
        intld={intld}
      />
    </Box>
  );
};

export default LineButton;
