import * as React from "react";
import Box from "@mui/material/Box";
import Autocomplete from "@mui/material/Autocomplete";
import { debounce } from "@mui/material/utils";
import { observer } from "mobx-react-lite";
import { AutocompleteRenderOptionState } from "@mui/material/Autocomplete";
import { TextField } from "@mui/material";
import { useAppTranslation } from "@/utils/translate/translate";
import { MultiDataSelectProvider } from "@/data/MultiDataSelectProvider";

interface MultiDataSelectProps<T, ServerModel> {
  provider: MultiDataSelectProvider<ServerModel, T[]>;
  value: T | null;
  onChange: (value: T | null) => void;
  getOptionLabel: (option: T) => string;
  label?: string;
  placeholder?: string;
  width?: number | string;
  disabled?: boolean;
  renderOption?: (
    props: React.HTMLAttributes<HTMLLIElement>,
    option: T,
    state: AutocompleteRenderOptionState
  ) => React.ReactNode;
}

const MultiDataSelect = observer(
  <T extends { id: string | number }, ServerModel>({
    provider,
    value,
    onChange,
    getOptionLabel,
    label = "Option",
    placeholder = "Select an option",
    width = 250,
    disabled = false,
    renderOption,
  }: MultiDataSelectProps<T, ServerModel>) => {
    const [inputValue, setInputValue] = React.useState(
      value ? getOptionLabel(value) : ""
    );
    const [options, setOptions] = React.useState<T[]>([]);
    const { t } = useAppTranslation();

    const fetchData = React.useMemo(
      () =>
        debounce(async (input: string) => {
          if (!input || input.length < 2) {
            setOptions([]);
            return;
          }
          try {
            provider.option = input;
            await provider.getData();
            setOptions(provider.data);
          } catch (error) {
            console.error("Failed to fetch data:", error);
            setOptions([]);
          }
        }, 400),
      [provider]
    );

    React.useEffect(() => {
      fetchData(inputValue);
    }, [inputValue, fetchData]);

    return (
      <Autocomplete
        size="small"
        sx={{ width }}
        getOptionLabel={getOptionLabel}
        filterOptions={(x) => x}
        options={options}
        autoComplete
        includeInputInList
        filterSelectedOptions
        value={value || null}
        disabled={disabled}
        noOptionsText={t("placeHolder.noOptions")}
        loading={provider.loading}
        onChange={(_, newValue) => {
          onChange(newValue);
        }}
        onInputChange={(_, newInputValue, reason) => {
          if (reason === "reset" && value) {
            setInputValue(getOptionLabel(value));
          } else {
            setInputValue(newInputValue);
          }
        }}
        renderInput={(params) => (
          <TextField
            {...params}
            label={t(label)}
            fullWidth
            placeholder={t(placeholder)}
          />
        )}
        renderOption={
          renderOption ||
          ((props, option) => (
            <li {...props} key={option.id}>
              <Box component="span">{getOptionLabel(option)}</Box>
            </li>
          ))
        }
      />
    );
  }
);

export default MultiDataSelect;
