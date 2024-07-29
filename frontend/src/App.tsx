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
import { useRecoilValue } from "recoil";
import { profile } from "./atoms";

function App() {
  const hasValidToken = () => {
    return isTokenValid(localStorage.getItem("token"));
  };

  const userAtom = useRecoilValue(profile);

  return (
    <ThemeProvider theme={darkTheme}>
      <Routes>
        <Route
          path="/*"
          element={
            userAtom && hasValidToken() ? <HomePage /> : <Authentication />
          }
        />
        <Route
          path="/message"
          element={
            userAtom && hasValidToken() ? <Message /> : <Authentication />
          }
        />
        <Route path="/*" element={<Authentication />} />
      </Routes>
    </ThemeProvider>
  );
}

export default App;
