import { CardMedia } from "@mui/material";
import React, { useEffect } from "react";
import { IFile, IMessage } from "../../interfaces";
import { useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { getMessageFile } from "../../api/api";
import { useQuery } from "@tanstack/react-query";

interface IChatMessageProp {
  message: IMessage;
}

const ChatMessage = ({ message }: IChatMessageProp) => {
  const profileValue = useRecoilValue(profile);

  const {
    data: fileData,
    isLoading: isFileDataLoading,
    isSuccess: isFileDataSuccess,
    refetch: refetchFileData,
  } = useQuery<IFile>({
    queryKey: ["file", message.id],
    queryFn: () => getMessageFile(message.filePath),
    enabled: false,
  });

  useEffect(() => {
    if (message.filePath) {
      refetchFileData();
    }
  }, []);

  return (
    <div
      className={`flex text-white ${
        message?.user?.id !== profileValue.id ? "justify-start" : "justify-end"
      }`}
    >
      <div
        className={`p-1 bg-[#191c29] ${
          message.contentType ? "rounded-md " : "px-5 rounded-full"
        }`}
      >
        {message?.contentType?.includes("image") && isFileDataSuccess && (
          <img
            src={`data:${message?.contentType};base64,${fileData}`}
            alt=""
            className="w-[12rem] h-[17rem] object-cover rounded-md"
          />
        )}
        {/* 
          {item?.contentType?.includes("video") && (
            <video
              className="w-[12rem] h-[17rem] object-cover rounded-md"
              controls
            >
              <source
                src={`data:${item?.contentType};base64,${media}`}
                type={item?.contentType}
              />
            </video>
          )} */}
        {message?.contentType?.includes("video") && isFileDataSuccess && (
          <CardMedia
            component="video"
            sx={{ maxWidth: "20rem" }}
            image={`data:${message?.contentType};base64,${fileData}`}
            // image={encodeURI(generateMediaURL(item.fileName, item.filePath))}
            controls
          />
        )}
        {message?.content.length > 0 && (
          <p className={`${message.contentType ? "py-2" : "py-1"}`}>
            {message?.content}
          </p>
        )}
      </div>
    </div>
  );
};

export default ChatMessage;
