import Authentication from "../pages/authentication/Authentication";
import { isTokenValid } from "../service/AuthenticationService";
import { Navigate } from "react-router-dom";

const AuthenticatedRoute = (props: any) => {
  const jwtToken = localStorage.getItem("token");
  return isTokenValid(jwtToken) ? props.children : <Navigate to={"/"} />;
};

export default AuthenticatedRoute;
