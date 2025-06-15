import Drawer from "@/components/basic-components/drawer/Drawer";
import { Box, Divider } from "@mui/material";
import { observer } from "mobx-react-lite";
import MenuContent from "../MenuContent";
import { useSideMenuStore } from "../hooks/useSideMenuStore";
import Button from "@/components/basic-components/button/Button";
import { useUserStore } from "@/hooks/stores/useUserStore";
import LogoutIcon from "@mui/icons-material/Logout";
import { gray } from "@/utils/theme/themePrimitives";
import Avatar from "@/components/basic-components/avatar/Avatar";
import GithubBox from "@/components/basic-components/box/GithubBox";
import { useLoginStore } from "@/pages/login/hooks/useLoginStore";

const ResponsiveSideMenu = observer(() => {
  const sideMenuStore = useSideMenuStore();
  const userStore = useUserStore();
  const loginStore = useLoginStore();

  const isLightTheme = userStore.theme === "light";

  const handleDrawerClose = () => {
    sideMenuStore.setMenuOpen(false);
  };

  const handleLogout = () => {
    loginStore.logoutUser();
    sideMenuStore.setMenuOpen(false);
  };

  return (
    <Drawer
      anchor="left"
      open={sideMenuStore.isMenuOpen}
      onClose={handleDrawerClose}
    >
      <Box sx={{ padding: 1 }}>
        <Avatar />
      </Box>
      <Divider />
      <MenuContent />
      <GithubBox />
      <Divider />
      <Box sx={{ padding: 1 }}>
        <Button
          onClick={handleLogout}
          intld="menu.logout"
          fullwidth
          startIcon={<LogoutIcon sx={{ fontSize: "18px" }} />}
          variant="outlined"
          sx={{
            border: `1px solid ${isLightTheme ? gray[100] : gray[600]}`,
            "&:hover": {
              borderColor: isLightTheme ? gray[400] : gray[600],
            },
          }}
        />
      </Box>
    </Drawer>
  );
});

export default ResponsiveSideMenu;
