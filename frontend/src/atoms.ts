import { atom } from "recoil";
import { IChat, IMessage, IPost, IProfile, IToken } from "./interfaces";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

export const posts = atom<IPost[]>({
  key: "posts",
  default: [],
});

export const profile = atom<IProfile>({
  key: "profile",
  default: {},
  effects_UNSTABLE: [persistAtom],
});

export const user = atom<IProfile>({
  key: "user",
  default: {},
  effects_UNSTABLE: [persistAtom],
});

export const chats = atom<IChat[]>({
  key: "chats",
  default: [],
});

export const messages = atom<IMessage[]>({
  key: "messages",
  default: [],
});

export const savedPosts = atom<IPost[]>({
  key: "savedPosts",
  default: [],
});
