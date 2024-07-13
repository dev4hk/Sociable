import { atom } from "recoil";
import { IPost, IProfile, IToken } from "./interfaces";

export const posts = atom<IPost[]>({
  key: "posts",
  default: [],
});

export const profile = atom<IProfile>({
  key: "profile",
  default: {},
});
