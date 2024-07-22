import { Avatar, Button, CardHeader } from "@mui/material";
import { red } from "@mui/material/colors";
import React, { useEffect } from "react";
import { IProfile, IUser } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";

interface IUserCardProps {
  user: IProfile;
}

const UserCard = ({ user }: IUserCardProps) => {
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
        action={<Button size="small">Follow</Button>}
        title={user.firstname + " " + user.lastname}
        subheader={`@${user.firstname?.toLowerCase()}_${user.lastname?.toLowerCase()}`}
      />
    </div>
  );
};

export default UserCard;
