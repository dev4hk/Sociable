import { atom } from "recoil";
import { IChat, IMessage, IPost, IProfile, IToken } from "./interfaces";

export const posts = atom<IPost[]>({
  key: "posts",
  default: [],
});

export const profile = atom<IProfile>({
  key: "profile",
  default: {},
});

export const chats = atom<IChat[]>({
  key: "chats",
  default: [],
});

export const messages = atom<IMessage[]>({
  key: "messages",
  default: [],
});
