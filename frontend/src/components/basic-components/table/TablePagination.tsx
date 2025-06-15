import { TablePagination as MuiTablePagination } from "@mui/material";
import { useAppTranslation } from "@/utils/translate/translate";

export interface TablePaginationProps {
  rowsPerPage: number;
  rowsPerPageOptions?: number[];
  page: number;
  count: number;
  handleChangePage: (event: unknown, newPage: number) => void;
  handleChangeRowsPerPage: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const TablePagination = (props: TablePaginationProps) => {
  const {
    rowsPerPage,
    rowsPerPageOptions,
    page,
    count,
    handleChangePage,
    handleChangeRowsPerPage,
  } = props;
  const { t } = useAppTranslation();

  return (
    <MuiTablePagination
      component="div"
      count={count}
      rowsPerPage={rowsPerPage}
      page={page}
      onPageChange={handleChangePage}
      onRowsPerPageChange={handleChangeRowsPerPage}
      rowsPerPageOptions={rowsPerPageOptions}
      labelRowsPerPage={t("table.rowsPerPage")}
      labelDisplayedRows={({ from, to, count }) =>
        `${from}-${to} ${t("of")} ${count}`
      }
    />
  );
};

export default TablePagination;
