import { atom } from "recoil";
import {
  IChat,
  IMessage,
  INotification,
  IPost,
  IProfile,
  IToken,
  IUser,
} from "./interfaces";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

export const posts = atom<IPost[]>({
  key: "postsAtom",
  default: [],
  effects_UNSTABLE: [persistAtom],
});

export const profile = atom<IProfile>({
  key: "profileAtom",
  default: {},
  effects_UNSTABLE: [persistAtom],
});

export const user = atom<IProfile>({
  key: "userAtom",
  default: {},
  effects_UNSTABLE: [persistAtom],
});

export const chats = atom<IChat[]>({
  key: "chatsAtom",
  default: [],
});

export const chat = atom<IChat>({
  key: "currentChat",
  default: undefined,
});

export const chatUser = atom<IUser>({
  key: "chatuser",
  default: undefined,
});

export const messages = atom<IMessage[]>({
  key: "messages",
  default: [],
});

export const savedPosts = atom<IPost[]>({
  key: "savedPostsAtom",
  default: [],
});

export const profileImage = atom<Blob | undefined>({
  key: "profileImageAtom",
  default: undefined,
  effects_UNSTABLE: [persistAtom],
});

export const notifications = atom<INotification[]>({
  key: "notifications",
  default: [],
  effects_UNSTABLE: [persistAtom],
});
