import React, { useState } from "react";
import AddIcon from "@mui/icons-material/Add";
import { Avatar, Card, IconButton } from "@mui/material";
import ImageIcon from "@mui/icons-material/Image";
import VideocamIcon from "@mui/icons-material/Videocam";
import ArticleIcon from "@mui/icons-material/Article";
import StoryCircle from "./StoryCircle";
import PostCard from "../post/PostCard";

const story = [1, 1, 1, 1, 1];
const posts = [1, 1, 1, 1, 1];

const HomeMiddle = () => {
  const [openCreatePostModal, setOpenCreatePostModal] = useState(false);

  const handleOpenCreatePostModal = () => {
    setOpenCreatePostModal(true);
  };
  return (
    <div className="px-20 w-full">
      {/* <section className="flex items-center p-5 rounded-b-md">
        <div className="flex flex-col items-center mr-4 cursor-pointer">
          <Avatar sx={{ width: "5rem", height: "5rem" }}>
            <AddIcon sx={{ fontSize: "3rem" }} />
          </Avatar>
          <p>New</p>
        </div>
        {story.map((item, index) => (
          <StoryCircle key={index} />
        ))}
      </section> */}
      <section>
        <Card className="p-5 mt-5">
          <div className="flex justify-between">
            <Avatar />
            <input
              onClick={handleOpenCreatePostModal}
              className="outline-none w-[90%] rounded-full px-5 bg-transparent border-[#3b4054] border"
              type="text"
              readOnly
            />
          </div>
          <div className="flex justify-center space-x-9 mt-5">
            <div className="flex items-center">
              <IconButton color="primary" onClick={handleOpenCreatePostModal}>
                <ImageIcon />
              </IconButton>
              <span>Media</span>
            </div>

            <div className="flex items-center">
              <IconButton color="primary" onClick={handleOpenCreatePostModal}>
                <VideocamIcon />
              </IconButton>
              <span>Video</span>
            </div>

            <div className="flex items-center">
              <IconButton color="primary" onClick={handleOpenCreatePostModal}>
                <ArticleIcon />
              </IconButton>
              <span>Write Article</span>
            </div>
          </div>
        </Card>
      </section>
      <section className="mt-5 space-y-5">
        {posts.map((index) => (
          <PostCard key={index} />
        ))}
      </section>
    </div>
  );
};

export default HomeMiddle;
