import { useQuery } from "@tanstack/react-query";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IMessage } from "../../interfaces";
import { createMessage, getMessagesInChat } from "../../api/api";
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";
import { useRecoilState } from "recoil";
import { messages } from "../../atoms";
import ChatMessage from "./ChatMessage";

interface IMessageContinerProp {
  chatId: number;
}

const MessageContainer = ({ chatId }: IMessageContinerProp) => {
  const [content, setContent] = useState("");
  const [selectedFile, setSelectedFile] = useState<any>();
  const [messagesAtom, setMessagesAtom] = useRecoilState(messages);
  const {
    data: messagesData,
    isLoading: isMessagesDataLoading,
    isSuccess: isMessagesDataSuccess,
  } = useQuery<IMessage[]>({
    queryKey: ["messages", chatId],
    queryFn: () => getMessagesInChat(chatId),
  });

  useEffect(() => {
    if (isMessagesDataSuccess) {
      setMessagesAtom(messagesData);
    }
  }, [messagesData]);

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
    createMessage(formData, chatId).then((res) =>
      setMessagesAtom((prev) => [...prev, res])
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
  }, [messagesAtom]);

  return (
    <>
      <div
        className="no-scrollbar overflow-y-scroll h-[82vh] px-2 space-y-5 py-5"
        ref={chatContainerRef}
      >
        {messagesAtom.map((message, index) => (
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
    </>
  );
};

export default MessageContainer;
