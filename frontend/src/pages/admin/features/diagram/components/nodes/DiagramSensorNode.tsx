import { gray, green, red } from "@/utils/theme/themePrimitives";
import { Box, Divider } from "@mui/material";
import { Handle, Position } from "@xyflow/react";
import { observer } from "mobx-react-lite";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { useDiagramStore } from "../../hooks/useDiagramStore";
import { MappedGooglePlace } from "@/types/mapperTypes";
import { DiagramPlaceDataProvider } from "../../stores/data/DiagramPlaceDataProvider";
import MultiDataSelect from "@/components/basic-components/select/MultiDataSelect";
import { useMemo } from "react";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import Label from "@/components/basic-components/typography/Label";
import DiagramNodeHeader from "./DiagramNodeHeader";
import DiagramSensorNodeMetrics from "./DiagramSensorNodeMetrics";
import DiagramSensorNodeStore from "../../stores/DiagramSensorNodeStore";
import { useAppTranslation } from "@/utils/translate/translate";
import FlashOffIcon from "@mui/icons-material/FlashOff";
import FlashOnIcon from "@mui/icons-material/FlashOn";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import ThermostatIcon from "@mui/icons-material/Thermostat";

export type DiagramSensorNodeData = {
  name: string;
  city: MappedGooglePlace;
  temperature: number;
  averageTemperature: number;
  condition: string;
  editMode: boolean;
};

const sensorNodeStore = new DiagramSensorNodeStore();

const flasIcon = (editMode: boolean, isConnected: boolean) => {
  if (editMode) {
    return <FlashOnIcon sx={{ fontSize: "18px", color: gray[500] }} />;
  } else if (isConnected) {
    return <FlashOnIcon sx={{ fontSize: "18px", color: green[500] }} />;
  } else {
    return <FlashOffIcon sx={{ fontSize: "18px", color: red[500] }} />;
  }
};

const DiagramSensorNode = observer(
  ({
    data,
    id,
    selected,
  }: {
    data: DiagramSensorNodeData;
    id: string;
    selected?: boolean;
  }) => {
    const userStore = useUserStore();
    const diagramStore = useDiagramStore();
    const isLight = userStore.getTheme === "light";
    const isConnected = diagramStore.isNodeConnected(id);
    const citiesProvider = useMemo(() => new DiagramPlaceDataProvider(), []);
    const { t } = useAppTranslation();

    const handleLocationChange = (location: MappedGooglePlace | null) => {
      if (!diagramStore.editMode) return;
      const updatedNodes = sensorNodeStore.updateNodeLocation(
        diagramStore.nodes,
        id,
        location
      );
      diagramStore.updateNodes(updatedNodes);
    };

    return (
      <>
        <Box
          sx={{
            backgroundColor: isLight ? "#fdfdfc" : gray[800],
            border: `1px solid ${
              selected ? gray[400] : isLight ? gray[100] : gray[700]
            }`,
            borderRadius: 1,
            color: isLight ? gray[900] : gray[50],
            display: "flex",
            flexDirection: "column",
            gap: 1,
            minWidth: 270,
            minHeight: 190,
            transition: "border-color 0.2s ease",
          }}
        >
          <DiagramNodeHeader
            nodeId={id}
            selected={selected}
            backgroundColor={
              isLight
                ? data.editMode
                  ? gray[100]
                  : isConnected
                  ? green[100]
                  : red[100]
                : data.editMode
                ? gray[400]
                : isConnected
                ? green[300]
                : red[300]
            }
          >
            <Box sx={{ display: "flex", alignItems: "center" }}>
              <ThermostatIcon sx={{ fontSize: "18px", color: gray[900] }} />
              <Label intld={data.name} sx={{ color: gray[900] }} />
            </Box>
            {flasIcon(data.editMode, isConnected)}
          </DiagramNodeHeader>

          <Box
            sx={{
              padding: "1rem 0.5rem",
              display: "flex",
              flexDirection: "column",
              gap: 1,
            }}
          >
            <Box
              sx={{
                display: "flex",
                gap: 1,
                alignItems: "center",
                minHeight: 40,
              }}
            >
              {data.editMode ? (
                <MultiDataSelect
                  provider={citiesProvider}
                  value={data.city}
                  onChange={handleLocationChange}
                  getOptionLabel={(option) => option.city}
                  label="label.city"
                  placeholder="placeHolder.city"
                  disabled={!data.editMode}
                />
              ) : (
                <>
                  <LocationOnIcon sx={{ fontSize: "18px", color: gray[500] }} />
                  <Paragraph>
                    {data.city.id.length > 0
                      ? data.city.city
                      : t("text.cityIsNotSet")}
                  </Paragraph>
                </>
              )}
            </Box>
            <Divider />
            <DiagramSensorNodeMetrics
              temperature={data.temperature}
              averageTemperature={data.averageTemperature}
              condition={data.condition}
              isLight={isLight}
              isEditMode={data.editMode}
            />
          </Box>
        </Box>
        <Handle
          type="source"
          position={Position.Right}
          isConnectable={data.city.city.length > 0}
          style={{
            borderRadius: 2,
            padding: "0.3rem 0.2rem",
            backgroundColor: data.city.city.length > 0 ? undefined : gray[300],
            cursor: data.city.city.length > 0 ? "pointer" : "not-allowed",
          }}
        />
      </>
    );
  }
);

export default DiagramSensorNode;
