import SideMenuStore from "@/components/common-components/menu/store/sideMenuStore";
import { UserStore } from "../user/userStore";
import { FieldErrorNoticeStore } from "@/components/common-components/field-error-notice/store/FieldErrorNoticeStore";
import SensorsDataTableStore from "@/pages/admin/features/sensors/stores/SensorsDataTableStore";
import PLCDataTableStore from "@/pages/admin/features/PLC/stores/PLCDataTableStore";
import DashboardStore from "@/pages/admin/features/dashboard/stores/DashboardStore";
import DiagramStore from "@/pages/admin/features/diagram/stores/DiagramStore";
import ForgottenPasswordStore from "@/pages/forgottenPassword/stores/ForgottenPasswordStore";
import { RegisterStore } from "@/pages/register/store/RegisterStore";
import { LoginStore } from "@/pages/login/stores/LoginStore";

export class RootStore {
  userStore: UserStore;
  fieldErrorNoticeStore: FieldErrorNoticeStore;
  loginStore: LoginStore;
  registerStore: RegisterStore;
  sideMenuStore: SideMenuStore;
  diagramStore: DiagramStore;
  sensorsDataTableStore: SensorsDataTableStore;
  PLCDataTableStore: PLCDataTableStore;
  dashboardStore: DashboardStore;
  forgottenPasswordStore: ForgottenPasswordStore;

  constructor() {
    this.userStore = new UserStore();

    this.loginStore = new LoginStore();
    this.registerStore = new RegisterStore();
    this.fieldErrorNoticeStore = new FieldErrorNoticeStore();
    this.sideMenuStore = new SideMenuStore();
    this.diagramStore = new DiagramStore();
    this.sensorsDataTableStore = new SensorsDataTableStore();
    this.PLCDataTableStore = new PLCDataTableStore();
    this.dashboardStore = new DashboardStore();
    this.forgottenPasswordStore = new ForgottenPasswordStore();
  }
}

export const rootStore = new RootStore();
