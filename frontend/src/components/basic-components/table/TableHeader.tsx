import { TableColumns } from "@/types/types";
import { useAppTranslation } from "@/utils/translate/translate";
import { TableCell, TableHead, TableRow, TableSortLabel } from "@mui/material";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";

type SortDirection = "asc" | "desc" | "none";

export interface TableHeaderProps {
  columns: TableColumns[];
  onSort?: (columnName: string, direction: SortDirection) => void;
  sortColumn?: string;
  sortDirection?: SortDirection;
}

const TableHeader = (props: TableHeaderProps) => {
  const { columns, onSort, sortColumn, sortDirection } = props;
  const { t } = useAppTranslation();

  const handleSort = (columnName: string) => {
    if (!onSort) return;

    let newDirection: SortDirection = "asc";

    if (sortColumn === columnName) {
      if (sortDirection === "asc") {
        newDirection = "desc";
      } else if (sortDirection === "desc") {
        newDirection = "none";
      }
    }

    onSort(columnName, newDirection);
  };

  return (
    <TableHead>
      <TableRow>
        {columns.map((column) => (
          <TableCell
            key={column.key}
            align={column.align || "left"}
            onClick={() => column.sortable && handleSort(column.name)}
            sx={{
              position: "relative",
              cursor: column.sortable ? "pointer" : "default",
              userSelect: "none",
              "&:hover .MuiTableSortLabel-icon": {
                opacity: column.sortable ? 1 : 0,
              },
              whiteSpace: "nowrap",
            }}
          >
            {t(column.label)}
            {column.sortable && (
              <TableSortLabel
                active={sortColumn === column.name && sortDirection !== "none"}
                direction={
                  sortColumn === column.name && sortDirection !== "none"
                    ? sortDirection === "asc"
                      ? "asc"
                      : "desc"
                    : "asc"
                }
                IconComponent={ArrowDropDownIcon}
                sx={{
                  position: "absolute",
                  right: "16px",
                  top: "50%",
                  transform: "translateY(-50%)",
                  pointerEvents: "none",
                }}
              />
            )}
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
};

export default TableHeader;
