import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";
import SubTitle from "../typography/SubTitle";
import Label from "../typography/Label";
import { Avatar as MuiAvatar } from "@mui/material";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { truncateText } from "@/utils/text/truncateText";

type TAvatarProps = {
  size?: number;
  onlyName?: boolean;
  column?: boolean;
};

const Avatar = observer(
  ({ size = 24, onlyName = false, column = false }: TAvatarProps) => {
    const userStore = useUserStore();
    const firstName = userStore.userFirstName;
    const lastName = userStore.userLastName;
    const email = userStore.userEmail;
    const avatar = userStore.avatar;

    const initial =
      (firstName && firstName[0]?.toUpperCase()) ||
      (lastName && lastName[0]?.toUpperCase()) ||
      (email && email[0]?.toUpperCase()) ||
      "";
    const displayName = truncateText(firstName || lastName || email || "");

    return (
      <Box
        sx={{
          display: "flex",
          gap: 1,
          alignItems: "center",
          flexDirection: column ? "column" : "row",
        }}
      >
        <MuiAvatar sx={{ width: size, height: size }} src={avatar}>
          {initial}
        </MuiAvatar>
        <Box>
          {onlyName ? (
            <SubTitle variant="subtitle2">{displayName}</SubTitle>
          ) : (
            <>
              {(firstName || lastName) && (
                <SubTitle variant="subtitle2">
                  {truncateText(
                    firstName && lastName
                      ? `${firstName} ${lastName}`
                      : lastName || firstName || ""
                  )}
                </SubTitle>
              )}
              {email && <Label>{truncateText(email)}</Label>}
            </>
          )}
        </Box>
      </Box>
    );
  }
);

export default Avatar;
