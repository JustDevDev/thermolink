import AuthBox from "@/components/basic-components/box/AuthBox";
import { observer } from "mobx-react-lite";
import { useParams } from "react-router-dom";
import ForgottenPasswordReset from "../components/forgottenPasswordReset";
import ForgottenPasswordNewPassword from "../components/forgottenPasswordNewPassword";

const ForgottenPassword = observer(() => {
  const { token } = useParams<{ token?: string }>();

  return (
    <AuthBox>
      {token ? (
        <ForgottenPasswordNewPassword token={token} />
      ) : (
        <ForgottenPasswordReset />
      )}
    </AuthBox>
  );
});

export default ForgottenPassword;
