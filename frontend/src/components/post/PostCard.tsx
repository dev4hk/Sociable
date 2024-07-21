import {
  Avatar,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  CardMedia,
  Divider,
  IconButton,
  Typography,
} from "@mui/material";
import { red } from "@mui/material/colors";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import ShareIcon from "@mui/icons-material/Share";
import ChatBubbleIcon from "@mui/icons-material/ChatBubble";
import BookmarkBorderIcon from "@mui/icons-material/BookmarkBorder";
import BookmarkIcon from "@mui/icons-material/Bookmark";
import { IComment, ICommentsResponse, IPost } from "../../interfaces";
import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { posts, profile } from "../../atoms";
import { createComment, getCommentsByPost } from "../../api/commentApi";
import { likePost } from "../../api/postApi";
import { getFile } from "../../api/fileApi";

interface IPostCard {
  post: IPost;
  refetch: () => void;
}

const page = 0;
const size = 10;

const PostCard = ({ post, refetch: refetchPosts }: IPostCard) => {
  const [showComments, setShowComments] = useState(false);
  const handleShowComments = () => {
    setShowComments(!showComments);
  };

  const {
    data: comments,
    isFetched,
    refetch: refetchComments,
  } = useQuery<ICommentsResponse>({
    queryKey: [`${post.id}:comments`],
    queryFn: () => getCommentsByPost(post.id, page, size),
  });

  const {
    data: file,
    isLoading: isFileLoading,
    isSuccess: isFileSuccess,
  } = useQuery<Blob>({
    queryKey: [`${post.id}:file`],
    queryFn: () => getFile(post?.fileInfo?.filePath),
  });

  const userAtom = useRecoilValue(profile);
  const setPostsAtom = useSetRecoilState(posts);
  const postsAtom = useRecoilValue(posts);
  const { register, watch, getValues, resetField } = useForm();

  const handleCreateComment = () => {
    const request = { comment: getValues("comment") };
    createComment(request, post.id).then(() => {
      resetField("comment");
      refetchComments();
    });
  };

  const handleLike = () => {
    likePost(post.id).then((res) => {
      const updated = res.data.result;
      refetchPosts!();
    });
  };

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

      {post?.fileInfo?.fileType?.includes("image") && isFileSuccess && (
        // true
        <CardMedia
          component="img"
          height="194"
          image={`data:${post?.fileInfo?.fileType};base64,${file}`}
          alt=""
        />
      )}
      {post?.fileInfo?.fileType?.includes("video") && isFileSuccess && (
        <CardMedia
          component="video"
          height="194"
          image={`data:${post?.fileInfo?.fileType};base64,${file}`}
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
          <IconButton onClick={handleLike}>
            {post.likedBy.includes(userAtom.id!) ? (
              <FavoriteIcon />
            ) : (
              <FavoriteBorderIcon />
            )}
          </IconButton>

          <IconButton>
            <ShareIcon />
          </IconButton>

          <IconButton onClick={handleShowComments}>
            <ChatBubbleIcon />
          </IconButton>
        </div>

        <div>
          <IconButton>
            {true ? <BookmarkIcon /> : <BookmarkBorderIcon />}
          </IconButton>
        </div>
      </CardActions>
      <CardContent sx={{ paddingY: 0 }}>
        <Typography variant="body2" color="text.secondary">
          {`${post.likedBy.length} likes`}
        </Typography>
      </CardContent>
      {isFetched && showComments && (
        <section>
          <div className="flex items-center space-x-5 mx-3 my-5">
            <Avatar sx={{}} />
            <input
              className="w-full outline-none bg-transparent border border-[#3b4050] rounded-full px-5 py-2"
              type="text"
              placeholder="Write your comment..."
              onKeyDown={(e: any) => {
                if (e.key === "Enter") {
                  handleCreateComment();
                }
              }}
              {...register("comment")}
            />
          </div>
          <Divider />
          <div className="mx-3 space-y-2 my-5 text-sm">
            {comments?.result.content.map(
              (comment: IComment, index: number) => (
                <div
                  key={`${comment.id}:${index}`}
                  className="flex items-center space-x-5"
                >
                  <Avatar
                    sx={{ height: "2rem", width: "2rem", fontSize: ".8rem" }}
                  >
                    {comment?.firstname[0].toUpperCase()}
                    {/* {"S"} */}
                  </Avatar>
                  <p>{comment?.comment}</p>
                  {/* <p>{comment}</p> */}
                </div>
              )
            )}
          </div>
        </section>
      )}
    </Card>
  );
};

export default PostCard;
