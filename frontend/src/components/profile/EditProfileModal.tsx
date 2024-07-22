import {
  Avatar,
  Box,
  Button,
  IconButton,
  Modal,
  TextField,
} from "@mui/material";
import React, { useEffect } from "react";
import CloseIcon from "@mui/icons-material/Close";
import { IChangeUserInfo } from "../../interfaces";
import { useForm } from "react-hook-form";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { profile } from "../../atoms";
import { changeUserInfo } from "../../api/userApi";
import { getFile } from "../../api/fileApi";
import { useQuery } from "@tanstack/react-query";

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

const EditProfileModal = ({ open, handleClose, image }: any) => {
  const [profileAtom, setProfileAtom] = useRecoilState(profile);

  // const {
  //   data: file,
  //   isLoading: isFileLoading,
  //   refetch: refetchFile,
  //   isSuccess: isFileSuccess,
  // } = useQuery({
  //   queryKey: ["userFile", profileAtom.id],
  //   queryFn: () => getFile(profileAtom.fileInfo?.filePath!),
  //   enabled: false,
  // });

  // useEffect(() => {
  //   if (profileAtom.fileInfo) {
  //     refetchFile();
  //   }
  // });

  useEffect(() => {
    getFile(profileAtom.fileInfo?.filePath!).then((res) => console.log(res));
  });

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<IChangeUserInfo>({
    defaultValues: {
      firstname: profileAtom.firstname,
      lastname: profileAtom.lastname,
      description: profileAtom.description,
    },
  });
  const onValid = (data: IChangeUserInfo) => {
    const formData = new FormData();
    formData.append("firstname", data.firstname);
    formData.append("lastname", data.lastname);
    formData.append("description", data.description);
    changeUserInfo(formData, data.file).then((res) => console.log(res));
    handleClose();
  };

  const handleModalClose = () => {
    reset();
    handleClose();
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
              <div className="pl-5">
                {/* {isFileSuccess ? (
                  <Avatar
                    className="transform -translate-y-24"
                    sx={{ width: "10rem", height: "10rem" }}
                    src={URL.createObjectURL(file)}
                  />
                ) : (
                  <Avatar
                    className="transform -translate-y-24"
                    sx={{ width: "10rem", height: "10rem" }}
                  />
                )} */}
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
