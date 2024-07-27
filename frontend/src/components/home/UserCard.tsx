import { Avatar, Button, CardHeader } from "@mui/material";
import { red } from "@mui/material/colors";
import React, { useEffect, useState } from "react";
import { IProfile, IUser } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";
import { followUser, getAnotherUserInfo } from "../../api/userApi";
import { useRecoilState, useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { getToken } from "../../api/authApi";
import { useLocation, useNavigate } from "react-router-dom";

interface IUserCardProps {
  user?: IProfile;
  userId?: number;
  handleClose?: () => void;
}

const UserCard = ({ user, userId, handleClose }: IUserCardProps) => {
  const [userAtom, setUserAtom] = useRecoilState(profile);
  const [userState, setUserState] = useState<IProfile>();
  const navigate = useNavigate();
  const location = useLocation();

  const {
    data: userData,
    refetch: refetchUser,
    error,
    isError,
  } = useQuery({
    queryKey: ["user", "profile", userId],
    queryFn: () => getAnotherUserInfo(userId!),
    enabled: false,
  });

  useEffect(() => {
    if (user) {
      setUserState(user);
    } else {
      refetchUser().then((res) => setUserState(res.data));
    }
  }, []);

  const { data, refetch } = useQuery<Blob>({
    queryKey: ["user", "profile", "image", userState?.id],
    queryFn: () => getFile(userState?.fileInfo?.filePath!),
    enabled: false,
  });

  useEffect(() => {
    if (userState?.fileInfo) {
      refetch();
    }
  }, [userState]);

  const handleFollow = () => {
    followUser(userState?.id!).then((res) => setUserAtom(res.data));
  };

  const handleUserClick = () => {
    if (location.pathname.includes("profile")) {
      handleClose!();
    }
    navigate(`/home/profile/${userState?.id}`);
  };

  return (
    <div>
      <CardHeader
        className="text-white cursor-pointer hover:text-sky-400"
        avatar={
          userState?.fileInfo && data ? (
            <Avatar
              src={`data:${userState?.fileInfo?.fileType};base64,${data}`}
            />
          ) : (
            <Avatar sx={{ bgcolor: red[500] }}>
              {userState?.firstname?.substring(0, 1).toUpperCase()}
            </Avatar>
          )
        }
        action={
          userAtom.id === userState?.id ? (
            ""
          ) : (
            <Button size="small" onClick={handleFollow}>
              {userAtom.followings?.includes(userState?.id!)
                ? "UNFOLLOW"
                : "FOLLOW"}
            </Button>
          )
        }
        title={
          <p onClick={handleUserClick}>
            {userState?.firstname + " " + userState?.lastname}
          </p>
        }
        subheader={`@${userState?.firstname?.toLowerCase()}_${userState?.lastname?.toLowerCase()}`}
      />
    </div>
  );
};

export default UserCard;
