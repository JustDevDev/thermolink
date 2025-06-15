import { useUserStore } from "@/hooks/stores/useUserStore";
import MenuIcon from "@mui/icons-material/Menu";
import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";
import { useSideMenuStore } from "./hooks/useSideMenuStore";
import IconButton from "@/components/basic-components/button/IconButton";

const HeaderUserMenu = observer(() => {
  const store = useUserStore();
  const sideMenuStore = useSideMenuStore();

  if (!store.isLoaded && !store.isLoggedIn) {
    return null;
  }

  return (
    <Box
      className="gap-4 items-center"
      sx={{ display: { xs: "flex", md: "none" } }}
    >
      <IconButton
        onClick={() => sideMenuStore.setMenuOpen(!sideMenuStore.isMenuOpen)}
      >
        <MenuIcon sx={{ fontSize: "17px" }} />
      </IconButton>
    </Box>
  );
});

export default HeaderUserMenu;
