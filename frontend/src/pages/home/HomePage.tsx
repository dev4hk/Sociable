import { Grid } from "@mui/material";
import React, { useEffect } from "react";
import { Route, Routes, useLocation, useNavigate } from "react-router-dom";
import SideNav from "../../components/sidenav/SideNav";
import HomeMiddle from "../../components/home/HomeMiddle";
import HomeRight from "../../components/home/HomeRight";
import Profile from "../profile/Profile";
import { IGetAllPosts, IPost } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { isTokenValid } from "../../service/AuthenticationService";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { posts, profile } from "../../atoms";
import { getAllPosts } from "../../api/postApi";

const page = 0;
const size = 10;

const HomePage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const setPostsAtom = useSetRecoilState(posts);
  const getUserInfo = useRecoilValue(profile);
  const { isLoading, data, refetch, isError, isFetched } = useQuery({
    queryKey: ["posts"],
    queryFn: () => getAllPosts(),
  });

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
            <Route
              path="/"
              element={
                <HomeMiddle
                  data={data?.data?.result?.content}
                  refetch={refetch}
                />
              }
            />
            <Route path={`/profile/:id`} element={<Profile />} />
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
