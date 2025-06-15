import { Box } from "@mui/material";
import { gray, green, red } from "@/utils/theme/themePrimitives";
import Label from "@/components/basic-components/typography/Label";
import FlashOffIcon from "@mui/icons-material/FlashOff";
import FlashOnIcon from "@mui/icons-material/FlashOn";
import ThermostatIcon from "@mui/icons-material/Thermostat";

type DiagramSensorNodeHeaderProps = {
  name: string;
  isLight: boolean;
  isConnected: boolean;
  editMode: boolean;
};

const DiagramSensorNodeHeader = ({
  name,
  isLight,
  isConnected,
  editMode,
}: DiagramSensorNodeHeaderProps) => (
  <Box
    sx={{
      display: "flex",
      alignItems: "center",
      width: "100%",
      padding: "0.5rem",
      justifyContent: "space-between",
      backgroundColor: isLight
        ? editMode
          ? gray[100]
          : isConnected
          ? green[100]
          : red[100]
        : editMode
        ? gray[400]
        : isConnected
        ? green[300]
        : red[300],
      borderTopLeftRadius: "0.3rem",
      borderTopRightRadius: "0.3rem",
    }}
  >
    <Box sx={{ display: "flex", alignItems: "center" }}>
      <ThermostatIcon sx={{ fontSize: "18px", color: gray[900] }} />
      <Label intld={name} sx={{ color: gray[900] }} />
    </Box>
    {editMode ? (
      <FlashOffIcon sx={{ fontSize: "18px", color: gray[900] }} />
    ) : isConnected ? (
      <FlashOnIcon sx={{ fontSize: "18px", color: green[500] }} />
    ) : (
      <FlashOffIcon sx={{ fontSize: "18px", color: red[900] }} />
    )}
  </Box>
);

export default DiagramSensorNodeHeader;
