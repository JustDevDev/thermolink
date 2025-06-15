import { useAppTranslation } from "@/utils/translate/translate";
import { Button as MuiButton } from "@mui/material";

import { SxProps, Theme } from "@mui/system";

interface ButtonProps {
  onClick: () => void;
  children?: React.ReactNode;
  intld?: string;
  color?: "primary" | "secondary";
  size?: "small" | "medium" | "large";
  variant?: "text" | "outlined" | "contained";
  fullwidth?: boolean;
  startIcon?: React.ReactNode;
  endIcon?: React.ReactNode;
  sx?: SxProps<Theme>;
}

const Button = (props: ButtonProps) => {
  const {
    onClick,
    children,
    intld,
    color,
    size = "small",
    variant = "contained",
    fullwidth = false,
    startIcon,
    endIcon,
    sx,
  } = props;

  const { t } = useAppTranslation();

  return (
    <MuiButton
      onClick={onClick}
      color={color}
      size={size}
      variant={variant}
      sx={{
        borderRadius: "0.50rem",
        textTransform: "none",
        fontSize: "0.9rem",
        padding: "0.5rem 1rem",
        fontWeight: 600,
        ...sx,
      }}
      fullWidth={fullwidth}
      startIcon={startIcon}
      endIcon={endIcon}
    >
      {intld ? t(intld) : children}
    </MuiButton>
  );
};

export default Button;
