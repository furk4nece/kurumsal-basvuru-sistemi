export const formatDate = (value) => {
  if (!value) return "-";
  return new Date(value).toLocaleString("tr-TR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

export const extractErrorMessage = (error, fallback) => {
  const data = error?.response?.data;
  if (data?.details?.length) return data.details.join(", ");
  return data?.message || fallback;
};