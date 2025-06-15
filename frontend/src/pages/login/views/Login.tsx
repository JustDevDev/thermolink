import { Box, Divider } from "@mui/material";
import { observer } from "mobx-react-lite";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { appPaths } from "@/routers/app/appPath";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { useFieldErrorNoticeStore } from "@/hooks/stores/useFieldErrorNoticeStore";
import { useLoginStore } from "@/pages/login/hooks/useLoginStore";
import { ResponseLoginType, SuccessResponse } from "@/types/api/responseTypes";
import AuthBox from "@/components/basic-components/box/AuthBox";
import Title from "@/components/basic-components/typography/Title";
import { FieldErrorNotice } from "@/components/common-components/field-error-notice/FieldErrorNotice";
import Label from "@/components/basic-components/typography/Label";
import TextField from "@/components/basic-components/field/TextField";
import LineButton from "@/components/basic-components/button/LineButton";
import { PasswordField } from "@/components/basic-components/field/PasswordField";
import LoadingButton from "@/components/basic-components/button/LoadingButton";
import { GoogleLoginButton } from "@/components/basic-components/button/LoginGoogleButton";
import { useEnterKeySubmit } from "@/hooks/useEnterKeySubmit";

const isSuccessResponse = (
  resp: ResponseLoginType
): resp is SuccessResponse => {
  return resp.error === undefined;
};

const Login = observer(() => {
  const FieldErrorNoticeStore = useFieldErrorNoticeStore();
  const store = useLoginStore();
  const userStore = useUserStore();
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!FieldErrorNoticeStore.checkRequiredValues()) {
      FieldErrorNoticeStore.setCollapse(true, "error.fillAllFields");
      return;
    }

    const fieldError = store.checkFields();
    if (fieldError) {
      FieldErrorNoticeStore.setCollapse(true, fieldError);
      return;
    }

    const response: ResponseLoginType = await store.login();
    if (response.error) {
      FieldErrorNoticeStore.setCollapse(true, `error.${response.error}`);
      return;
    }

    if (!isSuccessResponse(response)) {
      return;
    }

    userStore.saveLoginData(response);
    navigate(appPaths.ADMIN);
  };

  useEnterKeySubmit(handleLogin);

  useEffect(() => {
    if (userStore.isLoggedIn) {
      navigate(appPaths.ADMIN);
    }
    return () => {
      FieldErrorNoticeStore.setCollapse(false, "");
      FieldErrorNoticeStore.clearRequiredValues();
      store.resetLoginData();
    };
  }, [FieldErrorNoticeStore, store, userStore.isLoggedIn, navigate]);

  return (
    <AuthBox>
      <Box sx={{ display: "flex", flex: 1, flexDirection: "column" }}>
        <Box sx={{ mt: 2 }}>
          <Title
            intld="auth.signIn"
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
        <Box sx={{ mt: 3 }}>
          <Box className="flex justify-between">
            <Label
              intld="label.password"
              sx={{ marginBottom: "6px", textAlign: "left" }}
            />
            <LineButton
              intld="auth.forgotYourPassword"
              onClick={() => navigate(appPaths.FORGOTTEN_PASSWORD)}
            />
          </Box>
          <PasswordField
            required
            fullWidth={true}
            placeholder="placeHolder.password"
            value={store.password}
            onChange={(e) => store.setPassword(e)}
          />
        </Box>
        <Box sx={{ mt: 5 }}>
          <LoadingButton
            intld="auth.signIn"
            onClick={handleLogin}
            fullwidth={true}
            loading={store.loginProvider.loading}
          />
        </Box>
        <Box
          sx={{
            flex: 1,
            display: "flex",
            flexDirection: "column",
            gap: 2,
            mt: 5,
          }}
        >
          <Box sx={{ display: "flex", gap: 1, justifyContent: "center" }}>
            <Label intld="auth.dontHaveAnAccount" />
            <LineButton
              intld="auth.signUp"
              onClick={() => navigate(appPaths.REGISTER)}
            />
          </Box>
          <Divider>
            <Label intld="or" />
          </Divider>
          <Box>
            <GoogleLoginButton onClick={store.loginGoogle} />
          </Box>
        </Box>
      </Box>
    </AuthBox>
  );
});

export default Login;
