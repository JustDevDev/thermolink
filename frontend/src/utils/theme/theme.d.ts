import { Theme as MuiTheme } from "@mui/material/styles";

declare module "utils/theme/theme" {
  const Theme: {
    lightTheme: MuiTheme;
    darkTheme: MuiTheme;
  };
  export default Theme;
}
