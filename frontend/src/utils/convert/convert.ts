/**
 * Converts File to Base64 string.
 * @param file Input file of type image/png or image/jpeg
 * @throws Error if the format is other than PNG or JPEG
 */
export async function fileToBase64(file: File): Promise<string> {
  // Format validation
  if (file.type !== "image/png" && file.type !== "image/jpeg") {
    throw new Error("Only PNG and JPEG formats are supported.");
  }

  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => {
      resolve(reader.result as string);
    };
    reader.onerror = () => {
      reject(new Error("Error reading file."));
    };
    reader.readAsDataURL(file);
  });
}
