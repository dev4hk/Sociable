import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { useRecoilState } from "recoil";
import { chat, chatUser, messages } from "../../atoms";
import { Avatar } from "@mui/material";
import { createMessage, getMessagesInChat } from "../../api/chatApi";
import { IMessage } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";
import { red } from "@mui/material/colors";
import ChatMessage from "./ChatMessage";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";

interface IChatWindowProps {
  sendMessageToServer: (item: any) => void;
}

const ChatWindow = ({ sendMessageToServer }: IChatWindowProps) => {
  const [currentChat, setCurrentChat] = useRecoilState(chat);
  const [targetUser, setTargetUser] = useRecoilState(chatUser);
  const [currentMessages, setCurrentMessages] = useRecoilState(messages);

  const chatContainerRef = useRef<any>(null);
  useEffect(() => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop =
        chatContainerRef.current.scrollHeight;
    }
  }, [currentMessages]);

  const {
    data: targetUserProfileImage,
    refetch: refetchTargetUserProfileImage,
  } = useQuery({
    queryKey: ["chatRoom", targetUser?.id],
    queryFn: () => getFile(targetUser?.fileInfo.filePath!),
    enabled: false,
  });

  useEffect(() => {
    if (targetUser?.fileInfo) {
      refetchTargetUserProfileImage();
    }
  }, [targetUser]);

  /////////////// Input //////////////////////
  const [content, setContent] = useState("");
  const [selectedFile, setSelectedFile] = useState<any>();

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

  //////////// Messages ////////////////////
  const {
    data: messagesData,
    isLoading: isMessagesDataLoading,
    isSuccess: isMessagesDataSuccess,
  } = useQuery<IMessage[]>({
    queryKey: ["messages", "chat", currentChat?.id],
    queryFn: () => getMessagesInChat(currentChat!.id),
  });

  useEffect(() => {
    if (messagesData) {
      setCurrentMessages(messagesData);
    }
  }, [messagesData]);

  return (
    <>
      {currentChat ? (
        <div>
          <div className="flex justify-between items-center border-l p-5">
            <div className="flex items-center space-x-3">
              {targetUser?.fileInfo ? (
                <Avatar
                  src={`data:${targetUser?.fileInfo?.fileType};base64,${targetUserProfileImage}`}
                  sx={{
                    width: "3.5rem",
                    height: "3.5rem",
                    fontSize: "1.5rem",
                  }}
                />
              ) : (
                <Avatar sx={{ bgcolor: red[500] }}>
                  {targetUser?.firstname[0].toUpperCase()}
                </Avatar>
              )}
              <p>{targetUser?.firstname + " " + targetUser?.lastname}</p>
            </div>
          </div>
          <div
            className="no-scrollbar overflow-y-scroll h-[82vh] px-2 space-y-5 py-5"
            ref={chatContainerRef}
          >
            {currentMessages.map((message, index) => (
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
    </>
  );
};

export default ChatWindow;
