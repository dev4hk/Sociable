import { Alert, Button, TextField } from "@mui/material";
import React, { useState } from "react";
import { ILogin, IRegister } from "../../interfaces";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../../api/api";
import { useSetRecoilState } from "recoil";

const Register = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
  } = useForm<IRegister>();

  const navigate = useNavigate();
  const [serverErrors, setServerErrors] = useState();

  const onValid = (data: IRegister) => {
    if (data.password !== data.confirmPassword) {
      setError(
        "confirmPassword",
        { message: "password doesn't match" },
        { shouldFocus: true }
      );
    } else {
      registerUser(data)
        .then((res) => {
          localStorage.setItem("token", res.data.access_token);
          navigate("/home");
        })
        .catch((err) => {
          setServerErrors(err.response.data.resultCode);
          setTimeout(() => {
            setServerErrors(undefined);
          }, 5000);
        });
    }
  };
  return (
    <div className="space-y-5">
      <form onSubmit={handleSubmit(onValid)}>
        <div className="space-y-5">
          {serverErrors && <Alert severity="error">{serverErrors}</Alert>}
          <div>
            <TextField
              id="firstname"
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
              id="lastname"
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
            <span className="text-orange-500 text-xs">
              {errors.password?.message}
            </span>
          </div>
          <div>
            <TextField
              id="confirmPassword"
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
            Register
          </Button>
        </div>
      </form>
      <div className="flex gap-5 items-center justify-center pt-5">
        <p>Already have an account?</p>
        <Button onClick={() => navigate("/login")}>Login</Button>
      </div>
    </div>
  );
};

export default Register;
