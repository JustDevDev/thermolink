import { adminPaths } from "@/routers/admin/adminPath";
import { HelpDialogTypes } from "@/types/types";
import dashboardImage from "@/utils/images/dashboard.png";
import sensorsImage from "@/utils/images/sensors.png";
import plcImage from "@/utils/images/plcs.png";
import diagramVideo from "@/utils/videos/diagram-help.webp";

export const dataForHelp: HelpDialogTypes[] = [
  {
    name: adminPaths.DASHBOARD,
    title: "dashboard",
    description: "dashboardDescription",
    image: dashboardImage,
  },
  {
    name: adminPaths.SENSORS,
    title: "sensors",
    description: "sensorsDescription",
    image: sensorsImage,
  },
  {
    name: adminPaths.PLC,
    title: "plc",
    description: "plcDescription",
    image: plcImage,
  },
  {
    name: adminPaths.DIAGRAM,
    title: "diagram",
    description: "diagramDescription",
    image: diagramVideo,
  },
];
