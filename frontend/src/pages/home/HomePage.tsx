import { Grid } from "@mui/material";
import React from "react";
import { useLocation } from "react-router-dom";

const HomePage = () => {
  const location = useLocation();
  return (
    <div className="px-20">
      <Grid container spacing={0}>
        <Grid item xs={0} lg={3}>
          <div className="sticky top-0">Sidebar</div>
        </Grid>
        <Grid
          item
          className="px-5 flex justify-center"
          xs={12}
          lg={location.pathname === "/home" ? 6 : 9}
        >
          Home Middle
        </Grid>
        {location.pathname === "/home" && (
          <Grid item lg={3} className="relative">
            <div className="sticky top-0 w-full">HomePage Right</div>
          </Grid>
        )}
      </Grid>
    </div>
  );
};

export default HomePage;
