import React from "react";
import "./App.css";
import Authentication from "./pages/authentication/Authentication";
import { ThemeProvider } from "@emotion/react";
import { darkTheme } from "./theme/darkTheme";
import { Navigate, Route, Routes } from "react-router-dom";
import AuthenticatedRoute from "./auth/AuthenticatedRoute";
import Login from "./pages/authentication/Login";
import { isTokenValid } from "./service/AuthenticationService";
import HomePage from "./pages/home/HomePage";
import Message from "./pages/message/Message";

function App() {
  const hasValidToken = () => {
    return isTokenValid(localStorage.getItem("token"));
  };

  return (
    <ThemeProvider theme={darkTheme}>
      <Routes>
        <Route path="/home/message" element={<Message />} />
        <Route
          path="/home/*"
          element={
            <AuthenticatedRoute>
              <HomePage />
            </AuthenticatedRoute>
          }
        />
        <Route
          path="/"
          element={hasValidToken() ? <HomePage /> : <Authentication />}
        />
      </Routes>
    </ThemeProvider>
  );
}

export default App;
