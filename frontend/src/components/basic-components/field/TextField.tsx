import { TextField as MUITextField } from "@mui/material";
import { useEffect } from "react";
import { useFieldErrorNoticeStore } from "../../../hooks/stores/useFieldErrorNoticeStore";
import {
  OptionalFieldProps,
  RequiredWithPlaceholder,
  RequiredWithRequiredKey,
} from "../../../types/types";
import { observer } from "mobx-react-lite";
import { useAppTranslation } from "@/utils/translate/translate";
import { useMapSize } from "@/hooks/useMapSize";

type TextFieldProps =
  | RequiredWithPlaceholder
  | RequiredWithRequiredKey
  | OptionalFieldProps;

const TextField = observer(
  ({
    label,
    value,
    onChange,
    variant = "outlined",
    placeholder,
    fullWidth = false,
    required,
    requiredKey,
    InputProps,
    inputProps,
    ref,
    ...rest
  }: TextFieldProps) => {
    const fieldErrorNoticeStore = useFieldErrorNoticeStore();
    const { size } = useMapSize();
    const { t } = useAppTranslation();

    useEffect(() => {
      if (!required) {
        if (placeholder) {
          fieldErrorNoticeStore.deleteRequiredValue(placeholder);
        }
        return;
      }

      const key = requiredKey || placeholder || "";
      fieldErrorNoticeStore.addRequiredValue(key, value);

      return () => {
        if (required) {
          fieldErrorNoticeStore.deleteRequiredValue(key);
        }
      };
    }, [required, requiredKey, placeholder, value, fieldErrorNoticeStore]);

    return (
      <MUITextField
        fullWidth={fullWidth}
        label={label && t(label)}
        placeholder={placeholder && t(placeholder)}
        variant={variant}
        value={value}
        size={size}
        onChange={(e) => onChange(e.target.value)}
        InputProps={InputProps}
        inputProps={inputProps}
        ref={ref}
        {...rest}
      />
    );
  }
);

// Nastavení display name pro debugging
TextField.displayName = "TextField";

export default TextField;
