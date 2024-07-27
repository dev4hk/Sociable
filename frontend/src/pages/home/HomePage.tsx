import { Grid } from "@mui/material";
import React, { useEffect, useState } from "react";
import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import SideNav from "../../components/sidenav/SideNav";
import HomeMiddle from "../../components/home/HomeMiddle";
import HomeRight from "../../components/home/HomeRight";
import Profile from "../profile/Profile";
import { useQuery } from "@tanstack/react-query";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { notifications, posts, profile } from "../../atoms";
import { getAllPosts } from "../../api/postApi";
import { INotification } from "../../interfaces";
import { getNotification } from "../../api/notificationApi";
import { getToken } from "../../api/authApi";
import Notifications from "../notification/Notifications";

const HomePage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const getUserInfo = useRecoilValue(profile);
  const { isLoading, data, refetch, isError, isFetched, error } = useQuery({
    queryKey: ["posts"],
    queryFn: () => getAllPosts(),
  });
  const [postsAtom, setPostsAtom] = useRecoilState(posts);
  useEffect(() => {
    if (data) {
      console.log(data);
      setPostsAtom(data.data.result.content);
    }
  }, [data]);

  const [notificationEvent, setNotificationEvent] = useState<EventSource>();
  const [notificationsAtom, setNotificationsAtom] =
    useRecoilState(notifications);

  const { data: notificationsData, refetch: refetchNotification } = useQuery<
    INotification[]
  >({
    queryKey: ["notifications", getUserInfo.id],
    queryFn: getNotification,
    enabled: false,
  });

  useEffect(() => {
    refetchNotification();
    const eventSource = new EventSource(
      `http://localhost:8888/api/v1/notifications/subscribe?token=${getToken()}`
    );
    setNotificationEvent(eventSource);
    eventSource.addEventListener("open", function (event) {
      console.log("connection opened");
    });

    eventSource.addEventListener("notification", function (event) {
      console.log(event.data);
      refetchNotification();
    });

    eventSource.addEventListener("error", function (event: any) {
      console.log(event.target.readyState);
      if (event.target.readyState === EventSource.CLOSED) {
        console.log("eventsource closed (" + event.target.readyState + ")");
      }
      eventSource.close();
    });
  }, []);

  useEffect(() => {
    if (notificationsData) {
      setNotificationsAtom(notificationsData);
    }
  }, [notificationsData]);

  return (
    <div className="px-20">
      <Grid container spacing={0}>
        <Grid item xs={0} lg={3}>
          <div className="sticky top-0">
            <SideNav />
          </div>
        </Grid>
        <Grid
          item
          className="px-5 flex justify-center"
          xs={12}
          lg={location.pathname === "/home" ? 6 : 9}
        >
          <Routes>
            <Route path="/" element={<HomeMiddle />} />
            <Route path={`/profile/:id`} element={<Profile />} />
            <Route path={`/notifications`} element={<Notifications />} />
          </Routes>
        </Grid>
        {location.pathname === "/home" && (
          <Grid item lg={3} className="relative">
            <div className="sticky top-0 w-full">
              <HomeRight />
            </div>
          </Grid>
        )}
      </Grid>
    </div>
  );
};

export default HomePage;
