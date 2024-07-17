import { Avatar, Card, CardHeader } from "@mui/material";
import { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { chats } from "../../atoms";
import { useQuery } from "@tanstack/react-query";
import { IUser } from "../../interfaces";
import { createChat, getOtherUsers } from "../../api/api";

const UserSearch = () => {
  const [query, setQuery] = useState("");
  const {
    data: usersData,
    isLoading: isUsersDataLoading,
    refetch: refetchUsersData,
    isSuccess: isUsersDataSuccess,
  } = useQuery<IUser[]>({
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
      <div className="py-5 relative">
        <input
          type="text"
          className="bg-transparent border border-[#3b4054] outline-none w-full px-5 py-3 rounded-full text-white"
          placeholder="Search User..."
          onChange={handleSearchUser}
          value={query}
        />
        <div className="absolute w-full z-10 top-[4.5rem]">
          {query &&
            isUsersDataSuccess &&
            usersData?.map((user) => (
              <Card
                key={"user" + user.id}
                className="cursor-pointer hover:text-sky-400"
                onClick={() => handleUserClick(user.id)}
              >
                <CardHeader
                  title={"Firstname Lastname"}
                  subheader={"@firstname_lastname"}
                  avatar={
                    <Avatar src="https://images.pexels.com/photos/1264210/pexels-photo-1264210.jpeg?auto=compress&cs=tinysrgb&w=800" />
                  }
                />
              </Card>
            ))}
        </div>
      </div>
    </div>
  );
};

export default UserSearch;
