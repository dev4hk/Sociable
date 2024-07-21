import axios from "axios";
import { getToken } from "./authApi";

const BASE_URL = "http://localhost:8888";

export function getFile(filePath: string) {
  return axios
    .get(`${BASE_URL}/api/v1/files`, {
      headers: { Authorization: `Bearer ${getToken()}` },
      params: { filePath: filePath },
    })
    .then((res) => res.data.result);
}
