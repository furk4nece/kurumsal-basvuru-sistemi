import { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { Alert, Box, Button, Card, CardContent, Link, Stack, TextField, Typography } from "@mui/material";
import { registerUser } from "../api/authApi";
import { useAuth } from "../context/AuthContext";
import { extractErrorMessage } from "../utils/format";

function RegisterPage() {
  const [formData, setFormData] = useState({ name: "", surname: "", email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setLoading(true);
    try {
      const response = await registerUser(formData);
      const { token, ...userData } = response.data;
      login(userData, token);
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(extractErrorMessage(err, "Kayit basarisiz"));
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
      <Card sx={{ width: 460 }}>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h5" gutterBottom>
            Kayit Ol
          </Typography>

          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
            <Stack spacing={2}>
              <TextField label="Ad" name="name" value={formData.name} onChange={handleChange} required fullWidth />
              <TextField label="Soyad" name="surname" value={formData.surname} onChange={handleChange} required fullWidth />
              <TextField label="Email" type="email" name="email" value={formData.email} onChange={handleChange} required fullWidth />
              <TextField
                label="Sifre"
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
                fullWidth
                inputProps={{ minLength: 8 }}
                helperText="En az 8 karakter"
              />
              {error && <Alert severity="error">{error}</Alert>}
              <Button type="submit" variant="contained" size="large" disabled={loading} fullWidth>
                {loading ? "Kaydediliyor..." : "Kayit Ol"}
              </Button>
            </Stack>
          </Box>

          <Typography variant="body2" sx={{ mt: 3 }}>
            Zaten hesabin var mi? <Link component={RouterLink} to="/login">Giris Yap</Link>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
}

export default RegisterPage;