import Dialog from "@/components/basic-components/dialog/Dialog";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import { useSideMenuStore } from "@/components/common-components/menu/hooks/useSideMenuStore";
import { Language, LinkedIn, X } from "@mui/icons-material";
import { Box } from "@mui/material";
import { observer } from "mobx-react-lite";

const AboutUsDialog = observer(() => {
  const sideMenuStore = useSideMenuStore();

  return (
    <Dialog
      open={sideMenuStore.isAboutUsDialogOpen}
      onClose={() => sideMenuStore.setAboutUsDialogOpen(false)}
      title="menu.aboutUs"
      closeIcon
    >
      <Box>
        <Paragraph intld="text.aboutUsFirstParagraph" />
        <Paragraph intld="text.aboutUsSecondParagraph" sx={{ marginTop: 2 }} />
        <Paragraph intld="text.aboutUsThirdParagraph" sx={{ marginTop: 2 }} />
      </Box>
      <Box
        sx={{
          display: "flex",
          justifyContent: "end",
          marginTop: 5,
          gap: 2,
        }}
      >
        <Language
          onClick={() => window.open("https://just-dev.dev")}
          sx={{ cursor: "pointer", color: "text.secondary" }}
        />
        <X
          onClick={() => window.open("https://x.com/The_Just_Dev")}
          sx={{ cursor: "pointer", color: "text.secondary" }}
        />
        <LinkedIn
          onClick={() =>
            window.open("https://www.linkedin.com/in/just-dev-solutions/")
          }
          sx={{ cursor: "pointer", color: "text.secondary" }}
        />
      </Box>
    </Dialog>
  );
});

export default AboutUsDialog;
