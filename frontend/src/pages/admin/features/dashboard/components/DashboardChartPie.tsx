import { observer } from "mobx-react-lite";
import { useDashboardStore } from "../hooks/useDashboardStore";
import ChartPie from "@/components/basic-components/chart/ChartPie";
import { useAppTranslation } from "@/utils/translate/translate";
import { Box, Card, CardContent, LinearProgress } from "@mui/material";
import SubTitle from "@/components/basic-components/typography/SubTitle";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import { brand, gray } from "@/utils/theme/themePrimitives";
import React from "react";
import { useUserStore } from "@/hooks/stores/useUserStore";

const DashboardChartPie = observer(() => {
  const store = useDashboardStore();
  const userStore = useUserStore();
  const { t } = useAppTranslation();

  const continents = ["europe", "asia", "america", "africa", "australia"];

  const linearProgressColors = [
    brand[100],
    brand[300],
    brand[500],
    brand[700],
    brand[900],
  ];

  return (
    <Card
      variant="outlined"
      sx={{
        width: "100%",
      }}
    >
      <CardContent sx={{ height: "100%" }}>
        <SubTitle
          variant="subtitle1"
          intld={"dashboard.geographicalMeasurements"}
          sx={{ fontWeight: "bold" }}
        />
        <Box
          sx={{
            display: "flex",
            gap: 2,
            alignItems: "center",
            justifyContent: "center",
            height: "100%",
          }}
        >
          <Box>
            {store.continents.length > 0 ? (
              <ChartPie
                data={store.continents.map((item) => ({
                  label: t(`dashboard.${item.continent}`),
                  value: item.count,
                }))}
                hiddenLegend
                showCenterLabel
                centerLabel={{
                  primaryText: String(store.totalSensors()),
                  secondaryText: t("total"),
                }}
              />
            ) : (
              <ChartPie
                data={[
                  {
                    label: "",
                    value: 1,
                  },
                ]}
                hiddenLegend
                showCenterLabel
                centerLabel={{
                  primaryText: "",
                  secondaryText: t("chart.noData"),
                }}
                tooltip="none"
                colors={userStore.theme === "light" ? [gray[300]] : [gray[600]]}
              />
            )}
          </Box>
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              gap: 1,
              width: "100%",
            }}
          >
            {store.continents.length > 0
              ? store.continents.map((item, index) => (
                  <React.Fragment key={index}>
                    <Box
                      sx={{ display: "flex", justifyContent: "space-between" }}
                    >
                      <Paragraph
                        intld={`dashboard.${item.continent}`}
                        variant="body2"
                      />
                      <Paragraph variant="body2">{`${Math.round(
                        (item.count / store.totalSensors()) * 100
                      )} %`}</Paragraph>
                    </Box>
                    <LinearProgress
                      variant="determinate"
                      value={Math.round(
                        (item.count / store.totalSensors()) * 100
                      )}
                      sx={{
                        height: 10,
                        "& .MuiLinearProgress-bar": {
                          backgroundColor: linearProgressColors[index],
                        },
                      }}
                    />
                  </React.Fragment>
                ))
              : continents.map((item, index) => (
                  <React.Fragment key={index}>
                    <Box
                      sx={{ display: "flex", justifyContent: "space-between" }}
                    >
                      <Paragraph intld={`dashboard.${item}`} variant="body2" />
                      <Paragraph variant="body2">0 %</Paragraph>
                    </Box>
                    <LinearProgress
                      variant="determinate"
                      value={0}
                      sx={{
                        height: 10,
                        "& .MuiLinearProgress-bar": {
                          backgroundColor: linearProgressColors[index],
                        },
                      }}
                    />
                  </React.Fragment>
                ))}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
});

export default DashboardChartPie;
