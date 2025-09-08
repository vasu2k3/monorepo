import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8083/orders", // Spring Boot orders service
});

export default api;
