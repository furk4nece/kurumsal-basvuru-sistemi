import { AppBar, Box, Button, Container, Divider, Toolbar, Typography } from "@mui/material";
import AssignmentIcon from "@mui/icons-material/Assignment";
import { Link as RouterLink, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const navItems = [
  { label: "Dashboard", path: "/dashboard" },
  { label: "Basvurular", path: "/forms" },
  { label: "Yeni Basvuru", path: "/forms/new" },
  { label: "Profil", path: "/profile" },
  { label: "Kullanicilar", path: "/users", adminOnly: true },
];

function Layout({ title, children }) {
  const { user, logout, isAdmin } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "background.default" }}>
      <AppBar position="static">
        <Toolbar>
          <AssignmentIcon sx={{ mr: 1 }} />
          <Typography variant="h6" sx={{ mr: 4 }}>
            Basvuru Yonetim Sistemi
          </Typography>

          <Box sx={{ flexGrow: 1, display: "flex", gap: 1 }}>
            {navItems
              .filter((item) => !item.adminOnly || isAdmin)
              .map((item) => (
                <Button
                  key={item.path}
                  component={RouterLink}
                  to={item.path}
                  color="inherit"
                  sx={{
                    borderBottom: location.pathname === item.path ? "2px solid #fff" : "2px solid transparent",
                    borderRadius: 0,
                  }}
                >
                  {item.label}
                </Button>
              ))}
          </Box>

          <Typography variant="body2" sx={{ mr: 2 }}>
            {user?.name} {user?.surname} ({user?.role})
          </Typography>
          <Button color="inherit" variant="outlined" size="small" onClick={handleLogout}>
            Cikis Yap
          </Button>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        {title && (
          <>
            <Typography variant="h5" gutterBottom>
              {title}
            </Typography>
            <Divider sx={{ mb: 3 }} />
          </>
        )}
        {children}
      </Container>
    </Box>
  );
}

export default Layout;