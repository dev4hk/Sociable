import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IChat, IMessage } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { useRecoilState, useRecoilValue } from "recoil";
import { chat, chats, chatUser, messages, profile } from "../../atoms";
import { Avatar, Grid } from "@mui/material";
import WestIcon from "@mui/icons-material/West";

import UserSearch from "../../components/search/UserSearch";
import UserChatCard from "../../components/message/UserChatCard";
import { useNavigate } from "react-router-dom";
import Stomp, { Client, Subscription } from "stompjs";
import SockJS from "sockjs-client";
import ChatMessage from "../../components/message/ChatMessage";
import {
  createMessage,
  getAllChats,
  getMessagesInChat,
} from "../../api/chatApi";
import { getFile } from "../../api/fileApi";
import { red } from "@mui/material/colors";
import ChatWindow from "../../components/message/ChatWindow";

const Message = () => {
  const profileAtom = useRecoilValue(profile);
  const navigate = useNavigate();

  const handleHomeClick = () => {
    navigate("/home");
  };

  //////////// Chats //////////////////
  const [currentChat, setCurrentChat] = useRecoilState(chat);
  const [chatsAtom, setChatsAtom] = useRecoilState(chats);
  const [targetUser, setTargetUser] = useRecoilState(chatUser);
  const [subscription, setSubscription] = useState<Subscription>();

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

  const handleUserCardClick = (item: IChat) => {
    subscription?.unsubscribe();
    setCurrentMessages([]);
    setCurrentChat(item);
    const targetUser =
      profileAtom.id === item?.users[0].id ? item?.users[1] : item?.users[0];
    setTargetUser(targetUser);
  };

  ////////////    Web Socket //////////////////

  const [stompClient, setStompClient] = useState<Client>();
  useEffect(() => {
    const sock = new SockJS("http://localhost:8084/ws");
    const stomp = Stomp.over(sock);
    setStompClient(stomp);
    stomp.connect({}, onConnect, onError);
  }, []);

  useEffect(() => {
    if (stompClient && profileAtom && currentChat) {
      const subscription = stompClient.subscribe(
        `/user/${currentChat?.id}/private`,
        onMessageReceive
      );
      setSubscription(subscription);
    }
  }, [currentChat]);

  const sendMessageToServer = (newMessage: any) => {
    if (stompClient && newMessage) {
      console.log("sending...");
      stompClient.send(
        `/app/chat/${currentChat?.id.toString()}`,
        {},
        JSON.stringify(newMessage)
      );
    }
  };

  const onConnect = () => {
    console.log("websocket connected...");
  };

  const onError = (error: any) => {
    console.log("websocket connect error...", error);
  };

  const [currentMessages, setCurrentMessages] = useRecoilState(messages);

  const onMessageReceive = (message: any) => {
    const receivedMessage = JSON.parse(message.body);
    console.log("message received from websocket...", receivedMessage);
    setCurrentMessages((prev) => [...prev, receivedMessage]);
  };

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
          <ChatWindow sendMessageToServer={sendMessageToServer} />
        </Grid>
      </Grid>
    </div>
  );
};

export default Message;
