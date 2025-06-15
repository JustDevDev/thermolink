import { Box } from "@mui/material";

interface ViewProps extends React.HTMLAttributes<HTMLDivElement> {
  children?: React.ReactNode;
}

const View = (props: ViewProps) => {
  const { children, ...rest } = props;
  return (
    <Box
      {...rest}
      sx={{
        padding: "1rem",
        width: "100%",
        display: "flex",
        justifyContent: "center",
      }}
    >
      {children}
    </Box>
  );
};

export default View;
