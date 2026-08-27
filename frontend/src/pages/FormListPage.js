import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllForms, deleteForm } from "../api/formApi";
import { useAuth } from "../context/AuthContext";

function FormListPage() {
  const { user, logout } = useAuth();
  const [forms, setForms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadForms = async () => {
    setLoading(true);
    try {
      const response = await getAllForms();
      setForms(response.data);
    } catch (err) {
      setError("Basvurular yuklenirken hata olustu");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadForms();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Bu basvuruyu silmek istediginize emin misiniz?")) return;
    try {
      await deleteForm(id);
      loadForms(); // listeyi yenile
    } catch (err) {
      alert(err.response?.data?.message || "Silme islemi basarisiz");
    }
  };

  if (loading) return <p style={{ textAlign: "center", marginTop: 40 }}>Yukleniyor...</p>;

  return (
    <div style={{ maxWidth: 800, margin: "40px auto" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h2>Basvurular</h2>
        <div>
          <span style={{ marginRight: 12 }}>
            {user?.name} {user?.surname} ({user?.role})
          </span>
          <button onClick={logout}>Cikis Yap</button>
        </div>
      </div>

      <Link to="/forms/new">
        <button style={{ margin: "16px 0", padding: "8px 16px" }}>+ Yeni Basvuru</button>
      </Link>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {forms.length === 0 ? (
        <p>Henuz basvuru yok.</p>
      ) : (
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ borderBottom: "1px solid #ccc", textAlign: "left" }}>
              <th style={{ padding: 8 }}>Baslik</th>
              <th style={{ padding: 8 }}>Tur</th>
              <th style={{ padding: 8 }}>Durum</th>
              <th style={{ padding: 8 }}>Basvuran</th>
              <th style={{ padding: 8 }}>Islemler</th>
            </tr>
          </thead>
          <tbody>
            {forms.map((form) => (
              <tr key={form.id} style={{ borderBottom: "1px solid #eee" }}>
                <td style={{ padding: 8 }}>
                  <Link to={`/forms/${form.id}`}>{form.title}</Link>
                </td>
                <td style={{ padding: 8 }}>{form.formType?.name}</td>
                <td style={{ padding: 8 }}>{form.status}</td>
                <td style={{ padding: 8 }}>
                  {form.applicant?.name} {form.applicant?.surname}
                </td>
                <td style={{ padding: 8 }}>
                  <button onClick={() => handleDelete(form.id)}>Sil</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default FormListPage;