import { observer } from "mobx-react-lite";
import { useDashboardStore } from "../hooks/useDashboardStore";
import ChartLinear from "@/components/basic-components/chart/ChartLinear";
import DashboardChartLinearContent from "./DashboardChartLinearContent";
import { format } from "date-fns";
import { Card, CardContent } from "@mui/material";
import SubTitle from "@/components/basic-components/typography/SubTitle";

const DashboardChartLinear = observer(() => {
  const store = useDashboardStore();

  const sensorsWithData = store.plc.connectedSensors
    .filter((sensor) => sensor.temperatures.length > 0)
    .slice()
    .sort((a, b) => a.port - b.port);

  const xAxisData: string[] =
    sensorsWithData[0]?.temperatures
      .slice()
      .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
      .map((t) => format(new Date(t.date), "dd.MM HH:mm")) ?? [];

  const series = sensorsWithData.map((sensor) => ({
    id: `${sensor.id}-port${sensor.port}`,
    label: sensor.place,
    showMark: false,
    curve: "linear" as const,
    stackOrder: "ascending" as const,
    data: sensor.temperatures
      .slice()
      .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
      .map((item) => item.temperature),
    valueFormatter: (value: number | null) => `${value}°C`,
  }));

  return (
    <Card variant="outlined" sx={{ width: "100%" }}>
      <CardContent>
        <SubTitle
          variant="subtitle1"
          intld={"dashboard.temperatureChartTitle"}
          sx={{ fontWeight: "bold" }}
        />
        <DashboardChartLinearContent />
        <ChartLinear
          series={series}
          xAxisData={xAxisData}
          hiddenLegend
          loading={store.chartsPLCsProvider.loading}
        />
      </CardContent>
    </Card>
  );
});

export default DashboardChartLinear;
