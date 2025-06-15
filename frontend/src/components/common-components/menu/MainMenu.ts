import SpaceDashboardIcon from "@mui/icons-material/SpaceDashboard";
import SensorsIcon from "@mui/icons-material/Sensors";
import GamepadIcon from "@mui/icons-material/Gamepad";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import { adminPaths } from "@/routers/admin/adminPath";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { SvgIconTypeMap } from "@mui/material/SvgIcon";

export type TMainMenuItem = {
  title: string;
  Icon: OverridableComponent<SvgIconTypeMap<object, "svg">>;
  path: string;
};

export const mainMenuItems: TMainMenuItem[] = [
  {
    title: "dashboard",
    Icon: SpaceDashboardIcon,
    path: adminPaths.DASHBOARD,
  },
  {
    title: "sensors",
    Icon: SensorsIcon,
    path: adminPaths.SENSORS,
  },
  {
    title: "plc",
    Icon: GamepadIcon,
    path: adminPaths.PLC,
  },
  {
    title: "diagram",
    Icon: AccountTreeIcon,
    path: adminPaths.DIAGRAM,
  },
];
