import { Box } from "@mui/material";
import GitHubIcon from "@mui/icons-material/GitHub";
import Button from "../button/Button";
import { gray } from "@/utils/theme/themePrimitives";
import Label from "../typography/Label";
import { observer } from "mobx-react-lite";
import { useUserStore } from "@/hooks/stores/useUserStore";

const GithubBox = observer(() => {
  const userStore = useUserStore();
  const isLightTheme = userStore.getTheme === "light";

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        padding: "1rem 0.5rem",
        margin: 1,
        justifyContent: "center",
        borderRadius: 1,
        gap: 2,
        border: "1px solid",
        borderColor: isLightTheme ? gray[100] : gray[600],
        backgroundColor: isLightTheme ? "#ffffff" : gray[900],
      }}
    >
      <Label intld="text.githubBoxText" />
      <Button
        startIcon={<GitHubIcon />}
        onClick={() => window.open("https://github.com/JustDevDev/thermolink")}
        intld="button.viewOnGithub"
        sx={{ padding: "0.1rem 1.4rem" }}
      />
    </Box>
  );
});

export default GithubBox;
