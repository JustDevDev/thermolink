import { observer } from "mobx-react-lite";
import { useDiagramStore } from "../hooks/useDiagramStore";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { Box, Collapse } from "@mui/material";
import { gray } from "@/utils/theme/themePrimitives";
import AppsIcon from "@mui/icons-material/Apps";
import EditIcon from "@mui/icons-material/Edit";
import SaveIcon from "@mui/icons-material/Save";
import { useState } from "react";
import DiagramMenuItems from "./DiagramMenuItems";

type DiagramMenuProps = {
  getCanvasCenterPosition: () => { x: number; y: number };
};

const DiagramMenu = observer(
  ({ getCanvasCenterPosition }: DiagramMenuProps) => {
    const store = useDiagramStore();
    const userStore = useUserStore();
    const isLight = userStore.getTheme === "light";
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
    const [open, setOpen] = useState(false);

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
      setAnchorEl(event.currentTarget);
      setOpen(true);
    };

    const handleClose = () => {
      setAnchorEl(null);
      setOpen(false);
    };

    return (
      <Box
        sx={{
          position: "absolute",
          top: "1rem",
          left: "15px",
          zIndex: 1000,
          backgroundColor: isLight ? "#fdfdfc" : gray[800],
          border: `1px solid ${isLight ? gray[100] : gray[700]}`,
          borderRadius: 1,
          display: "flex",
          flexDirection: "column",
          overflow: "hidden",
        }}
      >
        <Box
          onClick={store.toggleEditMode}
          sx={{
            padding: "8px 7px",
            cursor: "pointer",
            transition: "all 0.3s ease",
            "&:hover": {
              backgroundColor: isLight ? gray[50] : gray[700],
            },
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          {store.editMode ? (
            <SaveIcon sx={{ fontSize: "18px" }} />
          ) : (
            <EditIcon sx={{ fontSize: "18px" }} />
          )}
        </Box>
        <Collapse in={store.editMode} timeout={300}>
          <Box
            component="button"
            sx={{
              padding: "10px 8px",
              borderTop: `1px solid ${isLight ? gray[100] : gray[700]}`,
              cursor: "pointer",
              transition: "all 0.3s ease",
              "&:hover": {
                backgroundColor: isLight ? gray[50] : gray[700],
              },
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              background: "none",
              border: "none",
              width: "100%",
            }}
            onClick={handleClick}
          >
            <AppsIcon sx={{ fontSize: "18px" }} />
          </Box>
          <DiagramMenuItems
            anchorEl={anchorEl}
            open={open}
            onClose={handleClose}
            getCanvasCenterPosition={getCanvasCenterPosition}
          />
        </Collapse>
      </Box>
    );
  }
);

export default DiagramMenu;
