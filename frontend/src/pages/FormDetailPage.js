import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  Grid,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Stack,
  Typography,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import DownloadIcon from "@mui/icons-material/Download";
import Layout from "../components/Layout";
import StatusChip from "../components/StatusChip";
import { approveForm, cancelForm, changeFormStatus, deleteForm, getFormById, rejectForm } from "../api/formApi";
import { deleteAttachment, downloadAttachment, uploadAttachment } from "../api/attachmentApi";
import { useAuth } from "../context/AuthContext";
import { isEditable } from "../constants/status";
import { extractErrorMessage, formatDate } from "../utils/format";

function DetailRow({ label, children }) {
  return (
    <Grid item xs={12} sm={6}>
      <Typography variant="caption" color="text.secondary">
        {label}
      </Typography>
      <Typography variant="body1">{children}</Typography>
    </Grid>
  );
}

function FormDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user, isAdmin } = useAuth();

  const [form, setForm] = useState(null);
  const [file, setFile] = useState(null);
  const [error, setError] = useState("");
  const [info, setInfo] = useState("");
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    try {
      const response = await getFormById(id);
      setForm(response.data);
      setError("");
    } catch (err) {
      setError(extractErrorMessage(err, "Basvuru yuklenemedi"));
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    load();
  }, [load]);

  const isOwner = form && user && form.applicant?.email === user.email;
  const canEdit = form && (isAdmin || (isOwner && isEditable(form.status)));

  const runAction = async (action, confirmText) => {
    if (confirmText && !window.confirm(confirmText)) return;
    setError("");
    setInfo("");
    try {
      await action();
      await load();
      setInfo("Islem basarili");
    } catch (err) {
      setError(extractErrorMessage(err, "Islem basarisiz"));
    }
  };

  const handleUpload = async (event) => {
    event.preventDefault();
    if (!file) return;
    await runAction(async () => {
      await uploadAttachment(id, file);
      setFile(null);
      event.target.reset();
    });
  };

  const handleDownload = async (attachment) => {
    try {
      const response = await downloadAttachment(attachment.id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.download = attachment.originalName;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setError(extractErrorMessage(err, "Dosya indirilemedi"));
    }
  };

  if (loading) {
    return (
      <Layout title="Basvuru Detay">
        <CircularProgress />
      </Layout>
    );
  }

  if (!form) {
    return (
      <Layout title="Basvuru Detay">
        <Alert severity="error">{error || "Basvuru bulunamadi"}</Alert>
        <Button sx={{ mt: 2 }} onClick={() => navigate("/forms")}>
          Listeye Don
        </Button>
      </Layout>
    );
  }

  return (
    <Layout title="Basvuru Detay">
      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError("")}>
          {error}
        </Alert>
      )}
      {info && (
        <Alert severity="success" sx={{ mb: 2 }} onClose={() => setInfo("")}>
          {info}
        </Alert>
      )}

      <Card sx={{ mb: 3 }}>
        <CardContent sx={{ p: 3 }}>
          <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
            <Typography variant="h6">{form.title}</Typography>
            <StatusChip status={form.status} />
          </Box>

          <Grid container spacing={2}>
            <DetailRow label="Basvuru Turu">{form.formType?.name}</DetailRow>
            <DetailRow label="Basvuran">
              {form.applicant?.name} {form.applicant?.surname} ({form.applicant?.email})
            </DetailRow>
            <DetailRow label="Olusturma Tarihi">{formatDate(form.createdDate)}</DetailRow>
            <DetailRow label="Guncelleme Tarihi">{formatDate(form.updatedDate)}</DetailRow>
            <Grid item xs={12}>
              <Typography variant="caption" color="text.secondary">
                Aciklama
              </Typography>
              <Typography variant="body1" sx={{ whiteSpace: "pre-wrap" }}>
                {form.description || "-"}
              </Typography>
            </Grid>
          </Grid>

          <Divider sx={{ my: 3 }} />

          <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
            <Button onClick={() => navigate("/forms")}>Listeye Don</Button>

            {canEdit && (
              <Button variant="outlined" onClick={() => navigate(`/forms/${id}/edit`)}>
                Duzenle
              </Button>
            )}

            {isOwner && isEditable(form.status) && (
              <Button
                color="warning"
                onClick={() => runAction(() => cancelForm(id), "Basvuru iptal edilsin mi?")}
              >
                Iptal Et
              </Button>
            )}

            {isAdmin && form.status === "NEW" && (
              <Button onClick={() => runAction(() => changeFormStatus(id, "IN_REVIEW"))}>
                Incelemeye Al
              </Button>
            )}

            {isAdmin && isEditable(form.status) && (
              <>
                <Button
                  variant="contained"
                  color="success"
                  onClick={() => runAction(() => approveForm(id), "Basvuru onaylansin mi?")}
                >
                  Onayla
                </Button>
                <Button
                  variant="contained"
                  color="error"
                  onClick={() => runAction(() => rejectForm(id), "Basvuru reddedilsin mi?")}
                >
                  Reddet
                </Button>
              </>
            )}

            {canEdit && (
              <Button
                color="error"
                onClick={() =>
                  runAction(async () => {
                    await deleteForm(id);
                    navigate("/forms", { replace: true });
                  }, "Bu basvuru silinsin mi?")
                }
              >
                Sil
              </Button>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card>
        <CardContent sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Ek Dosyalar
          </Typography>

          {form.attachments?.length ? (
            <List dense>
              {form.attachments.map((attachment) => (
                <ListItem
                  key={attachment.id}
                  divider
                  secondaryAction={
                    <>
                      <IconButton size="small" onClick={() => handleDownload(attachment)}>
                        <DownloadIcon fontSize="small" />
                      </IconButton>
                      {canEdit && (
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() =>
                            runAction(() => deleteAttachment(attachment.id), "Bu dosya silinsin mi?")
                          }
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      )}
                    </>
                  }
                >
                  <ListItemText
                    primary={attachment.originalName}
                    secondary={formatDate(attachment.uploadDate)}
                  />
                </ListItem>
              ))}
            </List>
          ) : (
            <Typography variant="body2" color="text.secondary">
              Henuz dosya eklenmemis.
            </Typography>
          )}

          {isOwner && isEditable(form.status) && (
            <Box component="form" onSubmit={handleUpload} sx={{ mt: 2 }}>
              <Stack direction="row" spacing={2} alignItems="center">
                <input type="file" onChange={(event) => setFile(event.target.files[0])} />
                <Button type="submit" variant="contained" size="small" disabled={!file}>
                  Yukle
                </Button>
              </Stack>
            </Box>
          )}
        </CardContent>
      </Card>
    </Layout>
  );
}

export default FormDetailPage;