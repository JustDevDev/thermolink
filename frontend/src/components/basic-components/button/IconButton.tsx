import {
  Box,
  IconButton as MuiIconButton,
  SxProps,
  Theme,
} from "@mui/material";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { ColorTheme } from "@/stores/setting/settingStore";
import { gray } from "@/utils/theme/themePrimitives";
import { observer } from "mobx-react-lite";

type IconButtonProps = {
  onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
  color?:
    | "inherit"
    | "default"
    | "primary"
    | "secondary"
    | "error"
    | "info"
    | "success"
    | "warning";
  children?: React.ReactNode;
  sx?: SxProps<Theme>;
};
const IconButton = observer((props: IconButtonProps) => {
  const { color = "primary", onClick, children, sx } = props;
  const userStore = useUserStore();
  const isLight = userStore.getTheme === ColorTheme.LIGHT;

  return (
    <Box
      data-screenshot="toggle-mode"
      sx={{
        verticalAlign: "bottom",
        display: "inline-flex",
        border: `1px solid ${isLight ? gray[100] : gray[700]}`,
        borderRadius: 1,
        ...sx,
      }}
    >
      <MuiIconButton color={color} onClick={(event) => onClick(event)}>
        {children}
      </MuiIconButton>
    </Box>
  );
});

export default IconButton;
