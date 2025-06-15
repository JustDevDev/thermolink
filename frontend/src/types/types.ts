import { InputProps as StandardInputProps } from "@mui/material/Input";
import { OutlinedInputProps } from "@mui/material/OutlinedInput";
import { FilledInputProps } from "@mui/material/FilledInput";
import { adminPaths } from "@/routers/admin/adminPath";

export type TError = {
  error?: string | null;
};

export type TId<T extends number | string = number> = {
  id: T;
};

/**
 * @description
 * Interface for the table
 */

export interface TableColumns {
  name: string;
  label: string;
  sortable?: boolean;
  key: string;
  align?: "left" | "right" | "center";
}

/**
 * @description
 * Interface for the base field props
 */
export interface BaseFieldProps {
  label?: string;
  value?: string;
  onChange: (value: string) => void;
  variant?: "standard" | "filled" | "outlined";
  fullWidth?: boolean;
  placeholder?: string;
  requiredKey?: string;
  InputProps?: Partial<
    StandardInputProps | FilledInputProps | OutlinedInputProps
  >;
  inputProps?: React.InputHTMLAttributes<HTMLInputElement>;
  ref?: React.Ref<HTMLDivElement>;
}

export interface RequiredWithPlaceholder extends BaseFieldProps {
  required: true;
  placeholder: string;
  requiredKey?: string;
}

export interface RequiredWithRequiredKey extends BaseFieldProps {
  required: true;
  placeholder?: string;
  requiredKey: string;
}

export interface OptionalFieldProps extends BaseFieldProps {
  required?: false;
}

export interface ResponseSensorsTableTypes<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  currentPage: number;
}

export interface HelpDialogTypes {
  name: adminPaths;
  title: string;
  description: string;
  image: string;
}
