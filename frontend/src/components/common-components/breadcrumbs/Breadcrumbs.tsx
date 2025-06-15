import HomeIcon from "@mui/icons-material/Home";
import { Breadcrumbs as MuiBreadcrumbs, Link } from "@mui/material";
import { Link as RouterLink, useLocation } from "react-router-dom";
import { appPaths } from "@/routers/app/appPath";
import { adminPaths } from "@/routers/admin/adminPath";
import { useAppTranslation } from "@/utils/translate/translate";
import { observer } from "mobx-react-lite";
import Paragraph from "@/components/basic-components/typography/Paragraph";

const Breadcrumbs = observer(() => {
  const location = useLocation();
  const pathnames = location.pathname.split("/").filter((x) => x);
  const { t } = useAppTranslation();

  return (
    <MuiBreadcrumbs aria-label="breadcrumb" sx={{ mb: 2 }}>
      <Link
        component={RouterLink}
        to={`${appPaths.ADMIN}/${adminPaths.DASHBOARD}`}
        color="inherit"
        sx={{ display: "flex", alignItems: "center" }}
      >
        <HomeIcon sx={{ mr: 0.5, fontSize: "20px" }} />
      </Link>
      {pathnames.map((value, index) => {
        if (value === "admin") return null;

        const last = index === pathnames.length - 1;
        const to = `/${pathnames.slice(0, index + 1).join("/")}`;

        return last ? (
          <Paragraph
            color="text.primary"
            key={to}
            variant="body2"
            sx={{ fontWeight: 600, color: "inherit" }}
          >
            {t(`menu.${value}`)}
          </Paragraph>
        ) : (
          <Link component={RouterLink} color="inherit" to={to} key={to}>
            {t(`menu.${value}`)}
          </Link>
        );
      })}
    </MuiBreadcrumbs>
  );
});

export default Breadcrumbs;
