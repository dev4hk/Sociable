import React from "react";
import { useRecoilState, useRecoilValue } from "recoil";
import { profile } from "../../atoms";
import { ValidationModeFlags } from "react-hook-form";
import { Box, IconButton, Modal } from "@mui/material";
import UserCard from "../home/UserCard";
import CloseIcon from "@mui/icons-material/Close";
import { IProfile } from "../../interfaces";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 450,
  maxHeight: 500,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 2,
  outline: "none",
  overFlow: "scroll-y",
  borderRadius: 3,
};

interface IUserListModalProps {
  open: boolean;
  handleClose: () => void;
  userIds: number[];
  isFollowing: boolean;
}

const UserListModal = ({
  open,
  handleClose,
  userIds,
  isFollowing,
}: IUserListModalProps) => {
  return (
    <Modal open={open} onClose={handleClose}>
      <Box sx={style}>
        <div className="flex justify-between items-center">
          <p className="text-white">
            {isFollowing ? "Followings" : "Followers"}
          </p>
          <IconButton onClick={handleClose}>
            <CloseIcon />
          </IconButton>
        </div>
        <div className="max-h-[425px] overflow-y-scroll no-scrollbar">
          {userIds.length === 0 ? (
            <p className="text-center text-white">No Users Found</p>
          ) : (
            userIds?.map((id: number, index: number) => (
              <UserCard userId={id} />
            ))
          )}
        </div>
      </Box>
    </Modal>
  );
};

export default UserListModal;
