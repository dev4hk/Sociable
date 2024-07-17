import React from "react";
import { useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { Avatar, Card, CardHeader, IconButton } from "@mui/material";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import { IChat } from "../../interfaces";

interface IUserChatCardProps {
  chat: IChat;
}

const UserChatCard = ({ chat }: IUserChatCardProps) => {
  const profileAtom = useRecoilValue(profile);
  return (
    <Card className="cursor-pointer">
      <CardHeader
        title={
          profileAtom?.id === chat.users[0].id
            ? chat.users[1].firstname + " " + chat.users[1].lastname
            : chat.users[0].firstname + " " + chat.users[0].lastname
        }
        subheader="new message"
        action={
          <IconButton>
            <MoreHorizIcon />
          </IconButton>
        }
        avatar={
          <Avatar
            src="https://images.pexels.com/photos/428364/pexels-photo-428364.jpeg?auto=compress&cs=tinysrgb&w=800"
            sx={{
              width: "3.5rem",
              height: "3.5rem",
              fontSize: "1.5rem",
              backgroundColor: "#191c29",
              color: "rgb(88, 199, 250)",
            }}
          />
        }
      ></CardHeader>
    </Card>
  );
};

export default UserChatCard;
