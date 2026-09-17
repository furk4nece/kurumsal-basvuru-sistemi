import { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { Alert, Box, Button, Card, CardContent, Link, Stack, TextField, Typography } from "@mui/material";
import { loginUser } from "../api/authApi";
import { useAuth } from "../context/AuthContext";
import { extractErrorMessage } from "../utils/format";

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setLoading(true);
    try {
      const response = await loginUser({ email, password });
      const { token, ...userData } = response.data;
      login(userData, token);
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(extractErrorMessage(err, "Giris basarisiz, bilgilerinizi kontrol edin"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "background.default",
        p: 2,
      }}
    >
      <Card sx={{ width: 420 }}>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h5" gutterBottom>
            Giris Yap
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Basvuru Yonetim Sistemi
          </Typography>

          <Box component="form" onSubmit={handleSubmit}>
            <Stack spacing={2}>
              <TextField
                label="Email"
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                required
                fullWidth
              />
              <TextField
                label="Sifre"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                required
                fullWidth
              />
              {error && <Alert severity="error">{error}</Alert>}
              <Button type="submit" variant="contained" size="large" disabled={loading} fullWidth>
                {loading ? "Giris yapiliyor..." : "Giris Yap"}
              </Button>
            </Stack>
          </Box>

          <Typography variant="body2" sx={{ mt: 3 }}>
            Hesabin yok mu? <Link component={RouterLink} to="/register">Kayit Ol</Link>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
}

export default LoginPage;