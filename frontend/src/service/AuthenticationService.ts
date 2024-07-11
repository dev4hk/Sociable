import { jwtDecode } from "jwt-decode";
import { IToken } from "../interfaces";

export function isTokenValid(token: string | null) {
  if (token === null) {
    return false;
  }
  const decodedToken: IToken = jwtDecode(token);
  const expiration = decodedToken.exp;
  const now = new Date();
  if (expiration && expiration < now.getTime() / 1000) {
    return false;
  }
  return true;
}
