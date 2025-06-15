import { useEffect } from "react";
import { useForgottenPasswordStore } from "../hooks/useForgottenPasswordStore";
import { useNavigate } from "react-router-dom";
import { appPaths } from "@/routers/app/appPath";
import { observer } from "mobx-react-lite";
import { Box } from "@mui/material";
import Label from "@/components/basic-components/typography/Label";
import { PasswordField } from "@/components/basic-components/field/PasswordField";
import {
  checkPasswordLength,
  containsSpecialCharacter,
} from "@/pages/register/RegisterUtils";
import LoadingButton from "@/components/basic-components/button/LoadingButton";
import Title from "@/components/basic-components/typography/Title";
import { FieldErrorNotice } from "@/components/common-components/field-error-notice/FieldErrorNotice";
import { useFieldErrorNoticeStore } from "@/hooks/stores/useFieldErrorNoticeStore";
import { ResponseForgottenPasswordNewType } from "@/types/api/responseTypes";
import { alertManager } from "@/utils/alert/AlertManager";
import CircleIcon from "@mui/icons-material/Circle";

const ForgottenPasswordNewPassword = observer(
  ({ token }: { token: string }) => {
    const store = useForgottenPasswordStore();
    const navigate = useNavigate();
    const FieldErrorNoticeStore = useFieldErrorNoticeStore();

    useEffect(() => {
      const validateToken = async () => {
        try {
          await store.requestValidateToken(token);
        } catch (error) {
          console.error("Token validation failed:", error);
          navigate(appPaths.LOGIN);
        }
      };

      validateToken();
    }, [store, token, navigate]);

    const handleSavePassword = async () => {
      if (FieldErrorNoticeStore.checkRequiredValues()) {
        const error = store.checkPassword();
        if (error) {
          FieldErrorNoticeStore.setCollapse(true, error);
          return;
        }

        const response: ResponseForgottenPasswordNewType =
          await store.requestNewPassword(token);
        if (response.error) {
          FieldErrorNoticeStore.setCollapse(true, `error.${response.error}`);
          return;
        }
        FieldErrorNoticeStore.setCollapse(false, "");
        FieldErrorNoticeStore.clearRequiredValues();
        alertManager.success({
          intld: "success.yourPasswordSuccessfullyChanged",
          duration: 4000,
        });
        setTimeout(() => {
          navigate(appPaths.LOGIN);
        }, 4000);
      } else {
        FieldErrorNoticeStore.setCollapse(true, "error.fillAllFields");
      }
    };

    return (
      <Box sx={{ mt: 2 }}>
        <Title
          intld="title.forgottenPassword"
          variant="h4"
          sx={{ fontWeight: "700", textAlign: "left" }}
        />
        <FieldErrorNotice message={FieldErrorNoticeStore.errorMessage} />

        <Box sx={{ mt: 2 }}>
          <Label
            intld="label.newPassword"
            sx={{ marginBottom: "6px", textAlign: "left" }}
          />
          <PasswordField
            required
            fullWidth={true}
            placeholder="placeHolder.password"
            value={store.newPassword}
            showIcon={false}
            onChange={(e) => (store.newPassword = e)}
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
            value={store.repeatPassword}
            id={"repeatPassword"}
            onChange={(e) => (store.repeatPassword = e)}
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
                color: checkPasswordLength(store.newPassword)
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
                color: containsSpecialCharacter(store.newPassword)
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
              intld="button.changePassword"
              onClick={handleSavePassword}
              fullwidth={true}
              loading={store.forgottenPasswordNewProvider.loading}
            />
          </Box>
        </Box>
      </Box>
    );
  }
);

export default ForgottenPasswordNewPassword;
