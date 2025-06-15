import { Box } from "@mui/material";
import { gray } from "@/utils/theme/themePrimitives";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import AutoGraphIcon from "@mui/icons-material/AutoGraph";
import LanguageIcon from "@mui/icons-material/Language";
import SensorsIcon from "@mui/icons-material/Sensors";

export const RegisterBenefits = () => {
  return (
    <Box
      className="hidden md:flex"
      sx={{
        maxWidth: "400px",
        gap: "2rem",
        flexDirection: "column",
        marginBottom: { sm: "200px" },
      }}
    >
      <Box sx={{ display: "flex", gap: "0.5rem" }}>
        <Box>
          <SensorsIcon sx={{ color: gray[300] }} />
        </Box>
        <Box sx={{ paddingTop: "0.2rem" }}>
          <Paragraph intld="auth.designSensors" />
          <Paragraph
            intld="auth.designSensorsDescription"
            variant="body2"
            sx={{ color: "text.secondary", marginTop: "0.5rem" }}
          />
        </Box>
      </Box>
      <Box sx={{ display: "flex", gap: "0.5rem" }}>
        <Box>
          <AutoGraphIcon sx={{ color: gray[300] }} />
        </Box>
        <Box sx={{ paddingTop: "0.2rem" }}>
          <Paragraph intld="auth.analyzeTrends" />
          <Paragraph
            intld="auth.analyzeTrendsDescription"
            variant="body2"
            sx={{ color: "text.secondary", marginTop: "0.5rem" }}
          />
        </Box>
      </Box>
      <Box sx={{ display: "flex", gap: "0.5rem" }}>
        <Box>
          <LanguageIcon sx={{ color: gray[300] }} />
        </Box>
        <Box sx={{ paddingTop: "0.2rem" }}>
          <Paragraph intld="auth.monitorOnline" />
          <Paragraph
            intld="auth.monitorOnlineDescription"
            variant="body2"
            sx={{ color: "text.secondary", marginTop: "0.5rem" }}
          />
        </Box>
      </Box>
    </Box>
  );
};
