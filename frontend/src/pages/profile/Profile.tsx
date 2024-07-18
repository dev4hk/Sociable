import { Avatar, Box, Button, Card, Tab, Tabs } from "@mui/material";
import React, { useState } from "react";
import PostCard from "../../components/post/PostCard";
import EditProfileModal from "../../components/profile/EditProfileModal";
import { useRecoilValue } from "recoil";
import { posts, profile } from "../../atoms";
import { IPost } from "../../interfaces";
import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import {
  getAllPostByUserId,
  getAnotherUserInfo,
  getUserProfile,
} from "../../api/api";

const tabs = [
  { value: "post", name: "Post" },
  { value: "saved", name: "Saved" },
];

const reels = [1, 1, 1, 1];
const savedPost = [1, 1, 1, 1];

const Profile = () => {
  const { id } = useParams();
  const [value, setValue] = useState("post");
  const token = localStorage.getItem("token");

  const allPosts = useRecoilValue(posts);
  const myInfo = useRecoilValue(profile);
  const {
    data: userInfo,
    isLoading: isUserInfoLoading,
    isError: isUserInfoError,
  } = useQuery({
    queryKey: ["userInfo"],
    queryFn: () => getAnotherUserInfo(+id!, token!),
  });

  const {
    data: userPosts,
    isLoading: isUserPostsLoading,
    isError: isUserPostsError,
  } = useQuery({
    queryKey: ["userPosts"],
    queryFn: () => getAllPostByUserId(+id!),
  });

  const [open, setOpen] = useState(false);
  const handleOpenProfileModal = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleChange = (event: any, newValue: any) => {
    setValue(newValue);
  };

  console.log(userInfo);
  console.log(userPosts);

  return (
    <Card className="my-10 w-[70%]">
      <div className="rounded-md">
        <div className="h-[15rem]">
          <img
            className="w-full h-full rounded-t-md object-cover"
            src="https://cdn.pixabay.com/photo/2018/01/12/14/24/night-3078326_1280.jpg"
            alt=""
          />
        </div>
        <div className="px-5 flex justify-between items-start mt-5 h-[5rem]">
          <Avatar
            src="https://cdn.pixabay.com/photo/2022/10/01/21/25/woman-7492273_1280.jpg"
            className="transform -translate-y-24"
            sx={{ width: "10rem", height: "10rem" }}
          />
          {myInfo?.id === userInfo?.id ? (
            <Button
              variant="outlined"
              sx={{ borderRadius: "20px" }}
              onClick={handleOpenProfileModal}
            >
              Edit Profile
            </Button>
          ) : (
            <Button variant="outlined" sx={{ borderRadius: "20px" }}>
              Follow
            </Button>
          )}
        </div>
        <div className="p-5">
          <div>
            <h1 className="py-1 font-bold text-xl">{`${myInfo.firstname} ${myInfo.lastname}`}</h1>
            <p>{`@${myInfo.firstname?.toLowerCase()}_${myInfo.lastname?.toLowerCase()}`}</p>
          </div>
          <div className="flex gap-2 item-center py-3">
            <span>{userPosts?.data.result.content.length} post</span>
            <span>35 followers</span>
            <span>5 followings</span>
          </div>
          <div>
            <p>
              Lorem ipsum, dolor sit amet consectetur adipisicing elit. Fugiat,
              laboriosam, fuga exercitationem alias aliquam culpa.
            </p>
          </div>
        </div>
        <section>
          <Box sx={{ width: "100%", borderBottom: 1, borderColor: "divider" }}>
            <Tabs
              value={value}
              aria-label="wrapped label tabs example"
              onChange={handleChange}
            >
              {tabs.map((item, index) => (
                <Tab
                  key={"tap" + index}
                  value={item.value}
                  label={item.name}
                  wrapped
                />
              ))}
            </Tabs>
          </Box>
          <div className="flex justify-center">
            {value === "post" && (
              <div className="space-y-5 w-[70%] my-10">
                {userPosts?.data.result.content.map(
                  (post: IPost, index: number) => (
                    <div
                      key={"myPost" + index}
                      className="border rounded-md border-slate-100"
                    >
                      <PostCard post={post} />
                    </div>
                  )
                )}
              </div>
            )}

            {value === "saved" && (
              <div className="space-y-5 w-[70%] my-10">
                {/* {posts.map((item, index) => (
                  <div
                    key={"Saved" + index}
                    className="border rounded-md border-slate-100"
                  >
                    <PostCard />
                  </div>
                ))} */}
              </div>
            )}
          </div>
        </section>
      </div>
      <section>
        <EditProfileModal open={open} handleClose={handleClose} />
      </section>
    </Card>
  );
};

export default Profile;
