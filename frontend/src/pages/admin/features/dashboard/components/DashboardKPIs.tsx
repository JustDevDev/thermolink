import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";
import { useDashboardStore } from "../hooks/useDashboardStore";
import DashboardKPI from "./DashboardKPI";

const DashboardKPIs = observer(() => {
  const store = useDashboardStore();

  return (
    <Box
      sx={{
        display: "grid",
        gap: 2,
        width: "100%",
        gridTemplateColumns: {
          xs: "1fr",
          sm: "repeat(2, 1fr)",
          md: "repeat(4, 1fr)",
        },
      }}
    >
      <DashboardKPI
        title="dashboard.activeSensors"
        value={String(store.KPIs?.activeSensors ?? 0)}
      />
      <DashboardKPI
        title="dashboard.inActiveSensors"
        value={String(store.KPIs?.inActiveSensors ?? 0)}
      />
      <DashboardKPI
        title="dashboard.todayHighTemperature"
        value={`${store.KPIs?.todayHighTemperature.temperature ?? "N/A"} °C`}
        label={store.KPIs?.todayHighTemperature.place}
      />
      <DashboardKPI
        title="dashboard.todayLowTemperature"
        value={`${store.KPIs?.todayLowTemperature.temperature ?? "N/A"} °C`}
        label={store.KPIs?.todayLowTemperature.place}
      />
    </Box>
  );
});

export default DashboardKPIs;
