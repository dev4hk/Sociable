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

export interface IProfile {
  id?: number;
  firstname?: string;
  lastname?: string;
  email?: string;
}

export interface IAuthResponse {
  access_token: string;
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

export interface IChangeUserInfo {
  firstname: string;
  lastname: string;
}

export interface IPost {
  id: number;
  body: string;
  fileInfo: IFileInfo;
  userId: number;
  firstname: string;
  lastname: string;
  likedBy: Array<number>;
  registeredAt: string;
  updatedAt: string;
  deletedAt: string;
}

export interface IGetAllPosts {
  resultCode: string;
  result: {
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
    size: number;
    content: IPost[];
    number: number;
    sort: [];
    numberOfElements: number;
    pageable: {
      pageNumber: number;
      pageSize: number;
      sort: [];
      offset: number;
      unpaged: boolean;
      paged: boolean;
    };
    empty: boolean;
  };
}

export interface IPostRequest {
  body: string;
  file: Blob;
}

export interface IPostForm {
  errors: string;
  body: string;
  image: Blob;
  video: Blob;
}

export interface IComment {
  id: number;
  userId: number;
  firstname: string;
  lastname: string;
  email: string;
  postId: number;
  comment: string;
}

export interface ICommentsResponse {
  resultCode: string;
  result: {
    totalPages: number;
    totalElements: number;
    first: boolean;
    last: boolean;
    size: number;
    content: IComment[];
    number: number;
    sort: [];
    numberOfElements: number;
    pageable: {
      pageNumber: number;
      pageSize: number;
      sort: [];
      offset: number;
      paged: boolean;
      unpaged: boolean;
    };
    empty: boolean;
  };
}

export interface IFile {
  file: string;
}

export interface IChat {
  id: number;
  users: IUser[];
}

export interface IUser {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
}

export interface IMessage {
  id: number;
  content: string;
  fileInfo: IFileInfo;
  user: IUser;
}

export interface IFileInfo {
  filePath: string;
  fileType: string;
}
