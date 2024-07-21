import { CardMedia } from "@mui/material";
import React, { useEffect } from "react";
import { IFile, IMessage } from "../../interfaces";
import { useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";

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
    queryFn: () => getFile(message?.fileInfo?.filePath),
    enabled: false,
  });

  useEffect(() => {
    if (message?.fileInfo?.filePath) {
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
          message?.fileInfo?.fileType ? "rounded-md " : "px-5 rounded-full"
        }`}
      >
        {message?.fileInfo?.fileType?.includes("image") &&
          isFileDataSuccess && (
            <img
              src={`data:${message?.fileInfo?.fileType};base64,${fileData}`}
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
        {message?.fileInfo?.fileType?.includes("video") &&
          isFileDataSuccess && (
            <CardMedia
              component="video"
              sx={{ maxWidth: "20rem" }}
              image={`data:${message?.fileInfo?.fileType};base64,${fileData}`}
              // image={encodeURI(generateMediaURL(item.fileName, item.filePath))}
              controls
            />
          )}
        {message?.content.length > 0 && (
          <p className={`${message?.fileInfo?.fileType ? "py-2" : "py-1"}`}>
            {message?.content}
          </p>
        )}
      </div>
    </div>
  );
};

export default ChatMessage;
