import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getFormById, updateForm } from "../api/formApi";
import { getAllFormTypes } from "../api/formApi";
import { uploadAttachment, deleteAttachment } from "../api/attachmentApi";
import { useAuth } from "../context/AuthContext";

function FormDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [form, setForm] = useState(null);
  const [formTypes, setFormTypes] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [formTypeId, setFormTypeId] = useState("");
  const [file, setFile] = useState(null);
  const [error, setError] = useState("");

  const loadForm = async () => {
    try {
      const res = await getFormById(id);
      setForm(res.data);
      setTitle(res.data.title);
      setDescription(res.data.description || "");
      setFormTypeId(res.data.formType.id);
    } catch (err) {
      setError("Basvuru yuklenemedi");
    }
  };

  useEffect(() => {
    loadForm();
    getAllFormTypes().then((res) => setFormTypes(res.data));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const isOwner = form && user && form.applicant.email === user.email;
  const canEdit = isOwner && form && (form.status === "NEW" || form.status === "IN_REVIEW");

  const handleUpdate = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await updateForm(id, { title, description, formTypeId: Number(formTypeId) });
      setEditMode(false);
      loadForm();
    } catch (err) {
      setError(err.response?.data?.message || "Guncelleme basarisiz");
    }
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!file) return;
    try {
      await uploadAttachment(id, file);
      setFile(null);
      loadForm();
    } catch (err) {
      setError(err.response?.data?.message || "Dosya yuklenemedi");
    }
  };

  const handleDeleteAttachment = async (attachmentId) => {
    if (!window.confirm("Bu dosyayi silmek istediginize emin misiniz?")) return;
    try {
      await deleteAttachment(attachmentId);
      loadForm();
    } catch (err) {
      alert(err.response?.data?.message || "Dosya silinemedi");
    }
  };

  if (error && !form) return <p style={{ color: "red", textAlign: "center" }}>{error}</p>;
  if (!form) return <p style={{ textAlign: "center", marginTop: 40 }}>Yukleniyor...</p>;

  return (
    <div style={{ maxWidth: 600, margin: "40px auto" }}>
      <button onClick={() => navigate("/forms")}>{"< Geri"}</button>
      <h2>{form.title}</h2>

      {!editMode ? (
        <>
          <p><strong>Aciklama:</strong> {form.description || "-"}</p>
          <p><strong>Tur:</strong> {form.formType.name}</p>
          <p><strong>Durum:</strong> {form.status}</p>
          <p><strong>Basvuran:</strong> {form.applicant.name} {form.applicant.surname}</p>

          {canEdit && (
            <button onClick={() => setEditMode(true)}>Duzenle</button>
          )}
        </>
      ) : (
        <form onSubmit={handleUpdate}>
          <div style={{ marginBottom: 12 }}>
            <label>Baslik</label>
            <input value={title} onChange={(e) => setTitle(e.target.value)} required style={{ width: "100%", padding: 8 }} />
          </div>
          <div style={{ marginBottom: 12 }}>
            <label>Aciklama</label>
            <textarea value={description} onChange={(e) => setDescription(e.target.value)} rows={4} style={{ width: "100%", padding: 8 }} />
          </div>
          <div style={{ marginBottom: 12 }}>
            <label>Form Turu</label>
            <select value={formTypeId} onChange={(e) => setFormTypeId(e.target.value)} required style={{ width: "100%", padding: 8 }}>
              {formTypes.map((ft) => (
                <option key={ft.id} value={ft.id}>{ft.name}</option>
              ))}
            </select>
          </div>
          <button type="submit">Kaydet</button>
          <button type="button" onClick={() => setEditMode(false)} style={{ marginLeft: 8 }}>Iptal</button>
        </form>
      )}

      {error && <p style={{ color: "red" }}>{error}</p>}

      <hr style={{ margin: "24px 0" }} />

      <h3>Ek Dosyalar</h3>
      {form.attachments && form.attachments.length > 0 ? (
        <ul>
          {form.attachments.map((att) => (
            <li key={att.id}>
              {att.originalName}
              {isOwner && (
                <button onClick={() => handleDeleteAttachment(att.id)} style={{ marginLeft: 8 }}>
                  Sil
                </button>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p>Henuz dosya eklenmemis.</p>
      )}

      {isOwner && (
        <form onSubmit={handleUpload} style={{ marginTop: 12 }}>
          <input type="file" onChange={(e) => setFile(e.target.files[0])} />
          <button type="submit" style={{ marginLeft: 8 }}>Yukle</button>
        </form>
      )}
    </div>
  );
}

export default FormDetailPage;