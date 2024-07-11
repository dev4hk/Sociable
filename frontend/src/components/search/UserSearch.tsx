import { Avatar, Card, CardHeader } from "@mui/material";

const users = [1, 1, 1, 1, 1];

const UserSearch = () => {
  return (
    <div>
      <div className="py-5 relative">
        <input
          type="text"
          className="bg-transparent border border-[#3b4054] outline-none w-full px-5 py-3 rounded-full"
          placeholder="Search User..."
        />
        <div className="absolute w-full z-10 top-[4.5rem]">
          {false &&
            users.map((user, index) => (
              <Card key={index} className="cursor-pointer hover:text-sky-400">
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
