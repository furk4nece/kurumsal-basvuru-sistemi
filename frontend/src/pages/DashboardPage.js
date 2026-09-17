import { useCallback, useEffect, useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import {
  Alert,
  Card,
  CardContent,
  CircularProgress,
  Grid,
  Link,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import Layout from "../components/Layout";
import StatusChip from "../components/StatusChip";
import { getDashboard } from "../api/dashboardApi";
import { extractErrorMessage, formatDate } from "../utils/format";

function StatCard({ label, value, color }) {
  return (
    <Card>
      <CardContent>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          {label}
        </Typography>
        <Typography variant="h4" sx={{ color, fontWeight: 600 }}>
          {value}
        </Typography>
      </CardContent>
    </Card>
  );
}

function DashboardPage() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const response = await getDashboard();
      setData(response.data);
      setError("");
    } catch (err) {
      setError(extractErrorMessage(err, "Dashboard verileri yuklenemedi"));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  return (
    <Layout title="Dashboard">
      {loading && <CircularProgress />}
      {error && <Alert severity="error">{error}</Alert>}

      {data && (
        <>
          <Grid container spacing={2} sx={{ mb: 4 }}>
            <Grid item xs={12} sm={6} md={2.4}>
              <StatCard label="Toplam Basvuru" value={data.totalCount} color="text.primary" />
            </Grid>
            <Grid item xs={12} sm={6} md={2.4}>
              <StatCard label="Bekleyen Basvuru" value={data.pendingCount} color="warning.main" />
            </Grid>
            <Grid item xs={12} sm={6} md={2.4}>
              <StatCard label="Onaylanan" value={data.approvedCount} color="success.main" />
            </Grid>
            <Grid item xs={12} sm={6} md={2.4}>
              <StatCard label="Reddedilen" value={data.rejectedCount} color="error.main" />
            </Grid>
            <Grid item xs={12} sm={6} md={2.4}>
              <StatCard label="Bugunku Basvurular" value={data.todayCount} color="primary.main" />
            </Grid>
          </Grid>

          <Typography variant="h6" gutterBottom>
            Son Olusturulan 10 Basvuru
          </Typography>

          <TableContainer component={Paper}>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>Baslik</TableCell>
                  <TableCell>Tur</TableCell>
                  <TableCell>Durum</TableCell>
                  <TableCell>Basvuran</TableCell>
                  <TableCell>Olusturma Tarihi</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.recentApplications.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} align="center">
                      Henuz basvuru yok.
                    </TableCell>
                  </TableRow>
                ) : (
                  data.recentApplications.map((form) => (
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
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </>
      )}
    </Layout>
  );
}

export default DashboardPage;