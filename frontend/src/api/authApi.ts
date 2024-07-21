import axios from "axios";
import { ILogin, IRegister } from "../interfaces";

const BASE_URL = "http://localhost:8888";

export function registerUser(data: IRegister) {
  return axios
    .post(`${BASE_URL}/api/v1/auth/register`, data)
    .then((res) => res.data);
}

export function loginUser(data: ILogin) {
  return axios
    .post(`${BASE_URL}/api/v1/auth/authenticate`, data)
    .then((res) => res.data);
}

export function getToken() {
  return localStorage.getItem("token");
}

export function logout() {
  return axios.get(`${BASE_URL}/api/v1/auth/logout`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
}
