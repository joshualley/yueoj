import axios from "axios";

axios.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

axios.interceptors.response.use(
  (response) => {
    console.log("响应", response);
    return response;
  },
  (error) => {
    return Promise.reject(error);
  },
);
