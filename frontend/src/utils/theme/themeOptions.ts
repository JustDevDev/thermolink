import { ThemeOptions, alpha } from "@mui/material/styles";
import { gray, brand, red, green } from "./themeConstants";
import { shape } from "./themeConstants";
import { getCheckboxIcon } from "./checkboxIcons";

export function getThemeOptions(mode: "light" | "dark"): ThemeOptions {
  const isLight = mode === "light";

  return {
    typography: {
      fontFamily: "sans-serif",
    },
    palette: {
      mode,
      primary: isLight
        ? { main: gray[900], light: gray[400], contrastText: "#fff" }
        : { main: "#ffffff", light: gray[300], contrastText: gray[900] },
      secondary: {
        main: brand[600],
        light: brand[400],
        dark: brand[700],
        contrastText: "#fff",
      },
      background: isLight
        ? { default: "#fafcff", paper: "#ffffff" }
        : { default: gray[900] },
      text: isLight
        ? { primary: gray[900], secondary: gray[500] }
        : { primary: gray[50], secondary: gray[400] },
      error: isLight
        ? { main: red[300], light: red[200], contrastText: red[600] }
        : { main: red[500], light: red[500], contrastText: "#ffffff" },
      success: isLight
        ? { main: green[400], light: green[200], contrastText: green[500] }
        : { main: green[500], light: green[500], contrastText: "#ffffff" },
      divider: mode === "dark" ? alpha(gray[700], 0.6) : alpha(gray[300], 0.4),
    },
    components: {
      MuiCssBaseline: {
        styleOverrides: {
          body: {
            height: "100%",
            backgroundColor: isLight ? "#fefeff" : gray[900],
            // Přidáváme globální styly pro scrollbar
            "&::-webkit-scrollbar": {
              width: "0.4em",
              height: "0.4em",
            },
            "&::-webkit-scrollbar-thumb": {
              backgroundColor: isLight ? brand[300] : gray[800],
              borderRadius: "8px",
            },
            "&::-webkit-scrollbar-track": {
              backgroundColor: "transparent",
            },
            scrollbarWidth: "thin",
            scrollbarColor: `${isLight ? gray[300] : gray[600]} transparent`,
          },
          "*": {
            "&::-webkit-scrollbar": {
              width: "0.4em",
              height: "0.4em",
            },
            "&::-webkit-scrollbar-thumb": {
              backgroundColor: isLight ? gray[300] : gray[600],
              borderRadius: "8px",
            },
            "&::-webkit-scrollbar-track": {
              backgroundColor: "transparent",
            },
            scrollbarWidth: "thin",
            scrollbarColor: `${isLight ? gray[300] : gray[600]} transparent`,
          },
        },
      },
      MuiOutlinedInput: {
        styleOverrides: {
          root: {
            borderRadius: shape.borderRadius,
            padding: "0 1rem 0 0",
            background: isLight ? "#fdfdfc" : gray[900],
            "& .MuiOutlinedInput-notchedOutline": {
              borderWidth: "1px",
              borderColor: isLight ? gray[100] : gray[700],
              transition: "border-color 0.3s ease",
            },
            "&:hover .MuiOutlinedInput-notchedOutline": {
              borderColor: isLight ? gray[400] : gray[600],
            },
            "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
              borderColor: brand[600],
            },
            fontSize: "0.875rem",
          },
          input: {
            "&:-webkit-autofill": {
              WebkitBoxShadow: "0 0 0 1000px transparent inset",
              transition: "background-color 5000s ease-in-out 0s",
            },
            "&:-webkit-autofill:hover": {
              WebkitBoxShadow: "0 0 0 1000px transparent inset",
            },
            "&:-webkit-autofill:focus": {
              WebkitBoxShadow: "0 0 0 1000px transparent inset",
              background: "transparent",
            },
          },
        },
      },
      MuiSelect: {
        styleOverrides: {
          select: {
            borderRadius: shape.borderRadius,
            padding: "4px 8px",
            fontSize: "0.875rem",
          },
        },
      },
      MuiInputLabel: {
        styleOverrides: {
          root: {
            fontSize: "0.875rem",
            transform: `translate(${8}px, ${3}px) scale(1)`,
            "&.Mui-focused": {
              transform: `translate(${10}px, ${-7}px) scale(0.75)`,
            },
          },
          shrink: {
            transform: `translate(${8}px, ${-6}px) scale(0.75)`,
          },
        },
      },
      MuiList: {
        styleOverrides: {
          root: {
            "& .MuiButtonBase-root": {
              fontSize: "0.875rem",
              padding: "4px 8px",
            },
          },
        },
      },
      MuiCard: {
        styleOverrides: {
          root: {
            borderRadius: shape.borderRadius,
            border: isLight
              ? `1px solid ${gray[100]}`
              : `1px solid ${gray[700]}`,
            boxShadow: isLight ? "0px 0px 0px rgba(0, 0, 0, 0)" : undefined,
            background: isLight ? undefined : gray[800],
          },
        },
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            border: isLight
              ? `1px solid ${gray[100]}`
              : `1px solid ${gray[700]}`,
            borderRadius: shape.borderRadius,
            boxShadow: isLight ? "0px 4px 8px rgba(0, 0, 0, 0.05)" : undefined,
            background: isLight ? undefined : gray[800],
          },
        },
      },
      MuiButton: {
        styleOverrides: {
          root: {
            "&:hover": {
              opacity: 0.8,
              boxShadow: "none",
            },
          },
        },
      },
      MuiCheckbox: {
        defaultProps: {
          icon: getCheckboxIcon(isLight),
        },
        styleOverrides: {
          root: {
            "&:hover": {
              backgroundColor: "transparent",
            },
          },
        },
      },
      MuiIconButton: {
        styleOverrides: {
          root: {
            borderRadius: shape.borderRadius,
            transition: "all 0.3s ease",
            "&:hover": {
              borderColor: isLight ? gray[400] : gray[600],
              backgroundColor: isLight && gray[100],
            },
          },
        },
      },
      MuiListItemButton: {
        styleOverrides: {
          root: {
            borderRadius: shape.borderRadius,
            color: isLight ? gray[600] : gray[400],
            padding: "0.2rem 1rem",
            "&:hover": {
              backgroundColor: isLight && gray[50],
            },
            "&.Mui-selected": {
              backgroundColor: isLight && gray[100],
              color: isLight ? gray[900] : gray[50],
            },
          },
        },
      },
      MuiListItemIcon: {
        styleOverrides: {
          root: {
            minWidth: 0,
          },
        },
      },
      MuiMenuItem: {
        styleOverrides: {
          root: {
            padding: "0.5rem 1rem",
            margin: "0.2rem",
            borderRadius: shape.borderRadius,
            "&:hover": {
              backgroundColor: isLight && gray[50],
            },
            "&.Mui-selected": {
              backgroundColor: isLight && gray[100],
              color: isLight ? gray[900] : gray[50],
            },
          },
        },
      },
      MuiTableContainer: {
        styleOverrides: {
          root: {
            borderRadius: shape.borderRadius,
            border: isLight
              ? `1px solid ${gray[100]}`
              : `1px solid ${gray[700]}`,
          },
        },
      },
      MuiTableHead: {
        styleOverrides: {
          root: {
            backgroundColor: isLight ? gray[50] : gray[800],
            "& .MuiTableCell-root": {
              fontWeight: "bold",
              color: isLight ? gray[600] : gray[300],
            },
          },
        },
      },
      MuiTableRow: {
        styleOverrides: {
          root: {
            borderBottom: isLight
              ? `1px solid ${gray[100]}`
              : `1px solid ${gray[700]}`,
          },
        },
      },
      MuiTableCell: {
        styleOverrides: {
          root: {
            borderBottom: "none",
            padding: "0.5rem 1rem",
          },
        },
      },
      MuiTableFooter: {
        styleOverrides: {
          root: {
            backgroundColor: isLight ? gray[50] : gray[800],
            "& .MuiTableRow-root": {
              borderBottom: "none",
            },
          },
        },
      },
      MuiTablePagination: {
        styleOverrides: {
          root: {
            "& .MuiToolbar-root": {
              minHeight: "40px",
            },
            "& .MuiIconButton-root": {
              border: isLight
                ? `1px solid ${gray[200]}`
                : `1px solid ${gray[700]}`,
              padding: 3,
              margin: "0 4px",
            },
          },
        },
      },
      MuiSkeleton: {
        styleOverrides: {
          root: {
            backgroundColor: isLight ? gray[100] : gray[700],
            borderRadius: 6,
          },
        },
      },
      MuiChip: {
        styleOverrides: {
          root: {
            padding: "0",
            height: "auto",
            "&.success-chip": {
              backgroundColor: isLight ? green[100] : green[800],
              color: isLight ? green[600] : green[400],
              border: `1px solid ${isLight ? green[300] : green[700]}`,
            },
            "&.error-chip": {
              backgroundColor: isLight ? gray[100] : gray[800],
              color: isLight ? gray[600] : gray[300],
              border: `1px solid ${isLight ? gray[300] : gray[700]}`,
            },
            "& span": {
              padding: "0.1rem 0.5rem",
              fontSize: "0.7rem",
              fontWeight: "bold",
            },
          },
        },
      },
      MuiLinearProgress: {
        styleOverrides: {
          root: {
            borderRadius: shape.borderRadius,
            backgroundColor: isLight ? gray[300] : gray[600],
          },
        },
      },
    },
    shape: {
      borderRadius: shape.borderRadius,
    },
  };
}
