import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { CssBaseline, ThemeProvider } from "@mui/material";
import theme from "./theme";
import { AuthProvider } from "./context/AuthContext";
import PrivateRoute from "./components/PrivateRoute";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import DashboardPage from "./pages/DashboardPage";
import FormListPage from "./pages/FormListPage";
import NewFormPage from "./pages/NewFormPage";
import EditFormPage from "./pages/EditFormPage";
import FormDetailPage from "./pages/FormDetailPage";
import ProfilePage from "./pages/ProfilePage";
import UsersPage from "./pages/UsersPage";

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            <Route
              path="/dashboard"
              element={
                <PrivateRoute>
                  <DashboardPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/forms"
              element={
                <PrivateRoute>
                  <FormListPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/forms/new"
              element={
                <PrivateRoute>
                  <NewFormPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/forms/:id/edit"
              element={
                <PrivateRoute>
                  <EditFormPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/forms/:id"
              element={
                <PrivateRoute>
                  <FormDetailPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <PrivateRoute>
                  <ProfilePage />
                </PrivateRoute>
              }
            />
            <Route
              path="/users"
              element={
                <PrivateRoute requiredRole="ADMIN">
                  <UsersPage />
                </PrivateRoute>
              }
            />

            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;