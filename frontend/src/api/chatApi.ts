import axios from "axios";
import { getToken } from "./authApi";

const BASE_URL = "http://localhost:8888";

export function getAllChats() {
  return axios
    .get(`${BASE_URL}/api/v1/chats`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data.result);
}

export function getMessagesInChat(chatId: number) {
  return axios
    .get(`${BASE_URL}/api/v1/messages/chat/${chatId}`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data.result);
}

export function createChat(userId: number) {
  return axios
    .post(
      `${BASE_URL}/api/v1/chats`,
      { userId: userId },
      {
        headers: { Authorization: `Bearer ${getToken()}` },
      }
    )
    .then((res) => res.data.result);
}

export function createMessage(formData: FormData, chatId: number) {
  return axios
    .post(`${BASE_URL}/api/v1/messages/chat/${chatId}`, formData, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data.result);
}
