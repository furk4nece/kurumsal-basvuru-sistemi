import { useCallback, useEffect, useState } from "react";
import { Alert, Button, Card, CardContent, CircularProgress, Stack, TextField, Typography } from "@mui/material";
import Layout from "../components/Layout";
import { getProfile, updateProfile } from "../api/userApi";
import { useAuth } from "../context/AuthContext";
import { extractErrorMessage } from "../utils/format";

function ProfilePage() {
  const { updateUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [info, setInfo] = useState("");
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const load = useCallback(async () => {
    try {
      const response = await getProfile();
      setProfile(response.data);
      setName(response.data.name);
      setSurname(response.data.surname);
    } catch (err) {
      setError(extractErrorMessage(err, "Profil yuklenemedi"));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setInfo("");
    setSaving(true);
    try {
      const payload = { name, surname };
      if (password) payload.password = password;
      const response = await updateProfile(payload);
      setProfile(response.data);
      updateUser({ name: response.data.name, surname: response.data.surname });
      setPassword("");
      setInfo("Profil guncellendi");
    } catch (err) {
      setError(extractErrorMessage(err, "Profil guncellenemedi"));
    } finally {
      setSaving(false);
    }
  };

  return (
    <Layout title="Profil">
      {loading ? (
        <CircularProgress />
      ) : (
        <Card sx={{ maxWidth: 600 }}>
          <CardContent component="form" onSubmit={handleSubmit} sx={{ p: 3 }}>
            <Stack spacing={2}>
              <Typography variant="body2" color="text.secondary">
                {profile?.email} - {profile?.role}
              </Typography>
              <TextField
                label="Ad"
                value={name}
                onChange={(event) => setName(event.target.value)}
                required
                fullWidth
              />
              <TextField
                label="Soyad"
                value={surname}
                onChange={(event) => setSurname(event.target.value)}
                required
                fullWidth
              />
              <TextField
                label="Yeni Sifre"
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                fullWidth
                helperText="Degistirmek istemiyorsan bos birak (en az 8 karakter)"
              />

              {error && <Alert severity="error">{error}</Alert>}
              {info && <Alert severity="success">{info}</Alert>}

              <Button type="submit" variant="contained" disabled={saving} sx={{ alignSelf: "flex-start" }}>
                {saving ? "Kaydediliyor..." : "Kaydet"}
              </Button>
            </Stack>
          </CardContent>
        </Card>
      )}
    </Layout>
  );
}

export default ProfilePage;