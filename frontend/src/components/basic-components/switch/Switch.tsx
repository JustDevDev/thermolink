import { Switch as MuiSwitch } from "@mui/material";

interface ISwitchProps {
  checked?: boolean;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  color?: "primary" | "secondary";
  size?: "small" | "medium";
}

const Switch = (props: ISwitchProps) => {
  const { checked, onChange, color, size = "medium" } = props;

  return (
    <MuiSwitch
      checked={checked}
      onChange={onChange}
      color={color}
      size={size}
    />
  );
};

export default Switch;
