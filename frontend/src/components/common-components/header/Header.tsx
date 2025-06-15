import { Box } from "@mui/material";
import { useUserStore } from "../../../hooks/stores/useUserStore";
import Brightness4Icon from "@mui/icons-material/Brightness4";
import Brightness5Icon from "@mui/icons-material/Brightness5";
import { ColorTheme } from "../../../stores/setting/settingStore";
import { observer } from "mobx-react-lite";
import HeaderUserMenu from "../menu/HeaderUserMenu";
import IconButton from "@/components/basic-components/button/IconButton";
import { Logo } from "@/components/basic-components/logo/Logo";
import LanguageSelector from "../language-selector/LanguageSelector";

const Header = observer(() => {
  const store = useUserStore();

  return (
    <>
      <Box
        sx={{
          display: "flex",
          gap: "10px",
          justifyContent: { xs: "space-between", sm: "right" },
          alignItems: "center",
          width: "100%",
          paddingX: "10px",
          paddingY: "5px",
        }}
      >
        <Logo
          width={"40px"}
          sx={{
            marginY: "0.5rem",
            display: { xs: "flex", sm: "none" },
          }}
        />
        <Box
          sx={{
            display: "flex",
            gap: "10px",
            marginTop: { sm: "7px" },
            alignItems: "center",
          }}
        >
          <Box
            sx={{
              display: { xs: !store.isLoggedIn ? "flex" : "none", md: "flex" },
            }}
          >
            <LanguageSelector
              sx={{
                "&&": {
                  padding: 0,
                },
                "&& .MuiSelect-select": {
                  paddingRight: "16px !important",
                },
                "&& .MuiSelect-icon": {
                  display: "none",
                },
                "&& .MuiInputBase-input": {
                  padding: "4px 8px!important",
                },
              }}
            />
          </Box>

          {store.isLoggedIn && <HeaderUserMenu />}
          <IconButton onClick={store.toggleTheme}>
            {store.getTheme === ColorTheme.LIGHT ? (
              <Brightness4Icon sx={{ fontSize: "17px" }} />
            ) : (
              <Brightness5Icon sx={{ fontSize: "17px" }} />
            )}
          </IconButton>
        </Box>
      </Box>
    </>
  );
});

export default Header;
