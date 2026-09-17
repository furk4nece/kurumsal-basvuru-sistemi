import Chip from "@mui/material/Chip";
import { STATUS_COLORS, STATUS_LABELS } from "../constants/status";

function StatusChip({ status }) {
  return (
    <Chip
      size="small"
      label={STATUS_LABELS[status] || status}
      color={STATUS_COLORS[status] || "default"}
      variant={status === "CANCELLED" ? "outlined" : "filled"}
    />
  );
}

export default StatusChip;