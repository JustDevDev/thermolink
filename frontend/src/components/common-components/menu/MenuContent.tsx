import React from "react";
import {
  Box,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Stack,
  SvgIconProps,
} from "@mui/material";
import SettingsIcon from "@mui/icons-material/Settings";
import HelpIcon from "@mui/icons-material/Help";
import InfoIcon from "@mui/icons-material/Info";
import { useLocation, useNavigate } from "react-router-dom";
import { appPaths } from "@/routers/app/appPath";
import { useSideMenuStore } from "./hooks/useSideMenuStore";
import { useAppTranslation } from "@/utils/translate/translate";
import { mainMenuItems } from "./MainMenu";

type MenuItemProps = {
  title: string;
  Icon: React.ComponentType<SvgIconProps>;
  onClick: () => void;
  selected?: boolean;
};

/**
 * Single menu item with icon and text.
 */
const MenuItem: React.FC<MenuItemProps> = ({
  title,
  Icon,
  onClick,
  selected = false,
}) => {
  const { t } = useAppTranslation();

  return (
    <ListItem disablePadding>
      <ListItemButton onClick={onClick} selected={selected}>
        <ListItemIcon>
          <Icon
            sx={(theme) => ({
              fontSize: 17,
              marginRight: 1.25,
              color: selected
                ? theme.palette.text.primary
                : theme.palette.text.secondary,
            })}
          />
        </ListItemIcon>
        <ListItemText primary={t(`menu.${title}`)} />
      </ListItemButton>
    </ListItem>
  );
};

/**
 * Side menu content with primary and secondary items.
 */
const MenuContent: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const sideMenuStore = useSideMenuStore();

  // Determine current admin subpath
  const adminBase = appPaths.ADMIN.replace(/^\//, "");
  const pathParts = location.pathname.split("/").filter(Boolean);
  const currentAdminPath = pathParts[0] === adminBase ? pathParts[1] || "" : "";

  // Secondary items defined inline to capture sideMenuStore
  const secondaryMenuItems = [
    {
      title: "settings",
      Icon: SettingsIcon,
      onClick: () => {
        sideMenuStore.setSettingDialogOpen(true);
        sideMenuStore.setMenuOpen(false);
      },
    },
    {
      title: "help",
      Icon: HelpIcon,
      onClick: () => {
        sideMenuStore.setHelpDialogOpen(true);
        sideMenuStore.setMenuOpen(false);
      },
    },
    {
      title: "aboutUs",
      Icon: InfoIcon,
      onClick: () => {
        sideMenuStore.setAboutUsDialogOpen(true);
        sideMenuStore.setMenuOpen(false);
      },
    },
  ] as {
    title: string;
    Icon: React.ComponentType<SvgIconProps>;
    onClick: () => void;
  }[];

  return (
    <Stack sx={{ flexGrow: 1, p: 1, justifyContent: "space-between" }}>
      <List dense>
        {mainMenuItems.map((item, index) => (
          <MenuItem
            key={index}
            title={item.title}
            Icon={item.Icon}
            selected={currentAdminPath === item.path}
            onClick={() => {
              sideMenuStore.setMenuOpen(false);
              navigate(`${appPaths.ADMIN}/${item.path}`);
            }}
          />
        ))}
      </List>
      <Box>
        <List dense>
          {secondaryMenuItems.map((item, index) => (
            <MenuItem
              key={index}
              title={item.title}
              Icon={item.Icon}
              onClick={item.onClick}
            />
          ))}
        </List>
      </Box>
    </Stack>
  );
};

export default MenuContent;
