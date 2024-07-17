import React, { useEffect, useState } from "react";
import { IChat, IMessage } from "../../interfaces";
import { getAllChats, getMessagesInChat } from "../../api/api";
import { useQuery } from "@tanstack/react-query";
import { useRecoilState, useRecoilValue } from "recoil";
import { chats, profile } from "../../atoms";
import { Avatar, Grid } from "@mui/material";
import WestIcon from "@mui/icons-material/West";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import UserSearch from "../../components/search/UserSearch";
import UserChatCard from "../../components/message/UserChatCard";
import { useNavigate } from "react-router-dom";
import MessageContainer from "../../components/message/MessageContainer";

const Message = () => {
  const profileAtom = useRecoilValue(profile);
  const navigate = useNavigate();
  const handleUserCardClick = (item: IChat) => {
    setCurrentChat(item);
  };
  const handleHomeClick = () => {
    navigate("/home");
  };

  // Chats
  const [currentChat, setCurrentChat] = useState<IChat>();
  const [chatsAtom, setChatsAtom] = useRecoilState(chats);
  const {
    data: chatsData,
    isLoading: isChatsLoading,
    isSuccess: isChatsSuccess,
  } = useQuery<IChat[]>({
    queryKey: ["chats"],
    queryFn: getAllChats,
  });

  useEffect(() => {
    if (isChatsSuccess) {
      setChatsAtom(chatsData);
    }
  }, [chatsData]);

  console.log(chatsData);

  return (
    <div className="text-white">
      <Grid container className="h-screen overflow-y-hidden">
        <Grid item xs={3} className="px-5">
          <div className="flex h-full justify-between space-x-2">
            <div className="w-full">
              <div
                className="flex space-x-4 items-center py-5 cursor-pointer"
                onClick={handleHomeClick}
              >
                <WestIcon />
                <h1 className="text-xl font-bold">Home</h1>
              </div>
              <div className="h-[83vh]">
                <div>
                  <UserSearch />
                </div>
                <div className="h-full space-y-4 mt-5 overflow-y-scroll no-scrollbar">
                  {isChatsSuccess &&
                    chatsAtom.map((chat, index) => (
                      <div
                        key={index}
                        onClick={() => handleUserCardClick(chat)}
                      >
                        <UserChatCard chat={chat} />
                      </div>
                    ))}
                </div>
              </div>
            </div>
          </div>
        </Grid>
        <Grid item xs={9} className="h-full">
          {currentChat ? (
            <div>
              <div className="flex justify-between items-center border-l p-5">
                <div className="flex items-center space-x-3">
                  <Avatar src="https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&w=800" />
                  <p>
                    {profileAtom?.id === currentChat.users[0].id
                      ? currentChat.users[1].firstname +
                        " " +
                        currentChat.users[1].lastname
                      : currentChat.users[0].firstname +
                        " " +
                        currentChat.users[0].lastname}
                  </p>
                </div>
              </div>
              <MessageContainer chatId={currentChat.id} />
            </div>
          ) : (
            <div className="h-full space-y-5 flex flex-col justify-center items-center">
              <ChatBubbleOutlineIcon sx={{ fontSize: "15rem" }} />
              <p className="text-xl font-semibold">No Chat Selected</p>
            </div>
          )}
        </Grid>
      </Grid>
    </div>
  );
};

export default Message;
