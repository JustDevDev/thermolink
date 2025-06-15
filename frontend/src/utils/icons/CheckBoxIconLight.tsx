import { SvgIconProps } from "@mui/material/SvgIcon";
import SvgIcon from "@mui/material/SvgIcon";

const CheckBoxIconLight = (props: SvgIconProps) => (
  <SvgIcon {...props}>
    <rect
      x="3"
      y="3"
      width="18"
      height="18"
      fill="#f5f8fa"
      stroke={"#939393"}
      strokeWidth="0.2"
      rx="3"
    />
  </SvgIcon>
);

export default CheckBoxIconLight;
