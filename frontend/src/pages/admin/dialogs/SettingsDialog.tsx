import AvatarUpload from "@/components/basic-components/avatar/AvatarEdit";
import LoadingButton from "@/components/basic-components/button/LoadingButton";
import Dialog from "@/components/basic-components/dialog/Dialog";
import { PasswordField } from "@/components/basic-components/field/PasswordField";
import TextField from "@/components/basic-components/field/TextField";
import Label from "@/components/basic-components/typography/Label";
import SubTitle from "@/components/basic-components/typography/SubTitle";
import { FieldErrorNotice } from "@/components/common-components/field-error-notice/FieldErrorNotice";
import { useSideMenuStore } from "@/components/common-components/menu/hooks/useSideMenuStore";
import { useFieldErrorNoticeStore } from "@/hooks/stores/useFieldErrorNoticeStore";
import { useUserStore } from "@/hooks/stores/useUserStore";
import {
  checkPasswordLength,
  containsSpecialCharacter,
} from "@/pages/register/RegisterUtils";
import { ResponseUserUpdateTypes } from "@/types/api/responseTypes";
import CircleIcon from "@mui/icons-material/Circle";
import SettingsIcon from "@mui/icons-material/Settings";
import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";
import { useState } from "react";

const checkPassword = (newPassowrd: string, currentPassword: string) => {
  if (newPassowrd.length > 0 && currentPassword.length === 0) {
    return "error.currentPasswordIsnotField";
  }
  if (currentPassword.length > 0 && !checkPasswordLength(newPassowrd)) {
    return "error.passwordLength";
  }
  if (currentPassword.length > 0 && !containsSpecialCharacter(newPassowrd)) {
    return "error.passwordSpecialCharacter";
  }
};

const SettingsDialog = observer(() => {
  const sideMenuStore = useSideMenuStore();
  const store = useUserStore();
  const FieldErrorNoticeStore = useFieldErrorNoticeStore();

  const [firstName, setFirstName] = useState<string>(store.userFirstName);
  const [lastName, setLastName] = useState<string>(store.userLastName);
  const [avatar, setAvatar] = useState<string | undefined>(store.avatar);
  const [avatarFileSize, setAvatarFileSize] = useState<number>(0);
  const [currentPassword, setCurrentPassword] = useState<string>("");
  const [newPassword, setNewPassword] = useState<string>("");

  const resetValues = () => {
    setFirstName(store.userFirstName);
    setLastName(store.userLastName);
    setAvatar(store.avatar);
    setCurrentPassword("");
    setNewPassword("");
  };

  const handleSave = async () => {
    if (FieldErrorNoticeStore.checkRequiredValues()) {
      const error =
        checkPassword(newPassword, currentPassword) ||
        (avatarFileSize > 2000000 && "error.avatarFileTooBig");

      if (error) {
        FieldErrorNoticeStore.setCollapse(true, error);
        return;
      }

      const response: ResponseUserUpdateTypes | undefined =
        await store.updateUserData({
          userAvatar: avatar,
          userFirstName: firstName,
          userLastName: lastName,
          userCurrentPassword: currentPassword,
          userNewPassword: newPassword,
        });
      if (response.error) {
        FieldErrorNoticeStore.setCollapse(true, `error.${response.error}`);
      } else {
        sideMenuStore.setSettingDialogOpen(false);
        resetValues();
      }
    } else {
      FieldErrorNoticeStore.setCollapse(true, "error.fillAllFields");
    }
  };

  const closeDialogHandle = () => {
    sideMenuStore.setSettingDialogOpen(false);
    resetValues();
    FieldErrorNoticeStore.setCollapse(false, "");
  };

  return (
    <Dialog
      open={sideMenuStore.isSettingDialogOpen}
      onClose={closeDialogHandle}
      title="menu.settings"
      icon={<SettingsIcon />}
      closeIcon
    >
      <Box>
        <FieldErrorNotice message={FieldErrorNoticeStore.errorMessage} />
      </Box>
      <SubTitle
        intld="subTitle.basicInformation"
        variant="subtitle1"
        sx={{ fontWeight: "bold", marginBottom: 1 }}
      />
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          marginBottom: 2,
          flexDirection: "column",
          alignItems: "center",
          gap: 2,
        }}
      >
        <AvatarUpload
          src={avatar}
          onUpload={(base64, fileSize) => {
            setAvatar(base64);
            setAvatarFileSize(fileSize);
          }}
          size={70}
          initial={
            (firstName && firstName[0]?.toUpperCase()) ||
            (store.userEmail && store.userEmail[0]?.toUpperCase()) ||
            ""
          }
        />
        <Label>{store.userEmail || ""}</Label>
      </Box>
      <Box sx={{ display: "flex", gap: 2 }}>
        <Box sx={{ mt: 3 }}>
          <Label
            intld="label.firstName"
            sx={{ marginBottom: "6px", textAlign: "left" }}
          />
          <TextField
            fullWidth
            value={firstName || ""}
            onChange={(e) => setFirstName(e)}
            placeholder="placeHolder.firstName"
          />
        </Box>
        <Box sx={{ mt: 3 }}>
          <Label
            intld="label.lastName"
            sx={{ marginBottom: "6px", textAlign: "left" }}
          />
          <TextField
            fullWidth
            value={lastName || ""}
            onChange={(e) => setLastName(e)}
            placeholder="placeHolder.lastName"
          />
        </Box>
      </Box>
      <SubTitle
        intld="subTitle.changePassword"
        variant="subtitle1"
        sx={{ fontWeight: "bold", my: 2 }}
      />
      {store.authProvider === "local" ? (
        <>
          <Box>
            <Box>
              <Label
                intld="label.currentPassword"
                sx={{ marginBottom: "6px", textAlign: "left" }}
              />
              <PasswordField
                fullWidth
                placeholder="placeHolder.password"
                value={currentPassword}
                showIcon={false}
                onChange={(e) => setCurrentPassword(e)}
              />
            </Box>
            <Box sx={{ mt: 3 }}>
              <Label
                intld="label.newPassword"
                sx={{ marginBottom: "6px", textAlign: "left" }}
              />
              <PasswordField
                fullWidth
                placeholder="placeHolder.password"
                value={newPassword}
                showIcon={false}
                onChange={(e) => setNewPassword(e)}
              />
            </Box>
            <Box
              sx={{
                mt: 1,
                display: "flex",
                flexDirection: "column",
                gap: 1,
              }}
            >
              <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
                <CircleIcon
                  sx={(theme) => ({
                    color: checkPasswordLength(newPassword)
                      ? theme.palette.success.main
                      : theme.palette.error.main,
                    fontSize: "0.7rem",
                  })}
                />
                <Label
                  intld="error.passwordLength"
                  sx={{ textAlign: "left" }}
                />
              </Box>
              <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
                <CircleIcon
                  sx={(theme) => ({
                    color: containsSpecialCharacter(newPassword)
                      ? theme.palette.success.main
                      : theme.palette.error.main,
                    fontSize: "0.7rem",
                  })}
                />
                <Label
                  intld="error.specialCharacter"
                  sx={{ textAlign: "left" }}
                />
              </Box>
            </Box>
          </Box>
        </>
      ) : (
        <Box>
          <Label
            intld="text.youCannotChangePasswordForGoogle"
            sx={{ textAlign: "left" }}
          />
        </Box>
      )}

      <Box sx={{ mt: 5 }}>
        <LoadingButton
          intld="button.save"
          onClick={handleSave}
          fullwidth
          loading={store.userUpdateProvider.loading}
        />
      </Box>
    </Dialog>
  );
});

export default SettingsDialog;
