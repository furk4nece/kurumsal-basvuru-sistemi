import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Alert, Button, Card, CardContent, MenuItem, Stack, TextField } from "@mui/material";
import Layout from "../components/Layout";
import { createForm, getAllFormTypes } from "../api/formApi";
import { extractErrorMessage } from "../utils/format";

function NewFormPage() {
  const [formTypes, setFormTypes] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [formTypeId, setFormTypeId] = useState("");
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    getAllFormTypes()
      .then((response) => setFormTypes(response.data))
      .catch(() => setError("Form turleri yuklenemedi"));
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setSaving(true);
    try {
      const response = await createForm({ title, description, formTypeId: Number(formTypeId) });
      navigate(`/forms/${response.data.id}`, { replace: true });
    } catch (err) {
      setError(extractErrorMessage(err, "Basvuru olusturulamadi"));
    } finally {
      setSaving(false);
    }
  };

  return (
    <Layout title="Yeni Basvuru">
      <Card sx={{ maxWidth: 700 }}>
        <CardContent component="form" onSubmit={handleSubmit} sx={{ p: 3 }}>
          <Stack spacing={2}>
            <TextField
              label="Baslik"
              value={title}
              onChange={(event) => setTitle(event.target.value)}
              required
              fullWidth
              inputProps={{ maxLength: 100 }}
              helperText={`${title.length}/100`}
            />
            <TextField
              label="Aciklama"
              value={description}
              onChange={(event) => setDescription(event.target.value)}
              multiline
              rows={5}
              fullWidth
              inputProps={{ maxLength: 1000 }}
              helperText={`${description.length}/1000`}
            />
            <TextField
              select
              label="Basvuru Turu"
              value={formTypeId}
              onChange={(event) => setFormTypeId(event.target.value)}
              required
              fullWidth
            >
              {formTypes.map((formType) => (
                <MenuItem key={formType.id} value={formType.id}>
                  {formType.name}
                </MenuItem>
              ))}
            </TextField>

            {error && <Alert severity="error">{error}</Alert>}

            <Stack direction="row" spacing={2}>
              <Button type="submit" variant="contained" disabled={saving}>
                {saving ? "Kaydediliyor..." : "Olustur"}
              </Button>
              <Button onClick={() => navigate("/forms")}>Vazgec</Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>
    </Layout>
  );
}

export default NewFormPage;