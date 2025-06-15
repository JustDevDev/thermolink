import { observer } from "mobx-react-lite";
import { useUserStore } from "./hooks/stores/useUserStore";
import { Box, CssBaseline } from "@mui/material";
import { ThemeProvider } from "@mui/material";
import Theme from "./utils/theme/theme";
import { ColorTheme } from "./stores/setting/settingStore";
import Header from "./components/common-components/header/Header";
import { RouterApp } from "./routers/app/RouterApp";
import { Alert } from "./components/common-components/alert/AlertNotification";

const App = observer(() => {
  const store = useUserStore();

  return (
    <ThemeProvider
      theme={
        store.getTheme === ColorTheme.LIGHT ? Theme.lightTheme : Theme.darkTheme
      }
    >
      <CssBaseline />
      <Alert />
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          minHeight: "100vh",
        }}
      >
        <Header />
        <Box
          component="main"
          sx={{
            display: "flex",
            justifyContent: "center",
            flexGrow: 1,
            width: "100%",
            boxSizing: "border-box",
          }}
        >
          <RouterApp />
        </Box>
      </Box>
    </ThemeProvider>
  );
});

export default App;
