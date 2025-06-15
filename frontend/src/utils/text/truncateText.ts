export const truncateText = (text: string, maxLength: number = 12) =>
  text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
