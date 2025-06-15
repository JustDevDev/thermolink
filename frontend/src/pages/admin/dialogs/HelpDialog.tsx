import IconButton from "@/components/basic-components/button/IconButton";
import Dialog from "@/components/basic-components/dialog/Dialog";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import SubTitle from "@/components/basic-components/typography/SubTitle";
import { useSideMenuStore } from "@/components/common-components/menu/hooks/useSideMenuStore";
import { East } from "@mui/icons-material";
import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";
import { useEffect, useMemo, useState } from "react";
import { useLocation } from "react-router-dom";
import { adminPaths } from "@/routers/admin/adminPath";
import { dataForHelp } from "./utils/HelpDialogUtils";

const HelpDialog = observer(() => {
  const sideMenuStore = useSideMenuStore();
  const location = useLocation();
  const [currentHelpIndex, setCurrentHelpIndex] = useState(0);

  const currentPath = useMemo(() => {
    const pathParts = location.pathname.split("/");
    const adminIndex = pathParts.findIndex((part) => part === "admin");
    return adminIndex >= 0 && pathParts.length > adminIndex + 1
      ? (pathParts[adminIndex + 1] as adminPaths)
      : adminPaths.DASHBOARD;
  }, [location]);

  useEffect(() => {
    const index = dataForHelp.findIndex((item) => item.name === currentPath);
    setCurrentHelpIndex(index >= 0 ? index : 0);
  }, [currentPath]);

  const helpData = useMemo(() => {
    return dataForHelp[currentHelpIndex];
  }, [currentHelpIndex]);

  const goToPrevious = () => {
    setCurrentHelpIndex((prev) =>
      prev === 0 ? dataForHelp.length - 1 : prev - 1
    );
  };

  const goToNext = () => {
    setCurrentHelpIndex((prev) =>
      prev === dataForHelp.length - 1 ? 0 : prev + 1
    );
  };

  return (
    <Dialog
      open={sideMenuStore.isHelpDialogOpen}
      onClose={() => {
        sideMenuStore.setHelpDialogOpen(false);
      }}
      title="menu.help"
      closeIcon
    >
      <Box
        sx={{
          width: "100%",
          height: "300px",
          border: "1px solid black",
          backgroundImage: `url(${helpData.image})`,
          backgroundSize: "cover",
          backgroundPosition: "left top",
          backgroundRepeat: "no-repeat",
          borderRadius: 1,
        }}
      ></Box>
      <SubTitle
        intld={`help.${helpData.title}`}
        variant="subtitle1"
        sx={{ fontWeight: "bold", marginBottom: 1, mt: 2 }}
      />
      <Paragraph
        intld={`help.${helpData.description}`}
        sx={{ marginBottom: 2 }}
      />
      <Box sx={{ display: "flex", justifyContent: "end", gap: 1 }}>
        <IconButton onClick={goToPrevious}>
          <East
            sx={{
              transform: "rotate(180deg)",
              fontSize: "17px",
            }}
          />
        </IconButton>
        <IconButton onClick={goToNext}>
          <East sx={{ fontSize: "17px" }} />
        </IconButton>
      </Box>
    </Dialog>
  );
});

export default HelpDialog;
