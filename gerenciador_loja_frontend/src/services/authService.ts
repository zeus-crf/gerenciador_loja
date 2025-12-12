import api from "./api";

export interface LoginRequest{
   username : String;
   password: String;
}

export async function login(data:LoginRequest) {
    const response = await api.post("auth/login", data)

    if (response.data.token){
        localStorage.setItem("token", response.data.token);
    }

    return response.data;

}

export function logout(){
     localStorage.removeItem("token");
}

export default {
  login,
  logout
};