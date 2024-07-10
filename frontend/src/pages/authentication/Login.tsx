import { Button, TextField } from "@mui/material";
import React from "react";
import { useForm } from "react-hook-form";
import { ILogin } from "../../interfaces";

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ILogin>();

  const onValid = (data: ILogin) => {
    console.log(data);
  };
  return (
    <div className="space-y-5">
      <form onSubmit={handleSubmit(onValid)}>
        <div className="space-y-5">
          <div>
            <TextField
              id="outlined-basic"
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
            <span>{errors.email?.message}</span>
          </div>
          <div>
            <TextField
              id="outlined-basic"
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
          <span>{errors.email?.message}</span>
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
    </div>
  );
};

export default Login;
