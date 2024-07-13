import axios from "axios";
import { ILogin, IPost, IRegister } from "../interfaces";

const BASE_URL = "http://localhost:8888";

export function registerUser(data: IRegister) {
  return axios.post(`${BASE_URL}/api/v1/auth/register`, data);
}

export function loginUser(data: ILogin) {
  return axios.post(`${BASE_URL}/api/v1/auth/authenticate`, data);
}

export function getAllPosts(page: number, size: number) {
  return axios.get(
    `${BASE_URL}/api/v1/posts?page=${page}&size=${size}&sort=registeredAt,desc`,
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
