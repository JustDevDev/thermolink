import Table from "@/components/basic-components/table/Table";
import { Box, TableCell, TableRow, Collapse } from "@mui/material";
import { observer } from "mobx-react-lite";
import { useAppTranslation } from "@/utils/translate/translate";
import ChipCell from "@/components/basic-components/table/cell/ChipCell";
import { useState } from "react";
import React from "react";
import SubTitle from "@/components/basic-components/typography/SubTitle";
import { ResponseSensorsTableTypes } from "@/types/api/responseTypes";
import { SparkLineChart } from "@mui/x-charts";
import i18next from "i18next";
import { useSensorsDataTableStore } from "../hooks/useSensorsDataTableStore";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";

const columns = [
  {
    name: "",
    label: "",
    key: "name",
  },
  {
    name: "status",
    label: "sensors.sensorStatus",
    key: "status",
  },
  {
    name: "place",
    label: "sensors.place",
    key: "place",
    sortable: true,
  },
  {
    name: "temperature",
    label: "sensors.temperature",
    key: "temperature",
    sortable: true,
  },
  {
    name: "averageTemperature",
    label: "sensors.averageTemperature",
    key: "averageTemperature",
    sortable: true,
  },
  {
    name: "condition",
    label: "sensors.status",
    key: "condition",
    sortable: true,
  },
  {
    name: "lastTemperatureRecords",
    label: "sensors.lastTemperatureRecords",
    key: "lastTemperatureRecords",
  },
];

const columnsPLCs = [
  {
    name: "name",
    label: "sensors.plcName",
    key: "name",
  },
  {
    name: "port",
    label: "sensors.plcPort",
    key: "port",
  },
];

const Sensors = observer(() => {
  const store = useSensorsDataTableStore();
  const [expandedRows, setExpandedRows] = useState<Set<string>>(new Set());
  const { t } = useAppTranslation();

  const toggleRow = (id: string) => {
    const newExpandedRows = new Set(expandedRows);
    if (expandedRows.has(id)) {
      newExpandedRows.delete(id);
    } else {
      newExpandedRows.add(id);
    }
    setExpandedRows(newExpandedRows);
  };

  const tableRows = store.items.map((item: ResponseSensorsTableTypes) => (
    <React.Fragment key={item.id}>
      <TableRow>
        <TableCell>
          {item.connectedPlcs.length > 0 && (
            <ArrowDropDownIcon
              sx={{
                cursor: "pointer",
                transform: expandedRows.has(item.id)
                  ? "rotate(180deg)"
                  : "none",
                transition: "transform 0.3s",
              }}
              onClick={() => toggleRow(item.id)}
            />
          )}
        </TableCell>
        <ChipCell
          trueLabel="sensors.online"
          falseLabel="sensors.offline"
          isTrue={item.connectedPlcs.length > 0}
        />
        <TableCell>{item.place}</TableCell>
        {item.temperature === null ? (
          <TableCell colSpan={4}>{t("error.sensorNoData")}</TableCell>
        ) : (
          <>
            <TableCell>{`${item.temperature}°C`}</TableCell>
            <TableCell>{`${item.averageTemperature}°C`}</TableCell>
            <TableCell>
              {t(`diagram.${item.condition.toUpperCase()}`)}
            </TableCell>
            <TableCell>
              <SparkLineChart
                plotType="bar"
                data={item.lastTemperatureRecords
                  .slice()
                  .sort(
                    (a, b) =>
                      new Date(a.date).getTime() - new Date(b.date).getTime()
                  )
                  .map((record) => record.temperature)}
                height={30}
                width={100}
                colors={["rgb(29,140,252)"]}
                showTooltip
                valueFormatter={(value) => `${value}°C`}
                showHighlight
                xAxis={{
                  scaleType: "band",
                  data: [
                    ...item.lastTemperatureRecords
                      .slice()
                      .sort(
                        (a, b) =>
                          new Date(a.date).getTime() -
                          new Date(b.date).getTime()
                      )
                      .map((record) => new Date(record.date)),
                  ],
                  valueFormatter: (value) => {
                    const date = value as Date;
                    const options: Intl.DateTimeFormatOptions = {
                      day: "2-digit",
                      month: "2-digit",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    };
                    const currentLanguage = i18next.language;
                    const isUS = currentLanguage === "en";
                    return date.toLocaleDateString(
                      isUS ? "en-US" : "cs-CZ",
                      options
                    );
                  },
                }}
              />
            </TableCell>
          </>
        )}
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={12}>
          <Collapse in={expandedRows.has(item.id)} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <SubTitle
                intld="sensors.connetctionDetail"
                sx={{ mb: 1, fontWeight: "bold", color: "text.secondary" }}
              />
              <Table
                getData={() => {}}
                columns={columnsPLCs}
                rows={item.connectedPlcs.map((plc) => (
                  <TableRow key={plc.id}>
                    <TableCell>{plc.name}</TableCell>
                    <TableCell>{plc.port}</TableCell>
                  </TableRow>
                ))}
                count={item.connectedPlcs.length}
                rowsPerPage={5}
                page={0}
                loading={false}
                handleChangePage={() => {}}
                handleChangeRowsPerPage={() => {}}
                disabledPagination
                rowsPerPageOptions={[]}
              />
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </React.Fragment>
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

export default Sensors;
