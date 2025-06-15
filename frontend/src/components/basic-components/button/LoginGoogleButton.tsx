import { useUserStore } from "../../../hooks/stores/useUserStore";
import Button from "./Button";
import GoogleLogo from "@/utils/icons/google.svg";
import { observer } from "mobx-react-lite";
import { ColorTheme } from "../../../stores/setting/settingStore";
import { gray } from "@/utils/theme/themePrimitives";

type GoogleLoginButtonProps = {
  onClick: () => void;
};

export const GoogleLoginButton = observer((props: GoogleLoginButtonProps) => {
  const { onClick } = props;
  const userStore = useUserStore();

  return (
    <Button
      fullwidth={true}
      intld="auth.signWithGoogle"
      sx={
        userStore.theme === ColorTheme.LIGHT
          ? {
              backgroundColor: "#fdfdfc",
              color: gray[900],
              border: `1px solid ${gray[100]}`,
              boxShadow: "none",
              "&:hover": {
                backgroundColor: "#f7f7f7",
                borderColor: gray[400],
                opacity: 1,
              },
            }
          : {
              backgroundColor: gray[900],
              color: "#fff",
              border: `1px solid ${gray[700]}`,
              "&:hover": {
                borderColor: gray[600],
              },
            }
      }
      startIcon={
        <img
          src={GoogleLogo}
          alt="Google logo"
          style={{ width: 24, height: 24 }}
        />
      }
      onClick={onClick}
    />
  );
});
