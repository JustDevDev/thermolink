import ContainerMenu from "@/components/basic-components/container-menu/ContainerMenu";
import LogoutIcon from "@mui/icons-material/Logout";
import { Box } from "@mui/material";
import { useSideMenuStore } from "./hooks/useSideMenuStore";
import Avatar from "@/components/basic-components/avatar/Avatar";
import { useLoginStore } from "@/pages/login/hooks/useLoginStore";

const OptionMenu = () => {
  const loginStore = useLoginStore();
  const sideMenuStore = useSideMenuStore();

  return (
    <Box
      sx={{
        padding: 2,
        display: "flex",
        gap: 2,
        alignItems: "center",
        justifyContent: "space-between",
      }}
    >
      <Avatar />
      <ContainerMenu
        menuItems={[
          {
            title: "menu.settings",
            onClick: () => {
              sideMenuStore.setSettingDialogOpen(true);
              sideMenuStore.setMenuOptionOpen(false);
            },
          },
          {
            title: "menu.help",
            onClick: () => {
              sideMenuStore.setHelpDialogOpen(true);
              sideMenuStore.setMenuOptionOpen(false);
            },
          },
          {
            listItemText: "menu.logout",
            iconPosition: "end",
            listItemIcon: <LogoutIcon sx={{ fontSize: "18px" }} />,
            onClick: () => {
              loginStore.logoutUser();
              sideMenuStore.setMenuOptionOpen(false);
            },
            divider: true,
          },
        ]}
      />
    </Box>
  );
};

export default OptionMenu;
