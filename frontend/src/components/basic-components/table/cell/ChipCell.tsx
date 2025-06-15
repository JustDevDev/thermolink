import { useAppTranslation } from "@/utils/translate/translate";
import { Chip, TableCell } from "@mui/material";

type TChipCellProps = {
  trueLabel: string;
  falseLabel: string;
  isTrue: boolean;
};

const ChipCell = (props: TChipCellProps) => {
  const { t } = useAppTranslation();

  return (
    <TableCell>
      <Chip
        label={
          props.isTrue ? t(props.trueLabel || "") : t(props.falseLabel || "")
        }
        className={props.isTrue ? "success-chip" : "error-chip"}
      />
    </TableCell>
  );
};

export default ChipCell;
