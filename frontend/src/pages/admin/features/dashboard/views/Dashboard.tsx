import { Box } from "@mui/material";
import Title from "@/components/basic-components/typography/Title";
import { useEffect } from "react";
import { observer } from "mobx-react-lite";
import { useDashboardStore } from "../hooks/useDashboardStore";
import DashboardKPIs from "../components/DashboardKPIs";
import DashboardChartLinear from "../components/DashboardChartLinear";
import DashboardChartPie from "../components/DashboardChartPie";
import Sensors from "../../sensors/views/Sensors";

const Dashboard = observer(() => {
  const store = useDashboardStore();

  useEffect(() => {
    const loadingDashboardData = async () => {
      await store.loadData();
    };

    loadingDashboardData();
  }, [store]);

  return (
    <Box>
      <Title
        intld="dashboard.overview"
        sx={{ fontWeight: "bold", marginY: 2 }}
        variant="h6"
      />
      <Box sx={{ marginBottom: 2 }}>
        <DashboardKPIs />
      </Box>
      <Box
        sx={{
          display: "flex",
          gap: 2,
          flexDirection: { xs: "column", md: "row" },
        }}
      >
        <DashboardChartLinear />
        <DashboardChartPie />
      </Box>
      <Title
        intld="dashboard.sensors"
        sx={{ fontWeight: "bold", marginY: 2 }}
        variant="h6"
      />
      <Sensors />
    </Box>
  );
});

export default Dashboard;
