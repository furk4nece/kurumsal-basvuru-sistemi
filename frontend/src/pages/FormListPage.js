import { useCallback, useEffect, useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import {
  Alert,
  Box,
  Button,
  Grid,
  IconButton,
  Link,
  MenuItem,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TableSortLabel,
  TextField,
  Tooltip,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import Layout from "../components/Layout";
import StatusChip from "../components/StatusChip";
import { approveForm, deleteForm, getAllFormTypes, getForms, rejectForm } from "../api/formApi";
import { useAuth } from "../context/AuthContext";
import { STATUS_LABELS, STATUS_OPTIONS, isEditable } from "../constants/status";
import { extractErrorMessage, formatDate } from "../utils/format";

const initialFilters = { keyword: "", status: "", formTypeId: "", startDate: "", endDate: "" };

function FormListPage() {
  const { user, isAdmin } = useAuth();
  const navigate = useNavigate();

  const [forms, setForms] = useState([]);
  const [formTypes, setFormTypes] = useState([]);
  const [filters, setFilters] = useState(initialFilters);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [orderBy, setOrderBy] = useState("createdDate");
  const [order, setOrder] = useState("desc");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const loadForms = useCallback(async () => {
    setLoading(true);
    try {
      const params = {
        page,
        size: rowsPerPage,
        sort: `${orderBy},${order}`,
      };
      if (filters.keyword) params.keyword = filters.keyword;
      if (filters.status) params.status = filters.status;
      if (filters.formTypeId) params.formTypeId = filters.formTypeId;
      if (filters.startDate) params.startDate = filters.startDate;
      if (filters.endDate) params.endDate = filters.endDate;

      const response = await getForms(params);
      const pageInfo = response.data.page ?? response.data;
      setForms(response.data.content ?? []);
      setTotalElements(pageInfo.totalElements ?? 0);
      setError("");
    } catch (err) {
      setError(extractErrorMessage(err, "Basvurular yuklenirken hata olustu"));
    } finally {
      setLoading(false);
    }
  }, [page, rowsPerPage, orderBy, order, filters]);

  useEffect(() => {
    loadForms();
  }, [loadForms]);

  useEffect(() => {
    getAllFormTypes()
      .then((response) => setFormTypes(response.data))
      .catch(() => setFormTypes([]));
  }, []);

  const handleFilterChange = (event) => {
    setPage(0);
    setFilters({ ...filters, [event.target.name]: event.target.value });
  };

  const handleSort = (field) => {
    if (orderBy === field) {
      setOrder(order === "asc" ? "desc" : "asc");
    } else {
      setOrderBy(field);
      setOrder("asc");
    }
    setPage(0);
  };

  const runAction = async (action, confirmText) => {
    if (confirmText && !window.confirm(confirmText)) return;
    try {
      await action();
      loadForms();
    } catch (err) {
      setError(extractErrorMessage(err, "Islem basarisiz"));
    }
  };

  const canModify = (form) =>
    isAdmin || (form.applicant?.email === user?.email && isEditable(form.status));

  return (
    <Layout title="Basvurular">
      <Paper sx={{ p: 2, mb: 3 }}>
                <Grid container spacing={2}>
          <Grid item xs={12} md={4}>
            <TextField
              label="Anahtar Kelime"
              name="keyword"
              value={filters.keyword}
              onChange={handleFilterChange}
              size="small"
              fullWidth
            />
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField
              select
              label="Durum"
              name="status"
              value={filters.status}
              onChange={handleFilterChange}
              size="small"
              fullWidth
            >
              <MenuItem value="">Tumu</MenuItem>
              {STATUS_OPTIONS.map((status) => (
                <MenuItem key={status} value={status}>
                  {STATUS_LABELS[status]}
                </MenuItem>
              ))}
            </TextField>
          </Grid>
          <Grid item xs={12} sm={6} md={4}>
            <TextField
              select
              label="Basvuru Turu"
              name="formTypeId"
              value={filters.formTypeId}
              onChange={handleFilterChange}
              size="small"
              fullWidth
            >
              <MenuItem value="">Tumu</MenuItem>
              {formTypes.map((formType) => (
                <MenuItem key={formType.id} value={formType.id}>
                  {formType.name}
                </MenuItem>
              ))}
            </TextField>
          </Grid>

          <Grid item xs={12} sm={6} md={3}>
            <TextField
              label="Baslangic"
              type="date"
              name="startDate"
              value={filters.startDate}
              onChange={handleFilterChange}
              size="small"
              fullWidth
              sx={{ minWidth: 170 }}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <TextField
              label="Bitis"
              type="date"
              name="endDate"
              value={filters.endDate}
              onChange={handleFilterChange}
              size="small"
              fullWidth
              sx={{ minWidth: 170 }}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Button
              fullWidth
              onClick={() => {
                setFilters(initialFilters);
                setPage(0);
              }}
            >
              Temizle
            </Button>
          </Grid>
        </Grid>
      </Paper>

      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate("/forms/new")}>
          Yeni Basvuru
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError("")}>
          {error}
        </Alert>
      )}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sortDirection={orderBy === "title" ? order : false}>
                <TableSortLabel
                  active={orderBy === "title"}
                  direction={orderBy === "title" ? order : "asc"}
                  onClick={() => handleSort("title")}
                >
                  Baslik
                </TableSortLabel>
              </TableCell>
              <TableCell>Tur</TableCell>
              <TableCell sortDirection={orderBy === "status" ? order : false}>
                <TableSortLabel
                  active={orderBy === "status"}
                  direction={orderBy === "status" ? order : "asc"}
                  onClick={() => handleSort("status")}
                >
                  Durum
                </TableSortLabel>
              </TableCell>
              <TableCell>Basvuran</TableCell>
              <TableCell sortDirection={orderBy === "createdDate" ? order : false}>
                <TableSortLabel
                  active={orderBy === "createdDate"}
                  direction={orderBy === "createdDate" ? order : "asc"}
                  onClick={() => handleSort("createdDate")}
                >
                  Olusturma
                </TableSortLabel>
              </TableCell>
              <TableCell align="right">Islemler</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {forms.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} align="center">
                  {loading ? "Yukleniyor..." : "Kayit bulunamadi."}
                </TableCell>
              </TableRow>
            ) : (
              forms.map((form) => (
                <TableRow key={form.id} hover>
                  <TableCell>
                    <Link component={RouterLink} to={`/forms/${form.id}`}>
                      {form.title}
                    </Link>
                  </TableCell>
                  <TableCell>{form.formType?.name}</TableCell>
                  <TableCell>
                    <StatusChip status={form.status} />
                  </TableCell>
                  <TableCell>
                    {form.applicant?.name} {form.applicant?.surname}
                  </TableCell>
                  <TableCell>{formatDate(form.createdDate)}</TableCell>
                  <TableCell align="right">
                    {isAdmin && isEditable(form.status) && (
                      <>
                        <Tooltip title="Onayla">
                          <IconButton
                            color="success"
                            size="small"
                            onClick={() => runAction(() => approveForm(form.id), "Basvuru onaylansin mi?")}
                          >
                            <CheckIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Reddet">
                          <IconButton
                            color="error"
                            size="small"
                            onClick={() => runAction(() => rejectForm(form.id), "Basvuru reddedilsin mi?")}
                          >
                            <CloseIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                      </>
                    )}
                    {canModify(form) && (
                      <>
                        <Tooltip title="Duzenle">
                          <IconButton size="small" onClick={() => navigate(`/forms/${form.id}/edit`)}>
                            <EditIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Sil">
                          <IconButton
                            size="small"
                            onClick={() => runAction(() => deleteForm(form.id), "Bu basvuru silinsin mi?")}
                          >
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                      </>
                    )}
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>

        <TablePagination
          component="div"
          count={totalElements}
          page={page}
          onPageChange={(event, newPage) => setPage(newPage)}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={(event) => {
            setRowsPerPage(parseInt(event.target.value, 10));
            setPage(0);
          }}
          rowsPerPageOptions={[5, 10, 25, 50]}
          labelRowsPerPage="Sayfa basina"
        />
      </TableContainer>
    </Layout>
  );
}

export default FormListPage;