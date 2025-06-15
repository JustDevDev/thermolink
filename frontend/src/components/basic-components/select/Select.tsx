import { useAppTranslation } from "@/utils/translate/translate";
import {
  FormControl,
  InputLabel,
  MenuItem,
  Select as MuiSelect,
  SelectChangeEvent,
  SxProps,
  Theme,
} from "@mui/material";

export type selectItemsModel<T = string> = {
  value: T;
  content: string | React.ReactNode;
};

export type SelectProps<T = string> = {
  items: selectItemsModel<T>[];
  value: T;
  onChange: (value: T) => void;
  label?: string;
  fullWidth?: boolean;
  disabled?: boolean;
  selectSx?: SxProps<Theme>;
};

const Select = <T extends string | number>(props: SelectProps<T>) => {
  const { items, value, label, onChange, fullWidth, disabled, selectSx } =
    props;
  const { t } = useAppTranslation();

  return (
    <FormControl
      fullWidth={fullWidth}
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
      }}
    >
      <InputLabel>{t(label || "")}</InputLabel>
      <MuiSelect
        value={value.toString()}
        onChange={(event: SelectChangeEvent) => {
          const rawValue = event.target.value;
          const parsedValue =
            typeof value === "number" ? Number(rawValue) : rawValue;
          onChange(parsedValue as T);
        }}
        disabled={disabled}
        sx={selectSx}
      >
        {items.map((item) => (
          <MenuItem key={item.value.toString()} value={item.value.toString()}>
            {item.content}
          </MenuItem>
        ))}
      </MuiSelect>
    </FormControl>
  );
};

export default Select;
