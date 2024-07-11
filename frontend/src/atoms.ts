import { atom } from "recoil";
import { IToken } from "./interfaces";

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
