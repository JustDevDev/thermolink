import { Box, Table as MuiTable, TableContainer } from "@mui/material";
import { useEffect, useState } from "react";
import TableHeader, { TableHeaderProps } from "./TableHeader";
import TableRow, { TableRowProps } from "./TableRow";
import TablePagination, { TablePaginationProps } from "./TablePagination";

interface ITableProps
  extends TableHeaderProps,
    TableRowProps,
    TablePaginationProps {
  getData: () => void;
  disabledPagination?: boolean;
  onSort?: (columnName: string, direction: "asc" | "desc" | "none") => void;
}

const Table = (props: ITableProps) => {
  const {
    columns,
    rows,
    rowsPerPage,
    rowsPerPageOptions,
    page,
    count,
    handleChangePage,
    handleChangeRowsPerPage,
    getData,
    loading,
    disabledPagination,
    onSort,
  } = props;
  const [sortColumn, setSortColumn] = useState<string>("");
  const [sortDirection, setSortDirection] = useState<"asc" | "desc" | "none">(
    "none"
  );

  const handleSort = (
    columnName: string,
    direction: "asc" | "desc" | "none"
  ) => {
    setSortColumn(direction === "none" ? "" : columnName);
    setSortDirection(direction);
    onSort?.(columnName, direction);
  };

  useEffect(() => {
    getData();
  }, [getData]);

  return (
    <Box sx={{ display: "flex", flexDirection: "column", width: "100%" }}>
      <TableContainer sx={{ overflowX: "auto", marginBottom: "8px" }}>
        <MuiTable>
          <TableHeader
            columns={columns}
            onSort={handleSort}
            sortColumn={sortColumn}
            sortDirection={sortDirection}
          />
          <TableRow rows={rows} columns={columns} loading={loading} />
        </MuiTable>
      </TableContainer>
      {!disabledPagination && (
        <Box
          sx={{
            display: "flex",
            justifyContent: "flex-end",
            minWidth: "fit-content",
          }}
        >
          <TablePagination
            rowsPerPage={rowsPerPage}
            page={page}
            count={count}
            handleChangePage={handleChangePage}
            handleChangeRowsPerPage={handleChangeRowsPerPage}
            rowsPerPageOptions={rowsPerPageOptions}
          />
        </Box>
      )}
    </Box>
  );
};

export default Table;
