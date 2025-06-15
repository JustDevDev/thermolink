import { Box, Paper, SxProps, Theme } from "@mui/material";
import { Logo } from "../logo/Logo";

type AuthBoxType = {
  children: React.ReactNode;
  sx?: SxProps<Theme>;
};

const AuthBox = (props: AuthBoxType) => {
  const { children, sx } = props;

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        maxWidth: "500px",
        padding: { xs: "0.5rem", sm: 0 },
        ...sx,
      }}
    >
      <Paper
        square={false}
        sx={{
          padding: { xs: 2, sm: 4 },
          width: "100%",
          textAlign: "center",
          display: "flex",
          flexDirection: "column",
          margin: { xs: 0, sm: "0.5rem" },
          maxWidth: { xs: "100%", sm: "400px" },
        }}
      >
        <Box
          sx={{
            display: { xs: "none", sm: "flex" },
            alignItems: "center",
            gap: 2,
            mb: 3,
          }}
        >
          <Logo width="28px" />
        </Box>
        {children}
      </Paper>
    </Box>
  );
};

export default AuthBox;
