import React from "react";
import { useRecoilValue } from "recoil";
import { notifications } from "../../atoms";
import { Card, CardHeader } from "@mui/material";
import { INotification } from "../../interfaces";
import NotificationCard from "../../components/notification/NotificationCard";

const Notifications = () => {
  const notificationsAtom = useRecoilValue(notifications);

  return (
    <div className="px-20 w-full">
      <section>
        <div className="p-5 mt-5">
          {notificationsAtom.length > 0 ? (
            notificationsAtom.map((item: INotification, index: number) => (
              <NotificationCard key={index} notification={item} />
            ))
          ) : (
            <Card className="my-5">
              <CardHeader subheader={"There is no notifications yet"} />
            </Card>
          )}
        </div>
      </section>
    </div>
  );
};

export default Notifications;
