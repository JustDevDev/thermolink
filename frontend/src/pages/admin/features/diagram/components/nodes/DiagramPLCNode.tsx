import { gray, green } from "@/utils/theme/themePrimitives";
import { Box } from "@mui/material";
import { useNodeConnections } from "@xyflow/react";
import { observer } from "mobx-react-lite";
import { useUserStore } from "@/hooks/stores/useUserStore";
import Label from "@/components/basic-components/typography/Label";
import { useEffect, useMemo } from "react";
import DiagramNodeHeader from "./DiagramNodeHeader";
import DiagramPLCNodeHandle from "./DiagramPLCNodeHandle";
import DiagramPLCNodeStore from "../../stores/DiagramPLCNodeStore";
import NetworkCheckIcon from "@mui/icons-material/NetworkCheck";
import TextField from "@/components/basic-components/field/TextField";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import { Edit } from "@mui/icons-material";

export type DiagramPLCNodeData = {
  name: string;
  editMode: boolean;
};

const plcNodeStore = new DiagramPLCNodeStore();

const DiagramPLCNode = observer(
  ({
    data,
    id,
    selected,
  }: {
    data: DiagramPLCNodeData;
    id: string;
    selected?: boolean;
  }) => {
    const { editMode } = data;
    const userStore = useUserStore();
    const connections = useNodeConnections({ handleType: "target" });
    const isLight = userStore.getTheme === "light";

    const handles = useMemo(() => [0, 1, 2, 3], []); // Wrapped in useMemo

    useEffect(() => {
      const intervals = handles.map((_, index) => {
        const randomInterval =
          Math.floor(Math.random() * (3000 - 1500 + 1)) + 1500;
        return setInterval(
          () => plcNodeStore.updateChartData(index),
          randomInterval
        );
      });

      return () => intervals.forEach(clearInterval);
    }, [handles]);

    const isHandleConnected = (handleId: string) =>
      connections.some((conn) => conn.targetHandle === handleId);

    return (
      <Box
        sx={{
          backgroundColor: isLight ? "#fdfdfc" : gray[800],
          border: `1px solid ${
            selected ? gray[400] : isLight ? gray[100] : gray[700]
          }`,
          borderRadius: 1,
          minWidth: 220,
          minHeight: 230,
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
                : green[100]
              : data.editMode
              ? gray[400]
              : green[300]
          }
        >
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <NetworkCheckIcon
              sx={{ fontSize: "18px", color: gray[900], mr: 1 }}
            />
            <Label intld={"diagram.plc"} sx={{ color: gray[900] }} />
          </Box>
        </DiagramNodeHeader>
        <Box
          sx={{
            padding: "1rem 0.5rem 0 0.5rem",
            display: "flex",
            alignItems: "center",
            minHeight: 40,
          }}
        >
          {data.editMode ? (
            <TextField
              value={data.name}
              onChange={(value) => {
                data.name = value;
              }}
              label="placeHolder.plcName"
            />
          ) : (
            <Box
              sx={{
                paddingBottom: "0.8rem",
                display: "flex",
                gap: 1,
                alignItems: "center",
              }}
            >
              <Edit sx={{ fontSize: "18px", color: gray[500] }} />
              <Paragraph>{data.name}</Paragraph>
            </Box>
          )}
        </Box>

        <Box
          sx={{
            backgroundColor: isLight ? "#fdfdfc" : gray[800],
            minHeight: 120,
            position: "relative",
          }}
        >
          {handles.map((index) => {
            const handleId = `port-${index}`;
            return (
              <DiagramPLCNodeHandle
                key={handleId}
                handleId={handleId}
                index={index}
                isConnected={isHandleConnected(handleId)}
                editMode={editMode}
                isLight={isLight}
                chartData={plcNodeStore.data[index]}
              />
            );
          })}
        </Box>
      </Box>
    );
  }
);

export default DiagramPLCNode;
