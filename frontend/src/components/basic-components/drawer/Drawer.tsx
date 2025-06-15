import { useUserStore } from "@/hooks/stores/useUserStore";
import { gray } from "@/utils/theme/themePrimitives";
import {
  Drawer as MuiDrawer,
  SxProps,
  Theme,
  DrawerProps as MuiDrawerProps,
  Box,
  drawerClasses,
} from "@mui/material";
import { observer } from "mobx-react-lite";

interface DrawerProps {
  children: React.ReactNode;
  open?: boolean;
  anchor?: "top" | "left" | "bottom" | "right";
  onClose?: () => void;
  sx?: SxProps<Theme>;
  PaperProps?: Partial<MuiDrawerProps["PaperProps"]>;
  variant?: "permanent" | "persistent" | "temporary";
}

export const drawerWidth = 210;

const Drawer = observer(
  ({
    children,
    open,
    anchor = "left",
    onClose,
    sx,
    PaperProps,
    variant,
  }: DrawerProps) => {
    const userStore = useUserStore();

    return (
      <MuiDrawer
        open={open}
        anchor={anchor}
        onClose={onClose}
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          boxSizing: "border-box",
          mt: 10,
          [`& .${drawerClasses.paper}`]: {
            width: drawerWidth,
            boxSizing: "border-box",
            border: "none",
            borderRight: `1px solid ${
              userStore.getTheme === "light" ? gray[100] : gray[700]
            }`,
            borderRadius: 0,
            backgroundColor:
              userStore.getTheme === "light" ? gray[50] : gray[900],
          },
          ...sx,
        }}
        PaperProps={PaperProps}
        variant={variant}
      >
        <Box className="flex justify-end p-2"></Box>
        {children}
      </MuiDrawer>
    );
  }
);

export default Drawer;
