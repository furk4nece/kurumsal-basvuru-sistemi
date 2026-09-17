import { useCallback, useEffect, useState } from "react";
import {
  Alert,
  CircularProgress,
  IconButton,
  MenuItem,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Tooltip,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import Layout from "../components/Layout";
import { deleteUser, getAllUsers, updateUserRole } from "../api/userApi";
import { useAuth } from "../context/AuthContext";
import { extractErrorMessage } from "../utils/format";

function UsersPage() {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");
  const [info, setInfo] = useState("");
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const response = await getAllUsers();
      setUsers(response.data);
    } catch (err) {
      setError(extractErrorMessage(err, "Kullanicilar yuklenemedi"));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  const handleRoleChange = async (id, role) => {
    setError("");
    setInfo("");
    try {
      await updateUserRole(id, role);
      setInfo("Rol guncellendi");
      load();
    } catch (err) {
      setError(extractErrorMessage(err, "Rol guncellenemedi"));
    }
  };

  const handleDelete = async (item) => {
    if (!window.confirm(`${item.name} ${item.surname} silinsin mi?`)) return;
    setError("");
    setInfo("");
    try {
      await deleteUser(item.id);
      setInfo("Kullanici silindi");
      load();
    } catch (err) {
      setError(extractErrorMessage(err, "Kullanici silinemedi"));
    }
  };

  return (
    <Layout title="Kullanici Yonetimi">
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {info && <Alert severity="success" sx={{ mb: 2 }}>{info}</Alert>}

      {loading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Ad Soyad</TableCell>
                <TableCell>Email</TableCell>
                <TableCell width={200}>Rol</TableCell>
                <TableCell align="right" width={80}>Islemler</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((item) => {
                const isSelf = item.email === user?.email;
                return (
                  <TableRow key={item.id} hover>
                    <TableCell>
                      {item.name} {item.surname}
                    </TableCell>
                    <TableCell>{item.email}</TableCell>
                    <TableCell>
                      <TextField
                        select
                        size="small"
                        fullWidth
                        value={item.role}
                        disabled={isSelf}
                        onChange={(event) => handleRoleChange(item.id, event.target.value)}
                      >
                        <MenuItem value="PERSONEL">PERSONEL</MenuItem>
                        <MenuItem value="ADMIN">ADMIN</MenuItem>
                      </TextField>
                    </TableCell>
                    <TableCell align="right">
                      <Tooltip title={isSelf ? "Kendi hesabinizi silemezsiniz" : "Sil"}>
                        <span>
                          <IconButton
                            size="small"
                            color="error"
                            disabled={isSelf}
                            onClick={() => handleDelete(item)}
                          >
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        </span>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </Layout>
  );
}

export default UsersPage;