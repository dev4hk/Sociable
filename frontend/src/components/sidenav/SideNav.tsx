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
import { useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { logout } from "../../api/authApi";

const navigationMenu = [
  { title: "Home", icon: <HomeIcon />, path: "/home" },
  {
    title: "Notifications",
    icon: <NotificationsIcon />,
    path: "/home/notifications",
  },
  { title: "Message", icon: <MessageIcon />, path: "/home/message" },
  { title: "Profile", icon: <AccountCircleIcon />, path: "/home/profile" },
];

const SideNav = () => {
  const userProfile = useRecoilValue(profile);
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const navigate = useNavigate();
  const handleOpen = (event: any) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };
  const handleNavigate = (item: any) => {
    if (item.title === "Profile") {
      navigate(item.path + `/${userProfile.id}`);
    } else {
      navigate(item.path);
    }
  };

  const toProfilePage = () => {
    navigate(`/home/profile/${userProfile.id}`);
  };

  const handleLogout = () => {
    logout().then((res) => {
      localStorage.clear();
      navigate("/login");
    });
  };

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
              <p className="font-bold">{`${userProfile.firstname} ${userProfile.lastname}`}</p>
              <p className="opacity079">{`@${userProfile.firstname?.toLowerCase()}_${userProfile.lastname?.toLowerCase()}`}</p>
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
              <MenuItem onClick={toProfilePage}>Profile</MenuItem>
              <MenuItem onClick={handleClose}>My account</MenuItem>
              <MenuItem onClick={handleLogout}>Logout</MenuItem>
            </Menu>
          </div>
        </div>
      </div>
    </Card>
  );
};

export default SideNav;
