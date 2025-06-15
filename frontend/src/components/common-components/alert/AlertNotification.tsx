import { Alert as MuiAlert, Snackbar } from "@mui/material";
import { observer } from "mobx-react-lite";
import { alertManager } from "@/utils/alert/AlertManager";

export const Alert = observer(() => {
  const handleClose = (id: string) => {
    alertManager.removeAlert(id);
  };

  return (
    <>
      {alertManager.alerts.map((alert) => (
        <Snackbar
          key={alert.id}
          open={true}
          onClose={() => handleClose(alert.id)}
          anchorOrigin={{ vertical: "top", horizontal: "center" }}
        >
          <MuiAlert
            onClose={() => handleClose(alert.id)}
            severity={alert.type}
            variant="filled"
            sx={{
              width: "100%",
              borderRadius: 1,
              boxShadow: (theme) =>
                theme.palette.mode === "light"
                  ? "0px 4px 8px rgba(0, 0, 0, 0.05)"
                  : "none",
              border: "none",
              "& .MuiAlert-icon": {
                opacity: 0.8,
                marginRight: "8px",
              },
              "& .MuiAlert-message": {
                padding: "4px 0",
                display: "flex",
                alignItems: "center",
              },
              "& .MuiAlert-action": {
                padding: "0px",
                marginRight: "4px",
                alignItems: "center",
                "& .MuiIconButton-root": {
                  padding: "0 0 0 4px",
                  border: "none",
                  "& svg": {
                    fontSize: "18px",
                  },
                  "&:hover": {
                    backgroundColor: "transparent",
                    opacity: 0.8,
                  },
                },
              },
            }}
          >
            {alert.message}
          </MuiAlert>
        </Snackbar>
      ))}
    </>
  );
});
