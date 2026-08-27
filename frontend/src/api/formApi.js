import axiosInstance from "./axiosInstance";

export const getAllForms = () => axiosInstance.get("/forms");
export const getFormById = (id) => axiosInstance.get(`/forms/${id}`);
export const createForm = (data) => axiosInstance.post("/forms", data);
export const updateForm = (id, data) => axiosInstance.put(`/forms/${id}`, data);
export const deleteForm = (id) => axiosInstance.delete(`/forms/${id}`);

export const getAllFormTypes = () => axiosInstance.get("/form-types");