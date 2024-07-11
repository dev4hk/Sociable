import {
  Avatar,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  CardMedia,
  IconButton,
  Typography,
} from "@mui/material";
import { red } from "@mui/material/colors";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import FavoriteIcon from "@mui/icons-material/Favorite";
import ShareIcon from "@mui/icons-material/Share";
import ChatBubbleIcon from "@mui/icons-material/ChatBubble";
import BookmarkBorderIcon from "@mui/icons-material/BookmarkBorder";
import BookmarkIcon from "@mui/icons-material/Bookmark";

const PostCard = () => {
  return (
    <Card>
      <CardHeader
        avatar={
          <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
            R
          </Avatar>
        }
        action={
          <IconButton aria-label="settings">
            <MoreVertIcon />
          </IconButton>
        }
        title="Username"
        subheader="
        @firstname_lastname"
      />

      <CardMedia
        component="img"
        height="194"
        image={`https://cdn.pixabay.com/photo/2024/02/15/15/46/cat-8575641_1280.jpg`}
        // image={`data:${item?.contentType};base64,${item?.file}`}
        // image={encodeURI(generateMediaURL(item.fileName, item.filePath))}
        alt=""
      />

      <CardContent>
        <Typography variant="body2" color="text.secondary">
          {"Caption"}
        </Typography>
      </CardContent>

      <CardActions className="flex justify-between" disableSpacing>
        <div>
          <IconButton>
            <FavoriteIcon />
          </IconButton>

          <IconButton>
            <ShareIcon />
          </IconButton>

          <IconButton>
            <ChatBubbleIcon />
          </IconButton>
        </div>

        <div>
          <IconButton>
            {true ? <BookmarkIcon /> : <BookmarkBorderIcon />}
          </IconButton>
        </div>
      </CardActions>
    </Card>
  );
};

export default PostCard;
