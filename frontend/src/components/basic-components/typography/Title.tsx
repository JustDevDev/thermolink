import { Typography } from "./Typography";
import { SxProps, Theme } from "@mui/material";

type TitlePropsBase = {
  variant?: "h1" | "h2" | "h3" | "h4" | "h5" | "h6";
  className?: string;
  sx?: SxProps<Theme>;
  color?: string;
};

type TitleProps =
  | (TitlePropsBase & { intld: string; children?: React.ReactNode })
  | (TitlePropsBase & { intld?: string; children: React.ReactNode })
  | (TitlePropsBase & { intld: string; children: React.ReactNode });

const Title = ({
  variant = "h4",
  intld,
  className,
  sx,
  color,
  children,
}: TitleProps) => {
  return (
    <Typography
      type={variant}
      color={color}
      className={className}
      sx={sx}
      intld={intld}
      children={children}
    />
  );
};

export default Title;
