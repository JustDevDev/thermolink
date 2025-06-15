import { useRef } from "react";
import { Avatar as MuiAvatar, Box, IconButton } from "@mui/material";
import PhotoCameraIcon from "@mui/icons-material/PhotoCamera";
import { fileToBase64 } from "@/utils/convert/convert";

type AvatarUploadProps = {
  src?: string;
  size?: number;
  onUpload: (dataUrl: string, fileSize: number) => void;
  initial?: string;
};

const AvatarUpload = ({
  src,
  size = 40,
  onUpload,
  initial,
}: AvatarUploadProps) => {
  const inputRef = useRef<HTMLInputElement>(null);

  const handleIconClick = () => {
    inputRef.current?.click();
  };

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    try {
      const base64 = await fileToBase64(file);
      onUpload(base64, file.size);
    } catch (error: unknown) {
      console.error(error);
      if (error instanceof Error) {
        alert(error.message);
      } else {
        alert("An unknown error occurred");
      }
    } finally {
      e.target.value = "";
    }
  };

  return (
    <Box
      sx={{
        position: "relative",
        width: size,
        height: size,
        "&:hover .overlay": {
          opacity: 1,
        },
      }}
    >
      <MuiAvatar src={src} sx={{ width: size, height: size }}>
        {initial}
      </MuiAvatar>
      <Box
        className="overlay"
        sx={{
          position: "absolute",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          bgcolor: "rgba(0, 0, 0, 0.5)",
          borderRadius: "50%",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          opacity: 0,
          transition: "opacity 0.3s",
        }}
      >
        <IconButton onClick={handleIconClick}>
          <PhotoCameraIcon />
        </IconButton>
      </Box>
      <input
        type="file"
        accept="image/png, image/jpeg"
        ref={inputRef}
        onChange={handleFileChange}
        style={{ display: "none" }}
      />
    </Box>
  );
};

export default AvatarUpload;
