import { Typography } from "./Typography";
import { SxProps, Theme } from "@mui/material";

type LabelPropsBase = {
  className?: string;
  sx?: SxProps<Theme>;
};

type LabelProps =
  | (LabelPropsBase & { intld: string; children?: React.ReactNode })
  | (LabelPropsBase & { intld?: string; children: React.ReactNode })
  | (LabelPropsBase & { intld: string; children: React.ReactNode });

const Label = ({ intld, className, sx, children }: LabelProps) => {
  return (
    <Typography
      type={"body2"}
      color="text.secondary"
      className={className}
      sx={sx}
      intld={intld}
      children={children}
    />
  );
};

export default Label;
