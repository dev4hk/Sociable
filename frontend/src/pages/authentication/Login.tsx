import { Button, TextField } from "@mui/material";
import React from "react";
import { useForm } from "react-hook-form";
import { ILogin } from "../../interfaces";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../../api/api";

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ILogin>();

  const navigate = useNavigate();

  const onValid = (data: ILogin) => {
    loginUser(data)
      .then((res) => {
        localStorage.setItem("token", res.data.access_token);
        navigate("/home");
      })
      .catch((err) => console.log(err.response.data.resultCode));
  };
  return (
    <div className="space-y-5">
      <form onSubmit={handleSubmit(onValid)}>
        <div className="space-y-5">
          <div>
            <TextField
              id="email"
              label="Email"
              variant="outlined"
              className="w-full"
              {...register("email", {
                required: "Email is required",
                pattern: {
                  value: /^[A-Za-z0-9._%+-]+@[a-z0-9.]+\.[a-z]+/,
                  message: "Email format is invalid",
                },
              })}
            />
            <span className="text-orange-500 text-xs">
              {errors.email?.message}
            </span>
          </div>
          <div>
            <TextField
              id="password"
              label="Password"
              variant="outlined"
              className="w-full"
              type="password"
              {...register("password", {
                required: "Password is required",
                minLength: {
                  value: 8,
                  message: "Password must be at least 8 characters",
                },
              })}
            />
          </div>
          <span className="text-orange-500 text-xs">
            {errors.password?.message}
          </span>
          <Button
            sx={{ padding: ".8rem 0rem" }}
            fullWidth
            type="submit"
            variant="contained"
            color="primary"
          >
            Login
          </Button>
        </div>
      </form>
      <div className="flex gap-5 items-center justify-center pt-5">
        <p>Don't have an account?</p>
        <Button onClick={() => navigate("/register")}>Register</Button>
      </div>
    </div>
  );
};

export default Login;
