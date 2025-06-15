import Dialog from "@/components/basic-components/dialog/Dialog";
import Label from "@/components/basic-components/typography/Label";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import SubTitle from "@/components/basic-components/typography/SubTitle";
import { useUserStore } from "@/hooks/stores/useUserStore";
import { observer } from "mobx-react-lite";

const WelcomeDialog = observer(() => {
  const userStore = useUserStore();

  return (
    <Dialog
      open={!userStore.hintPassed}
      onClose={() => {
        userStore.updateUserData(
          {
            hintPassed: true,
          },
          false
        );
      }}
      title="help.welcome"
      closeIcon
    >
      <SubTitle
        intld={"help.welcomeDescription"}
        variant="subtitle1"
        sx={{ marginBottom: 1, mt: 2 }}
      />
      <Paragraph
        intld={"help.welcomeSecondaryDescription"}
        sx={{ marginBottom: 2 }}
      />
      <Paragraph
        intld={"help.welcomeThirdDescription"}
        sx={{ marginBottom: 2 }}
      />
      <Label intld={"help.welcomeFourthDescription"} sx={{ marginBottom: 2 }} />
    </Dialog>
  );
});

export default WelcomeDialog;
