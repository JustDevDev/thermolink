import Label from "@/components/basic-components/typography/Label";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import Title from "@/components/basic-components/typography/Title";
import { Card, CardContent } from "@mui/material";

export type TDashboardKPIProps = {
  title: string;
  value: string;
  label?: string;
};

const DashboardKPI = (props: TDashboardKPIProps) => {
  const { title, value, label } = props;

  return (
    <Card sx={{ width: "100%" }}>
      <CardContent>
        <Paragraph intld={title} variant="body2" />
        <Title variant="h5" sx={{ fontWeight: "bold", marginTop: 1 }}>
          {value}
        </Title>
        {label && <Label>{label}</Label>}
      </CardContent>
    </Card>
  );
};

export default DashboardKPI;
