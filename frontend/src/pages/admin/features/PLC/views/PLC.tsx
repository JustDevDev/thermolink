import Table from "@/components/basic-components/table/Table";
import { Box, TableCell, TableRow } from "@mui/material";
import { observer } from "mobx-react-lite";
import { ResponsePLCTableTypes } from "@/types/api/responseTypes";
import { useAppTranslation } from "@/utils/translate/translate";
import { usePLCDataTableStore } from "../hooks/usePLCDataTableStore";

const columns = [
  {
    name: "id",
    label: "plc.id",
    key: "id",
  },
  {
    name: "name",
    label: "plc.name",
    key: "name",
    sortable: true,
  },
  {
    name: "port0",
    label: "plc.port0",
    key: "port0",
  },
  {
    name: "port1",
    label: "plc.port1",
    key: "port1",
  },
  {
    name: "port2",
    label: "plc.port2",
    key: "port2",
  },
  {
    name: "port3",
    label: "plc.port3",
    key: "port3",
  },
];

const PLC = observer(() => {
  const store = usePLCDataTableStore();
  const { t } = useAppTranslation();

  const getSensorName = (item: ResponsePLCTableTypes, port: number) =>
    item.connectedSensors.find((sensor) => sensor.port === port)?.place;

  const renderSensorCell = (item: ResponsePLCTableTypes, port: number) => (
    <TableCell key={`port-${port}`}>
      {getSensorName(item, port) &&
        `${t("sensors.temperatureSensor")} ${getSensorName(item, port)}`}
    </TableCell>
  );

  const tableRows = store.items.map((item: ResponsePLCTableTypes) => (
    <TableRow key={item.id}>
      <TableCell>{item.id}</TableCell>
      <TableCell>{item.name}</TableCell>
      {[0, 1, 2, 3].map((port) => renderSensorCell(item, port))}
    </TableRow>
  ));

  return (
    <Box>
      <Table
        getData={store.getData}
        columns={columns}
        rows={tableRows}
        count={store.totalItems}
        rowsPerPage={store.rowsPerPage}
        page={store.currentPage}
        loading={store.loading}
        handleChangePage={(_, page) => {
          store.changePage(page);
        }}
        handleChangeRowsPerPage={(event) => {
          store.changeRowsPerPage(Number(event.target.value));
        }}
        onSort={store.handleSort}
        rowsPerPageOptions={store.rowsPerPageOptions}
      />
    </Box>
  );
});

export default PLC;
