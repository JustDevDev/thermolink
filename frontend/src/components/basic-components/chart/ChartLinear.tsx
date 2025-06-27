import { LineChart } from "@mui/x-charts/LineChart";
import { observer } from "mobx-react-lite";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { ColorTheme } from "@/stores/setting/settingStore";
import { brand, gray } from "@/utils/theme/themePrimitives";
import { useAppTranslation } from "@/utils/translate/translate";
import ChartLoading from "@/components/basic-components/loading/ChartLoading";
import { Box } from "@mui/material";

export type ChartLinearSeriesModel = {
  id: string;
  label: string;
  showMark: boolean;
  curve:
    | "catmullRom"
    | "linear"
    | "monotoneX"
    | "monotoneY"
    | "natural"
    | "step"
    | "stepBefore"
    | "stepAfter";
  stack?: string;
  area?: boolean;
  stackOrder: "ascending" | "descending" | "none" | "reverse";
  data: number[];
  valueFormatter?: (value: number | null) => string;
};

export type ChartLinearProps = {
  series: ChartLinearSeriesModel[];
  xAxisData?: string[];
  hiddenLegend?: boolean;
  loading?: boolean;
};

const ChartLinear = observer((props: ChartLinearProps) => {
  const { series, xAxisData, hiddenLegend = false, loading = false } = props;
  const data = xAxisData && xAxisData.length > 0 ? xAxisData : [];

  const userStore = useUserStore();
  const { t } = useAppTranslation();

  return (
    <Box sx={{ position: "relative" }}>
      <LineChart
        colors={[brand[200], brand[400], brand[600], brand[800]]}
        xAxis={[
          {
            scaleType: "point",
            data,
            tickInterval: undefined,
          },
        ]}
        series={series}
        height={250}
        margin={{ left: 50, right: 20, top: 20, bottom: 20 }}
        grid={{ horizontal: true }}
        sx={{
          "& .MuiChartsAxis-left .MuiChartsAxis-tickLabel": {
            fill: userStore.theme === ColorTheme.LIGHT ? gray[600] : gray[400],
          },
          "& .MuiChartsAxis-bottom .MuiChartsAxis-tickLabel": {
            fill: userStore.theme === ColorTheme.LIGHT ? gray[600] : gray[400],
          },
          "& .MuiChartsAxis-bottom .MuiChartsAxis-line": {
            stroke:
              userStore.theme === ColorTheme.LIGHT ? gray[100] : gray[700],
            strokeWidth: 0.4,
          },
          "& .MuiChartsAxis-left .MuiChartsAxis-line": {
            stroke:
              userStore.theme === ColorTheme.LIGHT ? gray[100] : gray[700],
          },
          "& .MuiChartsAxis-tick": {
            stroke:
              userStore.theme === ColorTheme.LIGHT ? gray[100] : gray[700],
          },
          "& .MuiChartsGrid-line": {
            stroke:
              userStore.theme === ColorTheme.LIGHT ? gray[600] : gray[400],
            strokeWidth: 0.2,
            strokeDasharray: "4 4",
          },
        }}
        slotProps={{
          legend: {
            hidden: hiddenLegend,
          },
          noDataOverlay: {
            message: t("chart.noData"),
          },
        }}
      />
      {loading && <ChartLoading />}
    </Box>
  );
});

export default ChartLinear;
