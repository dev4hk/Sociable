import {
  Avatar,
  Box,
  Button,
  IconButton,
  Modal,
  TextField,
} from "@mui/material";
import React from "react";
import CloseIcon from "@mui/icons-material/Close";
import { IChangeUserInfo } from "../../interfaces";
import { useForm } from "react-hook-form";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { profile } from "../../atoms";
import { changeUserInfo } from "../../api/api";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 600,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 2,
  outline: "none",
  overFlow: "scroll-y",
  borderRadius: 3,
};

const EditProfileModal = ({ open, handleClose }: any) => {
  const setUserInfo = useSetRecoilState(profile);
  const getUserInfo = useRecoilValue(profile);

  const userFirstname = getUserInfo.firstname;
  const userLastname = getUserInfo.lastname;
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<IChangeUserInfo>({
    defaultValues: { firstname: userFirstname, lastname: userLastname },
  });
  const onValid = (data: IChangeUserInfo) => {
    changeUserInfo(data).then((res) => {
      setUserInfo((prev) => res);
    });
    handleClose();
  };
  return (
    <div>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <form onSubmit={handleSubmit(onValid)}>
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <IconButton onClick={handleClose}>
                  <CloseIcon />
                </IconButton>
                <p className="text-white">Edit Profile</p>
              </div>
              <Button type="submit">Save</Button>
            </div>
            <div>
              <div className="h-[15rem]">
                <img
                  src="https://cdn.pixabay.com/photo/2018/01/12/14/24/night-3078326_1280.jpg"
                  alt=""
                  className="w-full h-full rounded-t-md object-cover"
                />
              </div>
              <div className="pl-5">
                <Avatar
                  className="transform -translate-y-24"
                  sx={{ width: "10rem", height: "10rem" }}
                  src="https://cdn.pixabay.com/photo/2022/10/01/21/25/woman-7492273_1280.jpg"
                />
              </div>
            </div>
            <div className="space-y-3">
              <TextField
                fullWidth
                id="firstname"
                variant="outlined"
                label="Firstname"
                className="w-full"
                {...register("firstname", {
                  required: "Firstname is required",
                  minLength: {
                    value: 2,
                    message: "Firstname must be at least 2 characters",
                  },
                })}
              />
              <span className="text-orange-500 text-xs">
                {errors.firstname?.message}
              </span>
              <TextField
                fullWidth
                id="lastname"
                label="Lastname"
                {...register("lastname", {
                  required: "Lastname is required",
                  minLength: {
                    value: 2,
                    message: "Lastname must be at least 2 characters",
                  },
                })}
              />
              <span className="text-orange-500 text-xs">
                {errors.lastname?.message}
              </span>
            </div>
          </form>
        </Box>
      </Modal>
    </div>
  );
};

export default EditProfileModal;
