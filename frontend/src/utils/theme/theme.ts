import { createTheme, responsiveFontSizes } from "@mui/material/styles";
import { getThemeOptions } from "./themeOptions";

let lightTheme = createTheme(getThemeOptions("light"));
let darkTheme = createTheme(getThemeOptions("dark"));

lightTheme = responsiveFontSizes(lightTheme);
darkTheme = responsiveFontSizes(darkTheme);

const Theme = { lightTheme, darkTheme };
export default Theme;
