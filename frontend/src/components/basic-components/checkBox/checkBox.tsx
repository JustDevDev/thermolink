import { useAppTranslation } from "@/utils/translate/translate";
import {
  Checkbox as MUICheckbox,
  FormControlLabel,
  FormGroup,
  SxProps,
  Theme,
} from "@mui/material";

type CheckBoxProps = {
  sxFormGroup?: SxProps<Theme>;
  checkBoxs: {
    intld: string;
    value: string;
    disabled?: boolean;
    defaultChecked?: boolean;
    onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
    color?: "primary" | "secondary";
    sx?: SxProps<Theme>;
  }[];
};

const CheckBox = (props: CheckBoxProps) => {
  const { sxFormGroup, checkBoxs } = props;

  const { t } = useAppTranslation();

  return (
    <FormGroup sx={sxFormGroup}>
      {checkBoxs.map((checkBox, index) => (
        <FormControlLabel
          key={`checkBox-${index}`}
          control={
            <MUICheckbox
              defaultChecked={checkBox.defaultChecked}
              disabled={checkBox.disabled}
              onChange={checkBox.onChange}
              sx={checkBox.sx}
              color={checkBox.color}
            />
          }
          label={t(checkBox.intld)}
        />
      ))}
    </FormGroup>
  );
};

export default CheckBox;
