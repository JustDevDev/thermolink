import SearchOffIcon from "@mui/icons-material/SearchOff";
import { Box, Skeleton, TableBody, TableCell, TableRow } from "@mui/material";
import { TableColumns } from "@/types/types";
import Label from "../typography/Label";

export interface TableRowProps {
  rows: React.ReactNode[];
  columns: TableColumns[];
  loading?: boolean;
}

const TableRows = (props: TableRowProps) => {
  const { rows, columns, loading } = props;

  if (loading) {
    return (
      <TableBody>
        <TableRow>
          <TableCell
            colSpan={columns.length}
            sx={{
              textAlign: "center",
              padding: 2,
            }}
          >
            <Skeleton variant="text" sx={{ fontSize: "1.5rem" }} />
            <Skeleton variant="text" sx={{ fontSize: "1.5rem" }} />
            <Skeleton variant="text" sx={{ fontSize: "1.5rem" }} />
            <Skeleton variant="text" sx={{ fontSize: "1.5rem" }} />
            <Skeleton variant="text" sx={{ fontSize: "1.5rem" }} />
          </TableCell>
        </TableRow>
      </TableBody>
    );
  }

  return (
    <TableBody>
      {rows.length === 0 ? (
        <TableRow>
          <TableCell
            colSpan={columns.length}
            sx={{
              textAlign: "center",
              padding: 2,
            }}
          >
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 1,
              }}
            >
              <SearchOffIcon sx={{ opacity: 0.5, color: "inherit" }} />
              <Label intld="table.noData" />
            </Box>
          </TableCell>
        </TableRow>
      ) : (
        rows
      )}
    </TableBody>
  );
};

export default TableRows;
