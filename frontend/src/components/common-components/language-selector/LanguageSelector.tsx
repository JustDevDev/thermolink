import { observer } from "mobx-react-lite";
import Select from "@/components/basic-components/select/Select";
import { Language } from "@/stores/setting/settingStore";
import { useUserStore } from "@/hooks/stores/useUserStore";
import UKFlag from "@/utils/icons/uk-flag.png";
import CZFlag from "@/utils/icons/cs-flag.png";
import { SxProps, Theme } from "@mui/material";

type LanguageSelectorProps = {
  sx?: SxProps<Theme>;
};

const LanguageSelector = observer((props: LanguageSelectorProps) => {
  const { sx } = props;
  const store = useUserStore();

  const languageOptions = [
    {
      value: Language.EN,
      content: <img src={UKFlag} alt="UK Flag" style={{ width: "26px" }} />,
    },
    {
      value: Language.CS,
      content: <img src={CZFlag} alt="Czech Flag" style={{ width: "26px" }} />,
    },
  ];

  const changeLanguage = (hodnota: string) => {
    store.setLanguage(hodnota);
  };

  return (
    <Select
      items={languageOptions}
      value={store.getLanguage}
      onChange={changeLanguage}
      selectSx={sx}
    />
  );
});

export default LanguageSelector;
