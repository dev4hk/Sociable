import { Avatar, Card, CardHeader } from "@mui/material";
import { useState } from "react";

const users = [1, 1, 1, 1, 1];

const UserSearch = () => {
  const [username, setUsername] = useState("");
  const handleSearchUser = (event: any) => {
    setUsername(event.target.value);
  };
  return (
    <div>
      <div className="py-5 relative">
        <input
          type="text"
          className="bg-transparent border border-[#3b4054] outline-none w-full px-5 py-3 rounded-full text-white"
          placeholder="Search User..."
          onKeyDown={handleSearchUser}
        />
        <div className="absolute w-full z-10 top-[4.5rem]">
          {username &&
            users.map((user, index) => (
              <Card
                key={"user" + index}
                className="cursor-pointer hover:text-sky-400"
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
