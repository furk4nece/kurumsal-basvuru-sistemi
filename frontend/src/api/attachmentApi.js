import axiosInstance from "./axiosInstance";

export const uploadAttachment = (applicationFormId, file) => {
  const formData = new FormData();
  formData.append("file", file);
  return axiosInstance.post(`/attachments/upload/${applicationFormId}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const deleteAttachment = (id) => axiosInstance.delete(`/attachments/${id}`);

export const downloadAttachment = (id) =>
  axiosInstance.get(`/attachments/download/${id}`, { responseType: "blob" });