import { Avatar, Box, Button, Card, Tab, Tabs } from "@mui/material";
import React, { useEffect, useState } from "react";
import PostCard from "../../components/post/PostCard";
import EditProfileModal from "../../components/profile/EditProfileModal";
import { useRecoilState, useRecoilValue } from "recoil";
import { posts, profile } from "../../atoms";
import { IPost, IProfile } from "../../interfaces";
import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { getAllPostByUserId, getSavedPost } from "../../api/postApi";
import { getAnotherUserInfo } from "../../api/userApi";
import { getFile } from "../../api/fileApi";
import UserListModal from "../../components/profile/UserListModal";

const tabs = [
  { value: "post", name: "Post" },
  { value: "saved", name: "Saved" },
];

const Profile = () => {
  const { id } = useParams();
  const [value, setValue] = useState("post");
  const token = localStorage.getItem("token");

  const userAtom = useRecoilValue(profile);

  const {
    data: userInfo,
    isLoading: isUserInfoLoading,
    isError: isUserInfoError,
    refetch: refetchUserInfo,
  } = useQuery<IProfile>({
    queryKey: ["userInfo"],
    queryFn: () => getAnotherUserInfo(+id!),
  });

  const {
    data: userPosts,
    isLoading: isUserPostsLoading,
    isError: isUserPostsError,
    refetch,
  } = useQuery({
    queryKey: ["userPosts"],
    queryFn: () => getAllPostByUserId(+id!),
  });

  const { data: profileImage, refetch: refetchProfileImage } = useQuery({
    queryKey: ["profileImage", userInfo?.id],
    queryFn: () => getFile(userInfo?.fileInfo?.filePath!),
    enabled: false,
  });

  useEffect(() => {
    if (userInfo?.fileInfo) {
      refetchProfileImage();
    }
  }, [userInfo]);

  const { data: savedPosts, refetch: refetchSavedPosts } = useQuery({
    queryKey: [`savedPosts:${id}`],
    queryFn: getSavedPost,
    enabled: false,
  });

  useEffect(() => {
    if (+id! === userAtom.id) {
      refetchSavedPosts();
    }
  });

  console.log(savedPosts);

  const [editModalOpen, setEditModalOpen] = useState(false);
  const handleOpenEditModal = () => setEditModalOpen(true);
  const handleCloseEditModal = () => setEditModalOpen(false);

  const [userListModalOpen, setUserListModalOpen] = useState(false);
  const handleOpenUserListModal = () => setUserListModalOpen(true);
  const handleCloseUserListModal = () => setUserListModalOpen(false);
  const [isFollowing, setIsFollowing] = useState(true);

  const handleTapChange = (event: any, newValue: any) => {
    setValue(newValue);
  };

  return (
    <Card className="my-10 w-[70%]">
      <div className="rounded-md">
        <div className="h-[15rem]">
          <img
            className="w-full h-full rounded-t-md object-cover"
            src="https://cdn.pixabay.com/photo/2017/01/08/20/25/abstract-1963884_1280.jpg"
            alt=""
          />
        </div>
        <div className="px-5 flex justify-between items-start mt-5 h-[5rem]">
          {profileImage ? (
            <Avatar
              src={`data:${userInfo?.fileInfo?.fileType};base64,${profileImage}`}
              className="transform -translate-y-24"
              sx={{ width: "10rem", height: "10rem" }}
            />
          ) : (
            <Avatar
              className="transform -translate-y-24"
              sx={{ width: "10rem", height: "10rem" }}
            />
          )}

          {userAtom?.id === userInfo?.id ? (
            <Button
              variant="outlined"
              sx={{ borderRadius: "20px" }}
              onClick={handleOpenEditModal}
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
            <h1 className="py-1 font-bold text-xl">{`${userInfo?.firstname} ${userInfo?.lastname}`}</h1>
            <p>{`@${userInfo?.firstname?.toLowerCase()}_${userInfo?.lastname?.toLowerCase()}`}</p>
          </div>
          <div className="flex gap-2 item-center py-3">
            <span>{userPosts?.data.result.content.length} post</span>
            <span
              onClick={() => {
                setIsFollowing(false);
                handleOpenUserListModal();
              }}
            >
              {userInfo?.followers?.length} followers
            </span>
            <span
              onClick={() => {
                setIsFollowing(true);
                handleOpenUserListModal();
              }}
            >
              {userInfo?.followings?.length} followings
            </span>
          </div>
          <div>
            <p>{userInfo?.description}</p>
          </div>
        </div>
        <section>
          <Box sx={{ width: "100%", borderBottom: 1, borderColor: "divider" }}>
            <Tabs
              value={value}
              aria-label="wrapped label tabs example"
              onChange={handleTapChange}
            >
              <Tab value="post" label="Post" wrapped />
              {id === userAtom?.id?.toString() && (
                <Tab value="saved" label="Saved" wrapped />
              )}
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
                {savedPosts.map((post: IPost, index: number) => (
                  <div
                    key={"Saved" + index}
                    className="border rounded-md border-slate-100"
                  >
                    <PostCard post={post} />
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>
      </div>
      <section>
        <EditProfileModal
          open={editModalOpen}
          handleClose={handleCloseEditModal}
          image={profileImage}
          fileType={userInfo?.fileInfo?.fileType}
          refetchUserInfo={refetchUserInfo}
        />
      </section>

      <section>
        <UserListModal
          open={userListModalOpen}
          handleClose={handleCloseUserListModal}
          userIds={isFollowing ? userInfo?.followings! : userInfo?.followers!}
          isFollowing={isFollowing}
        />
      </section>
    </Card>
  );
};

export default Profile;
