import { observer } from "mobx-react-lite";
import { useDashboardStore } from "../hooks/useDashboardStore";
import ChartLinear from "@/components/basic-components/chart/ChartLinear";
import DashboardChartLinearContent from "./DashboardChartLinearContent";
import { format } from "date-fns";
import { Card, CardContent } from "@mui/material";
import SubTitle from "@/components/basic-components/typography/SubTitle";

const DashboardChartLinear = observer(() => {
  const store = useDashboardStore();
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
          series={store.plc.connectedSensors
            .filter((sensor) => sensor.temperatures.length > 0)
            .slice()
            .sort((a, b) => a.port - b.port)
            .map((sensor) => ({
              id: `${sensor.id}-port${sensor.port}`,
              label: sensor.place,
              showMark: false,
              curve: "linear" as const,
              stackOrder: "ascending" as const,
              data: sensor.temperatures
                .slice()
                .sort(
                  (a, b) =>
                    new Date(a.date).getTime() - new Date(b.date).getTime()
                )
                .map((item) => item.temperature),
              valueFormatter: (value) => `${value}°C`,
            }))}
          xAxisData={
            store.plc.connectedSensors[0]?.temperatures
              .map((t) => new Date(t.date))
              .sort((a, b) => a.getTime() - b.getTime())
              .map((d) => format(d, "HH:mm")) ?? []
          }
          hiddenLegend
          loading={store.chartsPLCsProvider.loading}
        />
      </CardContent>
    </Card>
  );
});

export default DashboardChartLinear;
