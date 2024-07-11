import React from "react";
import { Card } from "@mui/material";
import UserSearch from "../search/UserSearch";
import UserCard from "./UserCard";

const users = [1, 1, 1, 1, 1];

const HomeRight = () => {
  return (
    <div className="pr-5">
      <UserSearch />

      <Card className="p-5">
        <div className="flex justify-between py-5 items-center">
          <p className="font-semibold opacity-70">Suggestions for you</p>
        </div>
        <div>
          {users.map((user, index) => (
            <UserCard key={index} />
          ))}
        </div>
      </Card>
    </div>
  );
};

export default HomeRight;
