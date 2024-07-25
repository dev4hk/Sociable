import axios from "axios";
import { getToken } from "./authApi";

const BASE_URL = "http://localhost:8888";

export function getNotification() {
  return axios
    .get(`${BASE_URL}/api/v1/notifications?size=10&sort=id,desc`, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data.result.content);
}
