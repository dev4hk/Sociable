import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IChat, IMessage } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { useRecoilState, useRecoilValue } from "recoil";
import { chats, profile } from "../../atoms";
import { Avatar, Grid } from "@mui/material";
import WestIcon from "@mui/icons-material/West";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";
import UserSearch from "../../components/search/UserSearch";
import UserChatCard from "../../components/message/UserChatCard";
import { useNavigate } from "react-router-dom";
import Stomp, { Client } from "stompjs";
import SockJS from "sockjs-client";
import ChatMessage from "../../components/message/ChatMessage";
import {
  createMessage,
  getAllChats,
  getMessagesInChat,
} from "../../api/chatApi";

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
      refetchMessages();
    }
  }, [chatsData]);

  const [content, setContent] = useState("");
  const [selectedFile, setSelectedFile] = useState<any>();
  // const [messagesAtom, setMessagesAtom] = useRecoilState(messages);
  const [messages, setMessages] = useState<IMessage[]>([]);
  const {
    data: messagesData,
    isLoading: isMessagesDataLoading,
    isSuccess: isMessagesDataSuccess,
    refetch: refetchMessages,
  } = useQuery<IMessage[]>({
    queryKey: ["messages", currentChat?.id],
    queryFn: () => getMessagesInChat(currentChat!.id),
    enabled: false,
  });

  useEffect(() => {
    if (isMessagesDataSuccess && messagesData) {
      // setMessagesAtom(messagesData);
      setMessages(messagesData);
    }
  }, [messagesData]);

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
    }
  });

  const sendMessageToServer = (newMessage: any) => {
    if (stompClient && newMessage) {
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

  const onMessageReceive = (message: any) => {
    console.log("received message...", message);
    const receivedMessage = JSON.parse(message.body);
    console.log("message received from websocket...", receivedMessage);
    setMessages([...messages, receivedMessage]);
  };

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setContent(e.currentTarget.value);
  };

  const handleSelectFile = ({
    currentTarget,
  }: ChangeEvent<HTMLInputElement>) => {
    if (currentTarget.files) {
      setSelectedFile(currentTarget.files[0]);
    }
  };

  const handleCreateMessage = () => {
    const formData = new FormData();
    formData.append("content", content);
    formData.append("file", selectedFile);
    createMessage(formData, currentChat!.id).then((res) =>
      // setMessagesAtom((prev) => [...prev, res])
      sendMessageToServer(res)
    );
    setContent("");
    setSelectedFile("");
  };

  const chatContainerRef = useRef<any>(null);
  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  }, [messages]);

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
              <div
                className="no-scrollbar overflow-y-scroll h-[82vh] px-2 space-y-5 py-5"
                ref={chatContainerRef}
              >
                {messages.map((message, index) => (
                  <ChatMessage key={index} message={message} />
                ))}
              </div>
              <div className="sticky bottom-0 border-l">
                {selectedFile && selectedFile.type.includes("image") && (
                  <img
                    className="w-[5rem] h-[5rem] object-cover px-2"
                    src={URL.createObjectURL(selectedFile)}
                    alt=""
                  />
                )}
                {selectedFile && selectedFile.type.includes("video") && (
                  <video className="w-[5rem] h-[5rem] object-cover px-2">
                    <source
                      src={URL.createObjectURL(selectedFile)}
                      type={selectedFile.type}
                    />
                  </video>
                )}
                <div className="py-5 flex items-center justify-center space-x-5">
                  <input
                    type="text"
                    className="bg-transparent border border-[#3b4054] rounded-full w-[90%] py-3 px-5"
                    placeholder="Type Message..."
                    onChange={handleInputChange}
                    value={content}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        handleCreateMessage();
                      }
                    }}
                  />
                  <div>
                    <input
                      type="file"
                      accept="image/*,video/*"
                      onChange={handleSelectFile}
                      className="hidden"
                      id="file-input"
                    />
                    <label htmlFor="file-input" className="cursor-pointer">
                      <AddPhotoAlternateIcon />
                    </label>
                  </div>
                </div>
              </div>
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
