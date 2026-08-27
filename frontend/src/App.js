import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import PrivateRoute from "./components/PrivateRoute";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import FormListPage from "./pages/FormListPage";
import NewFormPage from "./pages/NewFormPage";
import FormDetailPage from "./pages/FormDetailPage";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
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
            path="/forms/:id"
            element={
              <PrivateRoute>
                <FormDetailPage />
              </PrivateRoute>
            }
          />
          <Route path="/" element={<Navigate to="/forms" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;