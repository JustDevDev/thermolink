import { Route, Navigate } from "react-router-dom";
import { adminPaths } from "./adminPath";
import Sensors from "@/pages/admin/features/sensors/views/Sensors";
import Dashboard from "@/pages/admin/features/dashboard/views/Dashboard";
import PLC from "@/pages/admin/features/PLC/views/PLC";
import Diagram from "@/pages/admin/features/diagram/views/Diagram";

export const RouterAdmin = (
  <>
    <Route path={adminPaths.DASHBOARD} element={<Dashboard />} />
    <Route path={adminPaths.SENSORS} element={<Sensors />} />
    <Route path={adminPaths.PLC} element={<PLC />} />
    <Route path={adminPaths.DIAGRAM} element={<Diagram />} />
    <Route path="*" element={<Navigate to="/admin/dashboard" replace />} />
  </>
);
