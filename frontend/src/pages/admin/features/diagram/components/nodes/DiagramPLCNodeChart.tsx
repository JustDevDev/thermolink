import { LineChart } from "@mui/x-charts";
import { gray } from "@/utils/theme/themePrimitives";

type PLCNodeChartProps = {
  data: number[];
  isLight: boolean;
};

const DiagramPLCNodeChart = ({ data, isLight }: PLCNodeChartProps) => (
  <LineChart
    xAxis={[
      {
        data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        disableLine: true,
        tickSize: 0,
        min: 1,
        max: 10,
      },
    ]}
    yAxis={[
      {
        disableLine: true,
        tickSize: 0,
        min: 0,
      },
    ]}
    series={[
      {
        data: data,
        showMark: false,
        color: isLight ? gray[500] : gray[400],
        area: false,
      },
    ]}
    width={100}
    height={20}
    margin={{ left: 0, right: 0, top: 0, bottom: 0 }} // Upraveno z negativních hodnot
    disableAxisListener
    sx={{
      borderRadius: 0.6,
      overflow: "hidden",
      "& .MuiLineElement-root": {
        strokeWidth: 0.5,
      },
      "& .MuiChartsAxis-root": {
        display: "none",
      },
      "& .MuiChartsLegend-root": {
        display: "none",
      },
      "& .MuiChartsContainer-root": {
        width: "100%",
        height: "100%",
      },
    }}
  />
);

export default DiagramPLCNodeChart;
