import { Avatar, Button, Card, Divider, Menu, MenuItem } from "@mui/material";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import HomeIcon from "@mui/icons-material/Home";
import ExploreIcon from "@mui/icons-material/Explore";
import ControlPointIcon from "@mui/icons-material/ControlPoint";
import NotificationsIcon from "@mui/icons-material/Notifications";
import MessageIcon from "@mui/icons-material/Message";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";

const navigationMenu = [
  { title: "Home", icon: <HomeIcon />, path: "/home" },
  { title: "Reels", icon: <ExploreIcon />, path: "/home/reels" },
  {
    title: "Create Reels",
    icon: <ControlPointIcon />,
    path: "/home/create-reels",
  },
  { title: "Notifications", icon: <NotificationsIcon />, path: "/home" },
  { title: "Message", icon: <MessageIcon />, path: "/home/message" },
  { title: "Profile", icon: <AccountCircleIcon />, path: "/home/profile" },
];

const SideNav = () => {
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const navigate = useNavigate();
  const handleOpen = (event: any) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };
  const handleNavigate = (item: any) => {};

  return (
    <Card className="card h-screen flex flex-col justify-between py-5">
      <div className="space-y-8 pl-5">
        <div>
          <span className="logo font-bold text-xl">Sociable</span>
        </div>
        <div className="space-y-8">
          {navigationMenu.map((item, index) => (
            <div
              key={index}
              className="flex space-x-3 items-center cursor-pointer"
              onClick={() => handleNavigate(item)}
            >
              {item.icon}
              <p className="text-xl">{item.title}</p>
            </div>
          ))}
        </div>
      </div>
      <div>
        <Divider />
        <div className="pl-5 flex items-center justify-between pt-5">
          <div className="flex items-center space-x-3">
            <Avatar />
            <div>
              <p className="font-bold">Name</p>
              <p className="opacity079">@ Username</p>
            </div>
          </div>
          <div>
            <Button
              id="basic-button"
              aria-controls={open ? "basic-menu" : undefined}
              aria-haspopup="true"
              aria-expanded={open ? "true" : undefined}
              onClick={handleOpen}
            >
              <MoreVertIcon />
            </Button>
            <Menu
              id="basic-menu"
              anchorEl={anchorEl}
              open={open}
              onClose={handleClose}
              MenuListProps={{
                "aria-labelledby": "basic-button",
              }}
            >
              <MenuItem onClick={handleClose}>Profile</MenuItem>
              <MenuItem onClick={handleClose}>My account</MenuItem>
              <MenuItem onClick={handleClose}>Logout</MenuItem>
            </Menu>
          </div>
        </div>
      </div>
    </Card>
  );
};

export default SideNav;
