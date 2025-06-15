import CheckBoxIconLight from "../icons/CheckBoxIconLight";
import CheckBoxIconDark from "../icons/CheckBoxIconDark";

export const getCheckboxIcon = (isLight: boolean) =>
  isLight ? <CheckBoxIconLight /> : <CheckBoxIconDark />;
