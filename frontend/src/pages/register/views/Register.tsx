import { Box, Divider } from "@mui/material";
import { useEffect } from "react";
import { observer } from "mobx-react-lite";
import { appPaths } from "@/routers/app/appPath";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { useFieldErrorNoticeStore } from "@/hooks/stores/useFieldErrorNoticeStore";
import { useRegisterStore } from "@/pages/register/hooks/useRegisterStore";
import { useLoginStore } from "@/pages/login/hooks/useLoginStore";
import { ResponseRegisterType } from "@/types/api/responseTypes";
import AuthBox from "@/components/basic-components/box/AuthBox";
import Title from "@/components/basic-components/typography/Title";
import { FieldErrorNotice } from "@/components/common-components/field-error-notice/FieldErrorNotice";
import Label from "@/components/basic-components/typography/Label";
import TextField from "@/components/basic-components/field/TextField";
import { PasswordField } from "@/components/basic-components/field/PasswordField";
import LoadingButton from "@/components/basic-components/button/LoadingButton";
import { GoogleLoginButton } from "@/components/basic-components/button/LoginGoogleButton";
import { alertManager } from "@/utils/alert/AlertManager";
import { RegisterBenefits } from "../components/RegisterBenefits";
import {
  checkPasswordLength,
  containsSpecialCharacter,
} from "../RegisterUtils";
import CircleIcon from "@mui/icons-material/Circle";
import { useEnterKeySubmit } from "@/hooks/useEnterKeySubmit";

const Register = observer(() => {
  const FieldErrorNoticeStore = useFieldErrorNoticeStore();
  const store = useRegisterStore();
  const userStore = useUserStore();
  const loginStore = useLoginStore();
  const navigate = useNavigate();

  const handleRegister = async () => {
    if (FieldErrorNoticeStore.checkRequiredValues()) {
      const error = store.checkFields();
      if (error) return FieldErrorNoticeStore.setCollapse(true, error);

      const response: ResponseRegisterType = await store.register();
      if (response.error)
        return FieldErrorNoticeStore.setCollapse(
          true,
          `error.${response.error}`
        );
      FieldErrorNoticeStore.setCollapse(false, "");
      FieldErrorNoticeStore.clearRequiredValues();
      alertManager.success({
        intld: "success.registrationSuccessMessage",
        persistent: true,
      });
    } else {
      FieldErrorNoticeStore.setCollapse(true, "error.fillAllFields");
    }
  };

  useEnterKeySubmit(handleRegister);

  useEffect(() => {
    if (userStore.isLoggedIn) {
      navigate(appPaths.ADMIN);
    }
    return () => {
      FieldErrorNoticeStore.setCollapse(false, "");
      FieldErrorNoticeStore.clearRequiredValues();
      store.resetRegisterData();
    };
  }, [FieldErrorNoticeStore, store, userStore.isLoggedIn, navigate]);

  return (
    <Box
      sx={{
        display: "flex",
        gap: "1.25rem",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        flexDirection: "row",
        padding: "0 1rem 0 1rem",
      }}
    >
      <RegisterBenefits />
      <AuthBox sx={{ paddingTop: "0" }}>
        <Box sx={{ mt: 2 }}>
          <Title
            intld="auth.signUp"
            variant="h4"
            sx={{ fontWeight: "700", textAlign: "left" }}
          />
        </Box>
        <FieldErrorNotice message={FieldErrorNoticeStore.errorMessage} />
        <Box sx={{ mt: 3 }}>
          <Label
            intld="label.email"
            sx={{ marginBottom: "6px", textAlign: "left" }}
          />

          <TextField
            required
            fullWidth={true}
            value={store.email}
            onChange={(e) => store.setEmail(e)}
            placeholder="placeHolder.email"
          />
        </Box>
        <Box sx={{ mt: 2 }}>
          <Label
            intld="label.password"
            sx={{ marginBottom: "6px", textAlign: "left" }}
          />
          <PasswordField
            required
            fullWidth={true}
            placeholder="placeHolder.password"
            value={store.password}
            showIcon={false}
            onChange={(e) => store.setPassword(e)}
          />
        </Box>
        <Box sx={{ mt: 2 }}>
          <Label
            intld="label.repeatPassword"
            sx={{ marginBottom: "6px", textAlign: "left" }}
          />
          <PasswordField
            required
            fullWidth={true}
            placeholder="placeHolder.password"
            showIcon={false}
            requiredKey="repeatPassword"
            value={store.repeatPassword}
            id={"repeatPassword"}
            onChange={(e) => store.setRepeatPassword(e)}
          />
        </Box>
        <Box sx={{ mt: 1, display: "flex", flexDirection: "column", gap: 1 }}>
          <Box
            sx={{
              display: "flex",
              gap: 1,
              alignItems: "center",
            }}
          >
            <CircleIcon
              sx={(theme) => ({
                color: checkPasswordLength(store.password)
                  ? theme.palette.success.main
                  : theme.palette.error.main,
                fontSize: "0.7rem",
              })}
            />
            <Label intld="error.passwordLength" />
          </Box>
          <Box
            sx={{
              display: "flex",
              gap: 1,
              alignItems: "center",
              textAlign: "left",
            }}
          >
            <CircleIcon
              sx={(theme) => ({
                color: containsSpecialCharacter(store.password)
                  ? theme.palette.success.main
                  : theme.palette.error.main,
                fontSize: "0.7rem",
              })}
            />
            <Label intld="error.specialCharacter" />
          </Box>
        </Box>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: 2,
            mt: 5,
            textAlign: "left",
          }}
        >
          <Box>
            <LoadingButton
              intld="auth.signUp"
              onClick={handleRegister}
              fullwidth={true}
              loading={store.registerProvider.loading}
            />
          </Box>
          <Divider>
            <Label intld="or" />
          </Divider>
          <Box>
            <GoogleLoginButton onClick={loginStore.loginGoogle} />
          </Box>
        </Box>
      </AuthBox>
    </Box>
  );
});

export default Register;
