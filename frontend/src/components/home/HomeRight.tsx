import React from "react";
import { Card } from "@mui/material";
import UserSearch from "../search/UserSearch";
import UserCard from "./UserCard";
import { useQuery } from "@tanstack/react-query";
import { getUserSuggestions } from "../../api/userApi";
import { IProfile, IUser } from "../../interfaces";

const users = [1, 1, 1, 1, 1];

const HomeRight = () => {
  const { data } = useQuery<IProfile[]>({
    queryKey: ["suggestions"],
    queryFn: getUserSuggestions,
  });

  return (
    <div className="pr-5">
      <UserSearch />
      {data?.length === 0 ? (
        <div className="flex justify-between py-5 items-center text-white">
          <p className="font-semibold opacity-70">No Suggestions</p>
        </div>
      ) : (
        <Card className="p-5">
          <div className="flex justify-between py-5 items-center">
            <p className="font-semibold opacity-70">Suggestions for you</p>
          </div>
          <div>
            {data?.map((user, index) => (
              <UserCard key={"usercard" + index} user={user} />
            ))}
          </div>
        </Card>
      )}
    </div>
  );
};

export default HomeRight;
