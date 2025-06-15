import { Box, Divider } from "@mui/material";
import { Handle, Position } from "@xyflow/react";
import SensorsOffIcon from "@mui/icons-material/SensorsOff";
import { gray, green } from "@/utils/theme/themePrimitives";
import DiagramPLCNodeChart from "./DiagramPLCNodeChart";

type DiagramPLCNodeHandleProps = {
  handleId: string;
  index: number;
  isConnected: boolean;
  editMode: boolean;
  isLight: boolean;
  chartData: number[];
};

const DiagramPLCNodeHandle = ({
  handleId,
  index,
  isConnected,
  editMode,
  isLight,
  chartData,
}: DiagramPLCNodeHandleProps) => {
  const topOffset = `${(index + 1) * 25}%`;

  return (
    <Box
      sx={{
        position: "absolute",
        top: topOffset,
        transform: "translateY(-50%)",
        display: "flex",
        alignItems: "center",
        zIndex: 10,
      }}
    >
      <Handle
        id={handleId}
        type="target"
        position={Position.Left}
        onConnect={(params) =>
          console.log(`handle ${handleId} onConnect`, params)
        }
        isConnectable={!isConnected}
        style={{
          borderRadius: 2,
          padding: "0.3rem 0.2rem",
          backgroundColor: editMode
            ? gray[300]
            : isConnected
            ? green[500]
            : gray[300],
        }}
      />

      <Box
        component="span"
        sx={{
          marginLeft: 2,
          fontSize: "0.8rem",
          userSelect: "none",
        }}
      >
        {!isConnected || editMode ? (
          <SensorsOffIcon sx={{ fontSize: "18px", color: gray[500] }} />
        ) : (
          <DiagramPLCNodeChart data={chartData} isLight={isLight} />
        )}
      </Box>
      <Divider />
    </Box>
  );
};

export default DiagramPLCNodeHandle;
