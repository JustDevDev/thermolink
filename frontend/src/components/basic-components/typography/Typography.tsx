import { useAppTranslation } from "@/utils/translate/translate";
import { Typography as MuiTypography, SxProps, Theme } from "@mui/material";

type TypographyTypeBase = {
  type:
    | "h1"
    | "h2"
    | "h3"
    | "h4"
    | "h5"
    | "h6"
    | "subtitle1"
    | "subtitle2"
    | "body1"
    | "body2"
    | "caption"
    | "button"
    | "overline";
  className?: string;
  sx?: SxProps<Theme>;
  color?: string;
};

type TypographyType =
  | (TypographyTypeBase & { intld: string; children?: React.ReactNode })
  | (TypographyTypeBase & { intld?: string; children: React.ReactNode })
  | (TypographyTypeBase & { intld: string; children: React.ReactNode });

export const Typography = (props: TypographyType) => {
  const { type, intld, className, sx, color = "primary", children } = props;

  const { t } = useAppTranslation();

  const content = intld ? t(intld) : children;

  return (
    <MuiTypography variant={type} className={className} sx={sx} color={color}>
      {content}
    </MuiTypography>
  );
};
