import BasicView from "@/components/basic-components/view/BasicView";
import View from "@/components/basic-components/view/View";
import ResponsiveSideMenu from "@/components/common-components/menu/responsive/ResponsiveSideMenu";
import SideMenu from "@/components/common-components/menu/SideMenu";
import { Outlet } from "react-router-dom";
import Breadcrumbs from "@/components/common-components/breadcrumbs/Breadcrumbs";
import AdminDialogs from "../dialogs/AdminDialogs";

export const Admin = () => {
  return (
    <View>
      <AdminDialogs />
      <ResponsiveSideMenu />
      <SideMenu />
      <BasicView>
        <Breadcrumbs />
        <Outlet />
      </BasicView>
    </View>
  );
};
