import { useAppTranslation } from "@/utils/translate/translate";
import { Button } from "@mui/material";

interface LoadingButtonProps {
  onClick: () => void;
  children?: React.ReactNode;
  intld?: string;
  color?: "primary" | "secondary";
  size?: "small" | "medium" | "large";
  variant?: "text" | "outlined" | "contained";
  loading?: boolean;
  fullwidth?: boolean;
}

const LoadingButton = (props: LoadingButtonProps) => {
  const {
    onClick,
    children,
    intld,
    color,
    size = "small",
    variant = "contained",
    loading,
    fullwidth = false,
  } = props;

  const { t } = useAppTranslation();

  return (
    <Button
      fullWidth={fullwidth}
      color={color}
      size={size}
      onClick={onClick}
      variant={variant}
      loading={loading}
      sx={{
        borderRadius: "0.50rem",
        textTransform: "none",
        fontSize: "0.9rem",
        padding: "0.35rem 1rem",
        fontWeight: 600,
      }}
    >
      {intld ? t(intld) : children}
    </Button>
  );
};

export default LoadingButton;
