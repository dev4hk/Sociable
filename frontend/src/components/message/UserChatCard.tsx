import React, { useEffect } from "react";
import { useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { Avatar, Card, CardHeader, IconButton } from "@mui/material";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { IChat } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";
import { red } from "@mui/material/colors";

interface IUserChatCardProps {
  chat: IChat;
}

const UserChatCard = ({ chat }: IUserChatCardProps) => {
  const profileAtom = useRecoilValue(profile);
  const targetUser =
    profileAtom.id === chat.users[0].id ? chat.users[1] : chat.users[0];

  const { data, refetch } = useQuery({
    queryKey: ["chat", chat.id, targetUser.id],
    queryFn: () => getFile(targetUser.fileInfo.filePath),
    enabled: false,
  });

  useEffect(() => {
    if (targetUser.fileInfo) {
      refetch();
    }
  }, [targetUser]);

  return (
    <Card className="cursor-pointer">
      <CardHeader
        title={targetUser.firstname + " " + targetUser.lastname}
        subheader="new message"
        action={
          <IconButton>
            <MoreHorizIcon />
          </IconButton>
        }
        avatar={
          targetUser.fileInfo ? (
            <Avatar
              src={`data:${targetUser.fileInfo?.fileType};base64,${data}`}
              sx={{
                width: "3.5rem",
                height: "3.5rem",
                fontSize: "1.5rem",
              }}
            />
          ) : (
            <Avatar sx={{ bgcolor: red[500] }}>
              {targetUser.firstname[0].toUpperCase()}
            </Avatar>
          )
        }
      ></CardHeader>
    </Card>
  );
};

export default UserChatCard;
