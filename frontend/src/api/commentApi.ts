import axios from "axios";
import { getToken } from "./authApi";

const BASE_URL = "http://localhost:8888";

export function getCommentsByPost<ICommentsResponse>(
  postId: number,
  page: number,
  size: number
) {
  return axios
    .get<ICommentsResponse>(
      `${BASE_URL}/api/v1/comments/post/${postId}?sort=registeredAt,desc`,
      {
        headers: { Authorization: getToken() },
      }
    )
    .then((res) => res.data);
}

export function createComment(request: any, postId: number) {
  return axios
    .post(`${BASE_URL}/api/v1/comments/post/${postId}`, request, {
      headers: { Authorization: getToken() },
    })
    .then((res) => res.data);
}
