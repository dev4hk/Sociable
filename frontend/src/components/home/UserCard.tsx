import { Avatar, Button, CardHeader } from "@mui/material";
import { red } from "@mui/material/colors";
import React, { useEffect } from "react";
import { IProfile, IUser } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";
import { followUser } from "../../api/userApi";
import { useRecoilState, useRecoilValue } from "recoil";
import { profile } from "../../atoms";

interface IUserCardProps {
  user: IProfile;
}

const UserCard = ({ user }: IUserCardProps) => {
  const [userAtom, setUserAtom] = useRecoilState(profile);

  const { data, refetch } = useQuery<Blob>({
    queryKey: ["user", "profile", "image", user.id],
    queryFn: () => getFile(user.fileInfo?.filePath!),
    enabled: false,
  });

  useEffect(() => {
    if (user.fileInfo) {
      refetch();
    }
  }, [user]);

  const handleFollow = () => {
    followUser(user.id!).then((res) => setUserAtom(res.data));
  };

  return (
    <div>
      <CardHeader
        avatar={
          user.fileInfo && data ? (
            <Avatar src={`data:${user?.fileInfo?.fileType};base64,${data}`} />
          ) : (
            <Avatar sx={{ bgcolor: red[500] }}>
              {user.firstname?.substring(0, 1).toUpperCase()}
            </Avatar>
          )
        }
        action={
          <Button size="small" onClick={handleFollow}>
            {userAtom.followings?.includes(user.id!) ? "UNFOLLOW" : "FOLLOW"}
          </Button>
        }
        title={user.firstname + " " + user.lastname}
        subheader={`@${user.firstname?.toLowerCase()}_${user.lastname?.toLowerCase()}`}
      />
    </div>
  );
};

export default UserCard;
