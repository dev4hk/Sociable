import React, { useState } from "react";
import { Avatar, Card, IconButton } from "@mui/material";
import ImageIcon from "@mui/icons-material/Image";
import VideocamIcon from "@mui/icons-material/Videocam";
import ArticleIcon from "@mui/icons-material/Article";
import PostCard from "../post/PostCard";
import CreatePostModal from "../post/CreatePostModal";
import { IPost } from "../../interfaces";

const posts = [1, 1, 1, 1, 1];

const HomeMiddle = ({ data, refetch }: any) => {
  const [openCreatePostModal, setOpenCreatePostModal] = useState(false);
  const handleClose = () => setOpenCreatePostModal(false);
  const handleOpenCreatePostModal = () => {
    setOpenCreatePostModal(true);
  };
  return (
    <div className="px-20 w-full">
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
        {data?.map((post: IPost, index: any) => (
          <PostCard key={"postcard" + index} post={post} refetch={refetch} />
        ))}
      </section>
      <div>
        <CreatePostModal
          handleClose={handleClose}
          open={openCreatePostModal}
          refetch={refetch}
        />
      </div>
    </div>
  );
};

export default HomeMiddle;
