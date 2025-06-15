import { useFieldErrorNoticeStore } from "@/hooks/stores/useFieldErrorNoticeStore";
import { observer } from "mobx-react-lite";
import { useEffect } from "react";
import { useForgottenPasswordStore } from "../hooks/useForgottenPasswordStore";
import { ResponseForgottenPasswordType } from "@/types/api/responseTypes";
import { alertManager } from "@/utils/alert/AlertManager";
import { Box } from "@mui/material";
import Title from "@/components/basic-components/typography/Title";
import { FieldErrorNotice } from "@/components/common-components/field-error-notice/FieldErrorNotice";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import Label from "@/components/basic-components/typography/Label";
import LoadingButton from "@/components/basic-components/button/LoadingButton";
import TextField from "@/components/basic-components/field/TextField";

const ForgottenPasswordReset = observer(() => {
  const FieldErrorNoticeStore = useFieldErrorNoticeStore();
  const store = useForgottenPasswordStore();

  const handleLogin = async () => {
    if (FieldErrorNoticeStore.checkRequiredValues()) {
      const error = store.checkEmail();
      if (error) {
        FieldErrorNoticeStore.setCollapse(true, error);
        return;
      }

      const response: ResponseForgottenPasswordType =
        await store.requestPasswordReset();
      if (response.error) {
        FieldErrorNoticeStore.setCollapse(true, `error.${response.error}`);
        return;
      }
      FieldErrorNoticeStore.setCollapse(false, "");
      FieldErrorNoticeStore.clearRequiredValues();
      alertManager.success({
        intld: "success.forgottenPassowrdSuccessMessage",
        persistent: true,
      });
      store.resetData();
    } else {
      FieldErrorNoticeStore.setCollapse(true, "error.fillAllFields");
    }
  };

  useEffect(() => {
    return () => {
      FieldErrorNoticeStore.setCollapse(false, "");
      FieldErrorNoticeStore.clearRequiredValues();
    };
  }, [FieldErrorNoticeStore]);

  return (
    <Box sx={{ mt: 2 }}>
      <Title
        intld="title.forgottenPassword"
        variant="h4"
        sx={{ fontWeight: "700", textAlign: "left" }}
      />
      <FieldErrorNotice message={FieldErrorNoticeStore.errorMessage} />
      <Box sx={{ mt: 3 }}>
        <Paragraph
          intld="text.EnterEmailAddressAccoviatedWithYourAccount"
          sx={{ marginBottom: "6px", textAlign: "left" }}
        />
      </Box>
      <Box sx={{ mt: 3 }}>
        <Label
          intld="label.email"
          sx={{ marginBottom: "6px", textAlign: "left" }}
        />
        <TextField
          required
          fullWidth={true}
          value={store.email}
          onChange={(e) => (store.email = e)}
          placeholder="placeHolder.email"
        />
      </Box>
      <Box sx={{ mt: 5 }}>
        <LoadingButton
          intld="button.resetPassword"
          onClick={handleLogin}
          fullwidth={true}
          loading={store.forgottenPasswordProvider.loading}
        />
      </Box>
    </Box>
  );
});

export default ForgottenPasswordReset;
