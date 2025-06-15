import { Divider } from "@mui/material";
import MenuContent from "./MenuContent";
import { Logo } from "@/components/basic-components/logo/Logo";
import OptionMenu from "./OptionMenu";
import Drawer from "@/components/basic-components/drawer/Drawer";
import GithubBox from "@/components/basic-components/box/GithubBox";

export const drawerWidth = 210;

const SideMenu = () => {
  return (
    <Drawer
      anchor="left"
      variant="permanent"
      sx={{
        display: { xs: "none", md: "block" },
      }}
    >
      <Logo
        width={"40px"}
        sx={{
          marginY: "0.5rem",
          justifyContent: "center",
        }}
      />
      <Divider />
      <MenuContent />
      <GithubBox />
      <Divider />
      <OptionMenu />
    </Drawer>
  );
};

export default SideMenu;
