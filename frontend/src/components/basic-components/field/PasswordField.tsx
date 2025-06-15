import { useFieldErrorNoticeStore } from "@/hooks/stores/useFieldErrorNoticeStore";
import {
  OptionalFieldProps,
  RequiredWithPlaceholder,
  RequiredWithRequiredKey,
} from "@/types/types";
import { useAppTranslation } from "@/utils/translate/translate";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import {
  FormControl,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useMapSize } from "@/hooks/useMapSize";

type PasswordFieldProps =
  | ({ showIcon?: boolean; id?: string } & RequiredWithPlaceholder)
  | ({ showIcon?: boolean; id?: string } & RequiredWithRequiredKey)
  | ({ showIcon?: boolean; id?: string } & OptionalFieldProps);

let uniqueIdCounter = 0;

export const PasswordField: React.FC<PasswordFieldProps> = ({
  label,
  placeholder,
  requiredKey,
  onChange,
  value,
  fullWidth = false,
  showIcon = true,
  required,
  id,
}) => {
  const [showPassword, setShowPassword] = useState(false);
  const fieldErrorNoticeStore = useFieldErrorNoticeStore();
  const { size } = useMapSize();
  const { t } = useAppTranslation();
  const [uniqueId] = useState(
    () => id || `password-field-${uniqueIdCounter++}`
  );

  const inputLabelSize = size === "medium" ? "normal" : "small";

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleMouseDownPassword = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
  };

  const handleMouseUpPassword = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
  };

  useEffect(() => {
    if (required) {
      fieldErrorNoticeStore.addRequiredValue(
        requiredKey || placeholder || "",
        value
      );
    } else {
      fieldErrorNoticeStore.deleteRequiredValue("password");
    }

    return () => {
      if (required && placeholder) {
        fieldErrorNoticeStore.deleteRequiredValue(placeholder);
      }
    };
  }, [fieldErrorNoticeStore, placeholder, required, requiredKey, value]);

  return (
    <FormControl variant="outlined" fullWidth={fullWidth}>
      <InputLabel htmlFor={uniqueId} size={inputLabelSize} shrink={true}>
        {label && t(label)}
      </InputLabel>
      <OutlinedInput
        id={uniqueId}
        type={showPassword ? "text" : "password"}
        size={size}
        endAdornment={
          showIcon && (
            <InputAdornment position="end">
              <IconButton
                disableRipple
                aria-label="toggle password visibility"
                onClick={handleClickShowPassword}
                onMouseDown={handleMouseDownPassword}
                onMouseUp={handleMouseUpPassword}
                edge="end"
                sx={{
                  border: "none",
                }}
              >
                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
              </IconButton>
            </InputAdornment>
          )
        }
        onChange={(e) => onChange(e.target.value)}
        label={label && t(label)}
        value={value}
        placeholder={placeholder && t(placeholder)}
      />
    </FormControl>
  );
};
