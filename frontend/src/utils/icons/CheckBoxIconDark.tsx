import { SvgIconProps } from "@mui/material/SvgIcon";
import SvgIcon from "@mui/material/SvgIcon";

const CheckBoxIconDark = (props: SvgIconProps) => (
  <SvgIcon {...props}>
    <rect
      x="3"
      y="3"
      width="18"
      height="18"
      fill="#04060a"
      stroke={"#486171"}
      strokeWidth="0.5"
      rx="2"
    />
  </SvgIcon>
);

export default CheckBoxIconDark;
