import axiosInstance from "./axiosInstance";

export const getForms = (params) => axiosInstance.get("/forms", { params });

export const getFormById = (id) => axiosInstance.get(`/forms/${id}`);

export const createForm = (data) => axiosInstance.post("/forms", data);

export const updateForm = (id, data) => axiosInstance.put(`/forms/${id}`, data);

export const deleteForm = (id) => axiosInstance.delete(`/forms/${id}`);

export const approveForm = (id) => axiosInstance.put(`/forms/${id}/approve`);

export const rejectForm = (id) => axiosInstance.put(`/forms/${id}/reject`);

export const changeFormStatus = (id, status) =>
  axiosInstance.put(`/forms/${id}/status`, null, { params: { status } });

export const cancelForm = (id) => axiosInstance.put(`/forms/${id}/cancel`);

export const getAllFormTypes = () => axiosInstance.get("/form-types");