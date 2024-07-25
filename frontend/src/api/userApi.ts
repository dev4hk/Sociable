import axios from "axios";
import { getToken } from "./authApi";
import { IChangeUserInfo } from "../interfaces";

const BASE_URL = "http://localhost:8888";

export function getUserProfile(token: string) {
  return axios
    .get(`${BASE_URL}/api/v1/users/profile`, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data);
}

export function getAnotherUserInfo(id: number) {
  return axios
    .get(`${BASE_URL}/api/v1/users/${id}/profile`, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data);
}

export function changeUserInfo(data: FormData) {
  return axios
    .put(`${BASE_URL}/api/v1/users/change/info`, data, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data);
}

export function getOtherUsers(query: string) {
  return axios
    .get(`${BASE_URL}/api/v1/users?query=${query}`, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data);
}

export function followUser(userId: number) {
  return axios
    .put(
      `${BASE_URL}/api/v1/users/follow/${userId}`,
      {},
      {
        headers: { Authorization: getToken() },
      }
    )
    .then((res) => res);
}

export function getUserSuggestions() {
  return axios
    .get(`${BASE_URL}/api/v1/users/suggestions`, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data);
}
