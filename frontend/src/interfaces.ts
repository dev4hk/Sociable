export interface IToken {
  exp: number | null;
  iat: number | null;
  id: string | null;
  username: string | null;
}

export interface ILogin {
  email: string;
  password: string;
}

export interface IRegister {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export interface IAuthResponse {
  access_token: string;
  refresh_token: string;
}

export interface IModal {
  handleClose: () => void;
  open: boolean;
}
