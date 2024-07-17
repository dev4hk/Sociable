import axios from "axios";
import { IChangeUserInfo, ILogin, IPost, IRegister } from "../interfaces";

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

export function getAllPosts() {
  return axios.get(`${BASE_URL}/api/v1/posts?sort=registeredAt,desc`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
}

export function getAllPostByUserId(userId: number) {
  return axios.get(
    `${BASE_URL}/api/v1/posts/user/${userId}?sort=registeredAt,desc`,
    {
      headers: { Authorization: `Bearer ${getToken()}` },
    }
  );
}

export function getToken() {
  return localStorage.getItem("token");
}

export function createPost(data: FormData) {
  return axios
    .post(`${BASE_URL}/api/v1/posts`, data, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
}

export function getCommentsByPost<ICommentsResponse>(
  postId: number,
  page: number,
  size: number
) {
  return axios
    .get<ICommentsResponse>(
      `${BASE_URL}/api/v1/comments/post/${postId}?sort=registeredAt,desc`,
      {
        headers: { Authorization: `Bearer ${getToken()}` },
      }
    )
    .then((res) => res.data);
}

export function createComment(request: any, postId: number) {
  return axios
    .post(`${BASE_URL}/api/v1/comments/post/${postId}`, request, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
}

export function getUserProfile(token: string) {
  return axios
    .get(`${BASE_URL}/api/v1/users/profile`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
}

export function getAnotherUserInfo(id: number, token: string) {
  return axios
    .get(`${BASE_URL}/api/v1/users/${id}/profile`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
}

export function changeUserInfo(data: IChangeUserInfo) {
  return axios
    .put(`${BASE_URL}/api/v1/users/change/info`, data, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
}

export function logout() {
  return axios.get(`${BASE_URL}/api/v1/auth/logout`, {
    headers: { Authorization: `Bearer ${getToken()}` },
  });
}

export function likeUnlikePost(postId: number) {
  return axios.patch(
    `${BASE_URL}/api/v1/posts/${postId}/like`,
    {},
    {
      headers: { Authorization: `Bearer ${getToken()}` },
    }
  );
}

export function getPostFile(filePath: string) {
  return axios
    .get(`${BASE_URL}/api/v1/posts/file`, {
      headers: { Authorization: `Bearer ${getToken()}` },
      params: { filePath: filePath },
    })
    .then((res) => res.data.result);
}

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

export function getOtherUsers(query: string) {
  return axios
    .get(`${BASE_URL}/api/v1/users?query=${query}`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
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

export function getMessageFile(filePath: string) {
  return axios
    .get(`${BASE_URL}/api/v1/messages/file`, {
      headers: { Authorization: `Bearer ${getToken()}` },
      params: { filePath: filePath },
    })
    .then((res) => res.data.result);
}

export function createMessage(formData: FormData, chatId: number) {
  return axios
    .post(`${BASE_URL}/api/v1/messages/chat/${chatId}`, formData, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data.result);
}
