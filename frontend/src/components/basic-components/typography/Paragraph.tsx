import { SxProps, Theme } from "@mui/material";
import { Typography } from "./Typography";

type ParagraphPropsBase = {
  variant?: "body1" | "body2";
  className?: string;
  sx?: SxProps<Theme>;
  color?: string;
};

type ParagraphProps =
  | (ParagraphPropsBase & { intld: string; children?: React.ReactNode })
  | (ParagraphPropsBase & { intld?: string; children: React.ReactNode })
  | (ParagraphPropsBase & { intld: string; children: React.ReactNode });

const Paragraph = ({
  variant = "body1",
  intld,
  className,
  sx,
  color,
  children,
}: ParagraphProps) => {
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

export default Paragraph;
