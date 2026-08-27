import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createForm, getAllFormTypes } from "../api/formApi";

function NewFormPage() {
  const [formTypes, setFormTypes] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [formTypeId, setFormTypeId] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    getAllFormTypes()
      .then((res) => setFormTypes(res.data))
      .catch(() => setError("Form turleri yuklenemedi"));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await createForm({ title, description, formTypeId: Number(formTypeId) });
      navigate("/forms");
    } catch (err) {
      const details = err.response?.data?.details;
      setError(details ? details.join(", ") : err.response?.data?.message || "Basvuru olusturulamadi");
    }
  };

  return (
    <div style={{ maxWidth: 500, margin: "40px auto" }}>
      <h2>Yeni Basvuru</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 12 }}>
          <label>Baslik</label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            style={{ width: "100%", padding: 8 }}
          />
        </div>
        <div style={{ marginBottom: 12 }}>
          <label>Aciklama</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={4}
            style={{ width: "100%", padding: 8 }}
          />
        </div>
        <div style={{ marginBottom: 12 }}>
          <label>Form Turu</label>
          <select
            value={formTypeId}
            onChange={(e) => setFormTypeId(e.target.value)}
            required
            style={{ width: "100%", padding: 8 }}
          >
            <option value="">Seciniz...</option>
            {formTypes.map((ft) => (
              <option key={ft.id} value={ft.id}>
                {ft.name}
              </option>
            ))}
          </select>
        </div>
        {error && <p style={{ color: "red" }}>{error}</p>}
        <button type="submit" style={{ padding: "8px 16px" }}>
          Olustur
        </button>
      </form>
    </div>
  );
}

export default NewFormPage;