import React from "react";
import "./App.css";
import Authentication from "./pages/authentication/Authentication";
import { ThemeProvider } from "@emotion/react";
import { darkTheme } from "./theme/darkTheme";
import { Navigate, Route, Routes } from "react-router-dom";
import AuthenticatedRoute from "./auth/AuthenticatedRoute";
import Home from "./pages/home/Home";
import Login from "./pages/authentication/Login";
import { isTokenValid } from "./service/AuthenticationService";

function App() {
  const hasValidToken = () => {
    return isTokenValid(localStorage.getItem("token"));
  };

  return (
    <ThemeProvider theme={darkTheme}>
      <Routes>
        <Route
          path="/home/*"
          element={
            <AuthenticatedRoute>
              <Home />
            </AuthenticatedRoute>
          }
        />
        {/* <Route path="/message" element={<Message />} /> */}
        <Route
          path="/*"
          element={
            hasValidToken() ? (
              <Navigate to={"/home"} replace />
            ) : (
              <Authentication />
            )
          }
        />
      </Routes>
    </ThemeProvider>
  );
}

export default App;
