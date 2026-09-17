import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: { main: "#1565c0" },
    secondary: { main: "#37474f" },
    background: { default: "#f4f6f8" },
  },
  shape: { borderRadius: 8 },
  typography: {
    fontFamily: "Segoe UI, Roboto, Helvetica, Arial, sans-serif",
    h5: { fontWeight: 600 },
    h6: { fontWeight: 600 },
  },
});

export default theme;