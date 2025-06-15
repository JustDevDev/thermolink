import { useEffect, useState, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import VerifyAccountStore from "../store/VerifyAccountStore";
import { appPaths } from "@/routers/app/appPath";
import { ResponseVerifyAccountType } from "@/types/api/responseTypes";
import { Box, Card, Skeleton } from "@mui/material";
import Paragraph from "@/components/basic-components/typography/Paragraph";
import { Clear, Done } from "@mui/icons-material";

type ValidateType = {
  error: boolean;
  message: string;
};

const VerifyAccount = () => {
  const { token } = useParams<{ token?: string }>();
  const navigate = useNavigate();
  const store = useMemo(() => new VerifyAccountStore(), []);
  const [validate, setValidate] = useState<ValidateType>({
    error: false,
    message: "",
  });
  const [isVerifying, setIsVerifying] = useState(true);

  useEffect(() => {
    if (!token) {
      navigate(appPaths.LOGIN);
      return;
    }

    const validateToken = async () => {
      setIsVerifying(true);
      const response: ResponseVerifyAccountType = await store.verifyAccount(
        token
      );
      if (response.error) {
        setValidate({ error: true, message: response.error });
      } else {
        setValidate({ error: false, message: "" });
        setTimeout(() => {
          navigate(appPaths.LOGIN);
        }, 4000);
      }
      setIsVerifying(false);
    };

    validateToken();
  }, [token, navigate, store]);

  return (
    <Card
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: 2,
        margin: 5,
        padding: "1rem",
        textAlign: "left",
        width: "600px",
        height: "fit-content",
        color: validate.error ? "error.contrastText" : "success.contrastText",
      }}
    >
      {isVerifying ? (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            padding: 3,
          }}
        >
          <Skeleton variant="circular" width={80} height={80} sx={{ mb: 2 }} />
          <Skeleton variant="text" width="80%" sx={{ fontSize: "1.5rem" }} />
          <Skeleton variant="text" width="60%" sx={{ fontSize: "1rem" }} />
        </Box>
      ) : (
        <Box>
          {validate.error ? (
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                gap: 1,
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <Clear
                sx={{
                  fontSize: { xs: "3.0rem", sm: "4rem" },
                  color: "error.main",
                }}
              />
              <Paragraph
                intld={`error.${validate.message}`}
                sx={{ textAlign: "center", mt: 1 }}
              />
            </Box>
          ) : (
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                gap: 1,
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <Done
                sx={{
                  fontSize: { xs: "3.0rem", sm: "4rem" },
                  color: "success.main",
                }}
              />
              <Paragraph
                intld="success.yourAccountSuccessfullyVerified"
                sx={{ textAlign: "center", mt: 1 }}
              />
            </Box>
          )}
        </Box>
      )}
    </Card>
  );
};

export default VerifyAccount;
