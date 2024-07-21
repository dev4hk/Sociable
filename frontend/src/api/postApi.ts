import axios from "axios";
import { getToken } from "./authApi";

const BASE_URL = "http://localhost:8888";

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

export function createPost(data: FormData) {
  return axios
    .post(`${BASE_URL}/api/v1/posts`, data, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data);
}

export function likePost(postId: number) {
  return axios.patch(
    `${BASE_URL}/api/v1/posts/${postId}/like`,
    {},
    {
      headers: { Authorization: `Bearer ${getToken()}` },
    }
  );
}

export function savePost(postId: number) {
  return axios
    .patch(
      `${BASE_URL}/api/v1/posts/save/${postId}`,
      {},
      {
        headers: { Authorization: `Bearer ${getToken()}` },
      }
    )
    .then((res) => res);
}

export function getSavedPost() {
  return axios
    .get(`${BASE_URL}/api/v1/posts/saved`, {
      headers: { Authorization: `Bearer ${getToken()}` },
    })
    .then((res) => res.data.result);
}
