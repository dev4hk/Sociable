import { Button, TextField } from "@mui/material";
import React from "react";
import { ILogin, IRegister } from "../../interfaces";
import { useForm } from "react-hook-form";

const Register = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
  } = useForm<IRegister>();

  const onValid = (data: IRegister) => {
    if (data.password !== data.confirmPassword) {
      setError(
        "confirmPassword",
        { message: "password doesn't match" },
        { shouldFocus: true }
      );
    }
    console.log(data);
  };
  return (
    <div className="space-y-5">
      <form onSubmit={handleSubmit(onValid)}>
        <div className="space-y-5">
          <div>
            <TextField
              id="outlined-basic"
              label="Firstname"
              variant="outlined"
              className="w-full"
              {...register("firstname", {
                required: "Firstname is required",
                minLength: {
                  value: 2,
                  message: "Firstname must be at least 2 characters",
                },
              })}
            />
            <span className="text-orange-500 text-xs">
              {errors.firstname?.message}
            </span>
          </div>
          <div>
            <TextField
              id="outlined-basic"
              label="Lastname"
              variant="outlined"
              className="w-full"
              {...register("lastname", {
                required: "Email is required",
                minLength: {
                  value: 2,
                  message: "Lastname must be at least 2 characters",
                },
              })}
            />
            <span className="text-orange-500 text-xs">
              {errors.lastname?.message}
            </span>
          </div>
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
            <span className="text-orange-500 text-xs">
              {errors.email?.message}
            </span>
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
            <span className="text-orange-500 text-xs">
              {errors.password?.message}
            </span>
          </div>
          <div>
            <TextField
              id="outlined-basic"
              label="Confirm Password"
              variant="outlined"
              className="w-full"
              type="password"
              {...register("confirmPassword", {
                required: "Password is required",
              })}
            />
            <span className="text-orange-500 text-xs">
              {errors.confirmPassword?.message}
            </span>
          </div>
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

export default Register;
