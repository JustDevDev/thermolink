import { Box, IconButton } from "@mui/material";
import { red } from "@/utils/theme/themePrimitives";
import CloseIcon from "@mui/icons-material/Close";
import { useDiagramStore } from "../../hooks/useDiagramStore";

type NodeHeaderProps = {
  children: React.ReactNode;
  nodeId: string;
  backgroundColor: string;
  selected?: boolean;
};

const DiagramNodeHeader = ({
  children,
  nodeId,
  backgroundColor,
  selected,
}: NodeHeaderProps) => {
  const diagramStore = useDiagramStore();

  const handleDelete = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (diagramStore.editMode) {
      diagramStore.removeNode(nodeId);
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        padding: "0.5rem",
        justifyContent: "space-between",
        backgroundColor,
        borderTopLeftRadius: "0.4rem",
        borderTopRightRadius: "0.4rem",
        position: "relative",
      }}
    >
      {children}
      {diagramStore.editMode && selected && (
        <IconButton
          size="small"
          onClick={handleDelete}
          sx={{
            position: "absolute",
            top: -10,
            right: -10,
            backgroundColor: red[400],
            "&:hover": {
              backgroundColor: red[300],
            },
          }}
        >
          <CloseIcon sx={{ fontSize: "10px", color: "white" }} />
        </IconButton>
      )}
    </Box>
  );
};

export default DiagramNodeHeader;
