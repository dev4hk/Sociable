import {
  Avatar,
  Backdrop,
  Box,
  Button,
  CircularProgress,
  IconButton,
  Modal,
  Typography,
} from "@mui/material";
import React, { useState } from "react";
import ImageIcon from "@mui/icons-material/Image";
import VideocamIcon from "@mui/icons-material/Videocam";
import { IPostForm, IPostRequest } from "../../interfaces";
import { useForm } from "react-hook-form";
import { getValue } from "@testing-library/user-event/dist/utils";
import { isTokenValid } from "../../service/AuthenticationService";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import { posts } from "../../atoms";
import { createPost } from "../../api/postApi";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 500,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 4,
  borderRadius: ".6rem",
  outline: "none",
};

const CreatePostModal = ({ handleClose, open, refetch }: any) => {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [serverError, setServerError] = useState<string>();
  const setPosts = useSetRecoilState(posts);

  const navigate = useNavigate();
  const {
    register,
    watch,
    handleSubmit,
    resetField,
    setValue,
    getValues,
    reset,
    setError,
    formState,
  } = useForm<IPostForm>();

  const closeModal = () => {
    reset();
    handleClose();
  };

  const onValid = (data: IPostForm) => {
    if (!isTokenValid(localStorage.getItem("token"))) {
      navigate("/login");
    }

    const formData = new FormData();
    if (data.body.length > 0) {
      formData.append("body", data.body);
    }
    if (data.image) {
      formData.append("file", data.image);
    }
    if (data.video) {
      formData.append("file", data.video);
    }
    console.log(formData);
    createPost(formData)
      .then((response) => {
        refetch();
        closeModal();
      })
      .catch((err) => {
        setServerError("Either caption or file must be filled");
        setTimeout(() => {
          setServerError(undefined);
        }, 5000);
      });
  };

  return (
    <Modal
      open={open}
      onClose={closeModal}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
      className="text-white"
    >
      <Box sx={style}>
        <form onSubmit={handleSubmit(onValid)}>
          <div>
            <div className="flex space-x-4 items-center">
              <Avatar />
              <div>
                <p className="font-bold text-lg">Username</p>
                <p className="text-sm">@Username</p>
              </div>
            </div>
            {}
            {serverError && (
              <span className="text-orange-500 text-xs">{serverError}</span>
            )}
            <textarea
              className="outline-none w-full mt-5 p-2 bg-transparent border border-[#3b4054] rounded-sm"
              id="caption"
              placeholder="Write Caption..."
              rows={4}
              {...register("body")}
            />
            <div className="flex space-x-5 items-center mt-5">
              <div>
                <input
                  type="file"
                  accept="image/*"
                  style={{ display: "none" }}
                  id="image-input"
                  {...register("image", {
                    onChange: (e) => {
                      resetField("video");
                      setValue("image", e.target.files[0]);
                    },
                  })}
                />
                <label htmlFor="image-input">
                  <IconButton color="primary" component="span">
                    <ImageIcon />
                  </IconButton>
                </label>
                <span>Image</span>
              </div>

              <div>
                <input
                  type="file"
                  accept="video/*"
                  style={{ display: "none" }}
                  id="video-input"
                  {...register("video", {
                    onChange: (e) => {
                      resetField("image");
                      setValue("video", e.target.files[0]);
                    },
                  })}
                />
                <label htmlFor="video-input">
                  <IconButton color="primary" component="span">
                    <VideocamIcon />
                  </IconButton>
                </label>
                <span>Video</span>
              </div>
            </div>
            {!formState.isSubmitting &&
              !formState.isSubmitted &&
              getValues("image") && (
                <div className="flex justify-center my-8">
                  <img
                    className="h-[15rem]"
                    src={URL.createObjectURL(watch().image)}
                    alt=""
                  />
                </div>
              )}
            {!formState.isSubmitting &&
              !formState.isSubmitted &&
              getValues("video") && (
                <div className="flex justify-center my-8">
                  <video className="h-[15rem]" controls>
                    <source
                      src={URL.createObjectURL(getValues("video"))}
                      type={getValues("video").type}
                    />
                  </video>
                </div>
              )}

            <div className="flex w-full justify-end">
              <Button
                type="submit"
                variant="contained"
                sx={{ borderRadius: "1.5rem" }}
              >
                Post
              </Button>
            </div>
          </div>
        </form>

        <Backdrop
          sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
          open={isLoading}
          onClick={() => setIsLoading(false)}
        >
          <CircularProgress color="inherit" />
        </Backdrop>
      </Box>
    </Modal>
  );
};

export default CreatePostModal;
