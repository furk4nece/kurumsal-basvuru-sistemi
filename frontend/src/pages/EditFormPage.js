import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Alert, Button, Card, CardContent, CircularProgress, MenuItem, Stack, TextField } from "@mui/material";
import Layout from "../components/Layout";
import { getAllFormTypes, getFormById, updateForm } from "../api/formApi";
import { extractErrorMessage } from "../utils/format";

function EditFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [formTypes, setFormTypes] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [formTypeId, setFormTypeId] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const load = useCallback(async () => {
    try {
      const [formResponse, typesResponse] = await Promise.all([getFormById(id), getAllFormTypes()]);
      setTitle(formResponse.data.title);
      setDescription(formResponse.data.description || "");
      setFormTypeId(formResponse.data.formType.id);
      setFormTypes(typesResponse.data);
    } catch (err) {
      setError(extractErrorMessage(err, "Basvuru yuklenemedi"));
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    load();
  }, [load]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    setSaving(true);
    try {
      await updateForm(id, { title, description, formTypeId: Number(formTypeId) });
      navigate(`/forms/${id}`, { replace: true });
    } catch (err) {
      setError(extractErrorMessage(err, "Guncelleme basarisiz"));
    } finally {
      setSaving(false);
    }
  };

  return (
    <Layout title="Basvuru Guncelle">
      {loading ? (
        <CircularProgress />
      ) : (
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
              />
              <TextField
                label="Aciklama"
                value={description}
                onChange={(event) => setDescription(event.target.value)}
                multiline
                rows={5}
                fullWidth
                inputProps={{ maxLength: 1000 }}
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
                  {saving ? "Kaydediliyor..." : "Kaydet"}
                </Button>
                <Button onClick={() => navigate(`/forms/${id}`)}>Vazgec</Button>
              </Stack>
            </Stack>
          </CardContent>
        </Card>
      )}
    </Layout>
  );
}

export default EditFormPage;