import React, { useEffect } from "react";
import { INotification } from "../../interfaces";
import { Avatar, Card, CardHeader } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { getFile } from "../../api/fileApi";

interface INotificationCardProps {
  notification: INotification;
}

const NotificationCard = ({ notification }: INotificationCardProps) => {
  const sourceUserName =
    notification.args.sourceUser.firstname +
    " " +
    notification.args.sourceUser.lastname;

  const subMessage =
    notification.notificationType === "NEW_LIKE_ON_POST"
      ? "liked your post"
      : notification.notificationType === "FOLLOW_USER"
      ? "followed you"
      : "made a comment on one of your posts";

  const { data: sourceUserImage, refetch: refetchImage } = useQuery<Blob>({
    queryKey: ["notification", "sourceUserImage", notification.id],
    queryFn: () => getFile(notification.args.sourceUser.fileInfo.filePath),
    enabled: false,
  });

  console.log(notification);
  useEffect(() => {
    if (notification?.args?.sourceUser?.fileInfo) {
      refetchImage();
    }
  }, [notification]);

  return (
    <Card className="my-5">
      <CardHeader
        avatar={
          notification?.args?.sourceUser?.fileInfo && sourceUserImage ? (
            <Avatar
              src={`data:${notification.args.sourceUser.fileInfo.fileType};base64,${sourceUserImage}`}
            />
          ) : (
            <Avatar />
          )
        }
        title={notification.notificationText}
        subheader={`${sourceUserName} ${subMessage}`}
      />
    </Card>
  );
};

export default NotificationCard;
