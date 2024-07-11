import { Grid } from "@mui/material";
import React from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import SideNav from "../../components/sidenav/SideNav";
import HomeMiddle from "../../components/home/HomeMiddle";
import HomeRight from "../../components/home/HomeRight";
import Profile from "../profile/Profile";

const HomePage = () => {
  const location = useLocation();
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
            <Route path="/profile" element={<Profile />} />
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
