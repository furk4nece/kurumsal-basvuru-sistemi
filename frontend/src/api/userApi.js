import axiosInstance from "./axiosInstance";

export const getProfile = () => axiosInstance.get("/users/me");

export const updateProfile = (data) => axiosInstance.put("/users/me", data);

export const getAllUsers = () => axiosInstance.get("/users");

export const updateUserRole = (id, role) => axiosInstance.put(`/users/${id}/role`, { role });

export const deleteUser = (id) => axiosInstance.delete(`/users/${id}`);