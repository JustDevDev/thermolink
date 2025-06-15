import { observer } from "mobx-react-lite";
import { useDashboardStore } from "../hooks/useDashboardStore";
import Select, {
  selectItemsModel,
} from "@/components/basic-components/select/Select";
import Label from "@/components/basic-components/typography/Label";
import { Box } from "@mui/material";

const DashboardChartLinearContent = observer(() => {
  const store = useDashboardStore();

  const items: selectItemsModel<string>[] =
    store.plcs?.map((item) => ({
      value: item.id,
      content: item.name,
    })) ?? [];

  return (
    <>
      <Box sx={{ mt: 2, width: "50%" }}>
        <Select<string>
          items={items}
          label={store.plcs.length === 0 ? "PLC" : ""}
          value={store.selectedPLCId}
          disabled={store.plcs.length === 0}
          onChange={(value) => {
            store.selectedPLCId = value;
          }}
          fullWidth
        />
      </Box>
      <Label intld="dashboard.measuredValueInfo" sx={{ my: 1 }} />
    </>
  );
});

export default DashboardChartLinearContent;
