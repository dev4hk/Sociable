import { Avatar, Card, CardHeader } from "@mui/material";
import { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { chats } from "../../atoms";
import { useQuery } from "@tanstack/react-query";
import { IProfile, IUser } from "../../interfaces";
import { getOtherUsers } from "../../api/userApi";
import { createChat } from "../../api/chatApi";
import UserSearchCard from "../home/UserSearchCard";

const UserSearch = () => {
  const [query, setQuery] = useState("");
  const {
    data: usersData,
    isLoading: isUsersDataLoading,
    refetch: refetchUsersData,
    isSuccess: isUsersDataSuccess,
  } = useQuery<IProfile[]>({
    queryKey: ["users", query],
    queryFn: () => getOtherUsers(query),
    enabled: false,
  });
  const [chatsAtom, setChatsAtom] = useRecoilState(chats);
  const handleSearchUser = (event: any) => {
    setQuery(event.target.value);
  };
  useEffect(() => {
    refetchUsersData();
  }, [query]);

  const handleUserClick = (id: number) => {
    createChat(id).then((res) => {
      if (chatsAtom.filter((chat) => chat.id === res.id).length === 0) {
        setChatsAtom((prev) => [res, ...prev]);
      }
    });
    setQuery("");
  };

  console.log(usersData);
  return (
    <div>
      <div className="py-5 relative ">
        <input
          type="text"
          className="bg-transparent border border-[#3b4054] outline-none w-full px-5 py-3 rounded-full text-white"
          placeholder="Search User..."
          onChange={handleSearchUser}
          value={query}
        />
        <div className="absolute w-full z-10 top-[4.5rem] overflow-y-scroll max-h-[500px] no-scrollbar">
          {query &&
            isUsersDataSuccess &&
            usersData?.map((user) => (
              <UserSearchCard user={user} handleUserClick={handleUserClick} />
            ))}
        </div>
      </div>
    </div>
  );
};

export default UserSearch;
