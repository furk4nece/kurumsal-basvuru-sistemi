export const STATUS_LABELS = {
  NEW: "Yeni",
  IN_REVIEW: "Incelemede",
  APPROVED: "Onaylandi",
  REJECTED: "Reddedildi",
  CANCELLED: "Iptal Edildi",
};

export const STATUS_COLORS = {
  NEW: "info",
  IN_REVIEW: "warning",
  APPROVED: "success",
  REJECTED: "error",
  CANCELLED: "default",
};

export const STATUS_OPTIONS = Object.keys(STATUS_LABELS);

export const isEditable = (status) => status === "NEW" || status === "IN_REVIEW";