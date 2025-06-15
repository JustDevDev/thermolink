import { SxProps, Theme } from "@mui/material";
import { Typography } from "./Typography";

type SubTitlePropsBase = {
  variant?: "subtitle1" | "subtitle2";
  className?: string;
  sx?: SxProps<Theme>;
  color?: string;
};

type SubTitleProps =
  | (SubTitlePropsBase & { intld: string; children?: React.ReactNode })
  | (SubTitlePropsBase & { intld?: string; children: React.ReactNode })
  | (SubTitlePropsBase & { intld: string; children: React.ReactNode });

const SubTitle = ({
  variant = "subtitle1",
  intld,
  className,
  sx,
  color,
  children,
}: SubTitleProps) => {
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

export default SubTitle;
