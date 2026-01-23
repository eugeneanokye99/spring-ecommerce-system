import api from './api';

// POST /api/v1/addresses - Create address
export const createAddress = (addressData) => api.post('/addresses', addressData);

// GET /api/v1/addresses/{id} - Get address by ID
export const getAddressById = (id) => api.get(`/addresses/${id}`);

// GET /api/v1/addresses/user/{userId} - Get addresses by user
export const getAddressesByUser = (userId) => api.get(`/addresses/user/${userId}`);

// GET /api/v1/addresses/user/{userId}/default - Get default address
export const getDefaultAddress = (userId) => api.get(`/addresses/user/${userId}/default`);

// PATCH /api/v1/addresses/{id}/set-default - Set default address
export const setDefaultAddress = (id) => api.patch(`/addresses/${id}/set-default`);

// PUT /api/v1/addresses/{id} - Update address
export const updateAddress = (id, addressData) => api.put(`/addresses/${id}`, addressData);

// DELETE /api/v1/addresses/{id} - Delete address
export const deleteAddress = (id) => api.delete(`/addresses/${id}`);
