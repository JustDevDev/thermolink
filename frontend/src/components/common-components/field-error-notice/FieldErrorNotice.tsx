import { Box, Collapse, SxProps, Theme } from "@mui/material";
import { observer } from "mobx-react-lite";
import Paragraph from "../../basic-components/typography/Paragraph";
import { useFieldErrorNoticeStore } from "../../../hooks/stores/useFieldErrorNoticeStore";

type FieldErrorNoticeProps = {
  message: string;
  sx?: SxProps<Theme>;
};

export const FieldErrorNotice = observer((props: FieldErrorNoticeProps) => {
  const store = useFieldErrorNoticeStore();

  const { message, sx } = props;

  return (
    <Collapse in={store.collapse} orientation="vertical">
      <Box
        className="flex p-1 justify-center items-center"
        sx={{
          border: "1px solid",
          borderColor: "transparent",
          backgroundColor: "error.main",
          borderRadius: "0.20rem",
          marginY: "0.5rem",
          ...sx,
        }}
      >
        <Paragraph intld={message} color={"error.contrastText"} />
      </Box>
    </Collapse>
  );
});
