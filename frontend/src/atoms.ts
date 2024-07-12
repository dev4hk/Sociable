import { atom } from "recoil";
import { IPost, IToken } from "./interfaces";

export const token = atom({
  key: "token",
  default: null,
});

export const decodedToken = atom<IToken>({
  key: "decodedToken",
  default: {
    exp: null,
    iat: null,
    id: null,
    username: null,
  },
});

export const posts = atom<IPost[]>({
  key: "posts",
  default: [],
});
