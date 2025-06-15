import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../auth/authHooks";
import { observer } from "mobx-react-lite";
import { appPaths } from "./appPath";
import Login from "@/pages/login/views/Login";
import { RouterAdmin } from "../admin/RouterAdmin";
import { Admin } from "@/pages/admin/views/Admin";
import Register from "@/pages/register/views/Register";
import ForgottenPassword from "@/pages/forgottenPassword/views/ForgottenPassword";
import VerifyAccount from "@/pages/verifyAccount/views/VerifyAccount";
import Loading from "@/components/basic-components/loading/Loading";

export const RouterApp = () => {
  return (
    <Routes>
      <Route path={appPaths.LOGIN} element={<Login />} />
      <Route path={appPaths.REGISTER} element={<Register />} />
      <Route
        path={`${appPaths.FORGOTTEN_PASSWORD}/:token?`}
        element={<ForgottenPassword />}
      />
      <Route
        path={`${appPaths.VERIFY_ACCOUNT}/:token`}
        element={<VerifyAccount />}
      />
      <Route
        path={appPaths.ADMIN + "/*"}
        element={<PrivateRoute element={<Admin />} />}
      >
        {RouterAdmin}
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

interface PrivateRouteProps {
  element: JSX.Element;
}

const PrivateRoute: React.FC<PrivateRouteProps> = observer(({ element }) => {
  const userStore = useAuth();
  if (!userStore.isLoaded) {
    return <Loading />;
  }

  if (!userStore.isLoggedIn) {
    return <Navigate to="/" replace />;
  }

  return element;
});
