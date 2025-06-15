import { Box, Divider } from "@mui/material";
import Label from "@/components/basic-components/typography/Label";
import { gray } from "@/utils/theme/themePrimitives";
import { useAppTranslation } from "@/utils/translate/translate";

type MetricBoxProps = {
  label: string;
  value: number | string;
  isLight: boolean;
  isEditMode: boolean;
};

const MetricBox = ({ label, value, isLight, isEditMode }: MetricBoxProps) => (
  <Box
    sx={{
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      minWidth: 100,
    }}
  >
    <Label intld={label} />
    <Label
      sx={{
        color: isLight ? gray[900] : gray[50],
        fontWeight: "bold",
      }}
    >
      {value && !isEditMode
        ? `${value}${typeof value === "number" ? "°C" : ""}`
        : "-"}
    </Label>
  </Box>
);

type DiagramSensorNodeMetricsProps = {
  temperature: number;
  averageTemperature: number;
  condition: string;
  isLight: boolean;
  isEditMode: boolean;
};

const DiagramSensorNodeMetrics = ({
  temperature,
  averageTemperature,
  condition,
  isLight,
  isEditMode,
}: DiagramSensorNodeMetricsProps) => {
  const { t } = useAppTranslation();

  return (
    <>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          padding: "0 0.5rem",
          marginTop: "0.5rem",
          gap: 2,
        }}
      >
        <MetricBox
          label="diagram.actualTemperature"
          value={temperature}
          isLight={isLight}
          isEditMode={isEditMode}
        />
        <Divider orientation="vertical" variant="middle" flexItem />
        <MetricBox
          label="diagram.averageTemperature"
          value={averageTemperature}
          isLight={isLight}
          isEditMode={isEditMode}
        />
      </Box>
      <Divider variant="middle" />
      <MetricBox
        label="diagram.condition"
        value={
          condition !== null
            ? t(`diagram.${condition.toUpperCase()}`)
            : t(`error.sensorNoData`)
        }
        isLight={isLight}
        isEditMode={isEditMode}
      />
    </>
  );
};

export default DiagramSensorNodeMetrics;
