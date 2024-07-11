import axios from "axios";
import { ILogin, IRegister } from "../interfaces";

const BASE_URL = "http://localhost:8888";

export function registerUser(data: IRegister) {
  return axios.post(`${BASE_URL}/api/v1/auth/register`, data);
}

export function loginUser(data: ILogin) {
  return axios.post(`${BASE_URL}/api/v1/auth/authenticate`, data);
}
