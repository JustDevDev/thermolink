import { Box, Divider, Dialog as MuiDialog } from "@mui/material";
import Title from "../typography/Title";
import CloseIcon from "@mui/icons-material/Close";
import IconButton from "../button/IconButton";

interface DialogProps {
  open: boolean;
  onClose: () => void;
  children: React.ReactNode;
  title: string;
  icon?: React.ReactNode;
  closeIcon?: boolean;
}

const Dialog: React.FC<DialogProps> = ({
  open,
  onClose,
  children,
  title,
  icon,
  closeIcon,
}) => {
  return (
    <MuiDialog
      onClose={onClose}
      open={open}
      PaperProps={{
        sx: { padding: "1rem" },
      }}
    >
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          marginBottom: 1,
        }}
      >
        <Box sx={{ display: "flex", gap: 1, alignItems: "center" }}>
          {icon && icon}
          {title && (
            <Title intld={title} variant="h6" sx={{ fontWeight: 600 }} />
          )}
        </Box>
        {closeIcon && (
          <IconButton onClick={onClose}>
            <CloseIcon sx={{ fontSize: "17px" }} />
          </IconButton>
        )}
      </Box>
      <Divider sx={{ marginBottom: 2 }} />
      {children}
    </MuiDialog>
  );
};

export default Dialog;
