import {
  Avatar,
  Box,
  Button,
  IconButton,
  Modal,
  TextField,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import CloseIcon from "@mui/icons-material/Close";
import { IChangeUserInfo } from "../../interfaces";
import { useForm } from "react-hook-form";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { profile } from "../../atoms";
import { changeUserInfo } from "../../api/userApi";
import { getFile } from "../../api/fileApi";
import { useQuery } from "@tanstack/react-query";
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";

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

interface IEditProfileModal {
  open: boolean;
  handleClose: () => void;
  image?: Blob;
  fileType?: string;
  refetchUserInfo: () => void;
}

const EditProfileModal = ({
  open,
  handleClose,
  image,
  fileType,
  refetchUserInfo,
}: IEditProfileModal) => {
  const [profileAtom, setProfileAtom] = useRecoilState(profile);

  const [selectedFile, setSelectedFile] = useState<any>();

  const {
    register,
    handleSubmit,
    reset,
    formState,
    getValues,
    watch,
    setValue,
  } = useForm<IChangeUserInfo>({
    defaultValues: {
      firstname: profileAtom.firstname,
      lastname: profileAtom.lastname,
      description: profileAtom.description,
    },
  });
  const onValid = (data: IChangeUserInfo) => {
    const formData = new FormData();
    const requestStr = JSON.stringify({
      firstname: data.firstname,
      lastname: data.lastname,
      description: data.description,
    });
    formData.append("request", requestStr);
    formData.append("file", selectedFile);
    console.log(formData.get("file"));
    changeUserInfo(formData).then((res) => {
      setProfileAtom(res);
      refetchUserInfo();
    });
    handleClose();
  };

  const handleModalClose = () => {
    reset();
    setSelectedFile(undefined);
    handleClose();
  };

  const handleSelectFile = (e: any) => {
    setSelectedFile(e.target.files[0]);
  };

  return (
    <div>
      <Modal
        open={open}
        onClose={handleModalClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <form onSubmit={handleSubmit(onValid)}>
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <IconButton onClick={handleModalClose}>
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
              <div className="pl-5 flex relative">
                {selectedFile ? (
                  <Avatar
                    className="transform -translate-y-24"
                    sx={{ width: "10rem", height: "10rem" }}
                    src={URL.createObjectURL(selectedFile)}
                  />
                ) : image && fileType ? (
                  <Avatar
                    className="transform -translate-y-24"
                    sx={{ width: "10rem", height: "10rem" }}
                    src={`data:${fileType};base64,${image}`}
                  />
                ) : (
                  <Avatar
                    className="transform -translate-y-24"
                    sx={{ width: "10rem", height: "10rem" }}
                  />
                )}
                <div className="w-[10rem] h-[10rem] absolute flex justify-center items-center backdrop-brightness-50 rounded-full -top-24">
                  <input
                    type="file"
                    accept="image/*"
                    style={{ display: "none" }}
                    id="image-input"
                    onChange={handleSelectFile}
                  />
                  <label htmlFor="image-input">
                    <IconButton color="primary" component="span">
                      <AddPhotoAlternateIcon />
                    </IconButton>
                  </label>
                </div>
              </div>
            </div>
            <div className="space-y-5">
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
                {formState.errors.firstname?.message}
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
                {formState.errors.lastname?.message}
              </span>
              <TextField
                fullWidth
                id="description"
                label="Description"
                {...register("description")}
              />
            </div>
          </form>
        </Box>
      </Modal>
    </div>
  );
};

export default EditProfileModal;
