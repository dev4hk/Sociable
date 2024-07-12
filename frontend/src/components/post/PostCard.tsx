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
import { IPost } from "../../interfaces";

interface IPostCard {
  post: IPost;
}

const PostCard = ({ post }: IPostCard) => {
  return (
    <Card>
      <CardHeader
        avatar={
          <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
            {post.firstname.slice(0, 1).toUpperCase()}
          </Avatar>
        }
        action={
          <IconButton aria-label="settings">
            <MoreVertIcon />
          </IconButton>
        }
        title={`${post.firstname} ${post.lastname}`}
        subheader={`@${post.firstname.toLowerCase()}_${post.lastname.toLowerCase()}`}
      />

      {post?.fileType?.includes("image") && (
        // true
        <CardMedia
          component="img"
          height="194"
          image={`data:${post?.fileType};base64,${post.file}`}
          alt=""
        />
      )}
      {post?.fileType?.includes("video") && (
        <CardMedia
          component="video"
          height="194"
          image={`data:${post?.fileType};base64,${post.file}`}
          controls
        />
      )}

      <CardContent>
        <Typography variant="body2" color="text.secondary">
          {post.body}
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
