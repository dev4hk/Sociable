import React, { useEffect } from "react";
import { IProfile } from "../../interfaces";
import { Avatar, Card, CardHeader } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";
import { useLocation, useNavigate } from "react-router-dom";
import { red } from "@mui/material/colors";

interface IUserSearchCardProps {
  user: IProfile;
  handleUserClick: (id: number) => void;
}

const UserSearchCard = ({ user, handleUserClick }: IUserSearchCardProps) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { data, refetch } = useQuery<Blob>({
    queryKey: ["otherUserProfile", user.id],
    queryFn: () => getFile(user.fileInfo?.filePath!),
    enabled: false,
  });

  useEffect(() => {
    if (user.fileInfo?.filePath) {
      refetch();
    }
  });

  const handleClick = () => {
    if (location.pathname.includes("/message")) {
      handleUserClick(user.id!);
    } else {
      navigate(`/home/profile/${user.id}`);
    }
  };

  return (
    <Card
      key={"user" + user.id}
      className="cursor-pointer hover:text-sky-400"
      onClick={handleClick}
    >
      <CardHeader
        title={user.firstname + " " + user.lastname}
        subheader={`@${user.firstname?.toLowerCase()}_${user.lastname?.toLowerCase()}`}
        avatar={
          data ? (
            <Avatar src={`data:${user.fileInfo?.fileType};base64,${data}`} />
          ) : (
            <Avatar sx={{ bgcolor: red[500] }}>
              {user.firstname?.substring(0, 1).toUpperCase()}
            </Avatar>
          )
        }
      />
    </Card>
  );
};

export default UserSearchCard;
