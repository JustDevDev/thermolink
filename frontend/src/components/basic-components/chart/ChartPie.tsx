import { PieChart, useDrawingArea } from "@mui/x-charts";
import { styled, useMediaQuery, useTheme } from "@mui/material";
import { brand } from "@/utils/theme/themePrimitives";
import { TriggerOptions } from "@mui/x-charts/ChartsTooltip/utils";

export type ChartPieDataModel = {
  label: string;
  value: number;
};

type BaseProps = {
  data: ChartPieDataModel[];
  hiddenLegend?: boolean;
  tooltip?: TriggerOptions;
  colors?: string[];
};

export type ChartPieProps =
  | (BaseProps & {
      showCenterLabel: true;
      centerLabel: {
        primaryText: string;
        secondaryText: string;
      };
    })
  | (BaseProps & {
      showCenterLabel?: false;
      centerLabel?: never;
    });

interface StyledTextProps {
  variant: "primary" | "secondary";
}

const StyledText = styled("text", {
  shouldForwardProp: (prop) => prop !== "variant",
})<StyledTextProps>(({ theme, variant }) => ({
  fill: theme.palette.text.secondary,
  textAnchor: "middle",
  dominantBaseline: "central",
  fontWeight: variant === "primary" ? "bold" : "normal",
  fontSize:
    variant === "primary"
      ? theme.typography.h4.fontSize
      : theme.typography.subtitle2.fontSize,
}));

const PieCenterLabel = ({
  primaryText,
  secondaryText,
}: {
  primaryText: string;
  secondaryText: string;
}) => {
  const { width, height, left, top } = useDrawingArea();
  const primaryY = top + height / 2 - 10;
  const secondaryY = primaryY + 24;

  return (
    <>
      <StyledText x={left + width / 2} y={primaryY} variant="primary">
        {primaryText}
      </StyledText>
      <StyledText x={left + width / 2} y={secondaryY} variant="secondary">
        {secondaryText}
      </StyledText>
    </>
  );
};

const ChartPie = (props: ChartPieProps) => {
  const {
    data,
    hiddenLegend,
    showCenterLabel,
    centerLabel,
    tooltip = "item",
    colors = [brand[900], brand[700], brand[500], brand[300], brand[100]],
  } = props;
  const theme = useTheme();

  const mdUp = useMediaQuery(theme.breakpoints.up("md"));
  const lgUp = useMediaQuery(theme.breakpoints.up("lg"));

  const size = lgUp ? 280 : mdUp ? 160 : 160;
  const innerRadiusSize = lgUp ? 75 : mdUp ? 50 : 50;
  const outerRadiusSize = lgUp ? 100 : mdUp ? 70 : 68;

  return (
    <PieChart
      series={[
        {
          data,
          innerRadius: innerRadiusSize,
          outerRadius: outerRadiusSize,
          paddingAngle: 0,
          highlightScope: { faded: "global", highlighted: "item" },
        },
      ]}
      colors={colors}
      margin={{ left: 20, right: 20, top: 80, bottom: 80 }}
      width={size}
      height={size}
      slotProps={{ legend: { hidden: hiddenLegend } }}
      tooltip={{
        trigger: tooltip,
      }}
    >
      {showCenterLabel && (
        <PieCenterLabel
          primaryText={centerLabel?.primaryText || ""}
          secondaryText={centerLabel?.secondaryText || ""}
        />
      )}
    </PieChart>
  );
};

export default ChartPie;
