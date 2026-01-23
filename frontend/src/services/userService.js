import api from './api';

// POST /api/v1/users/register - Register new user
export const registerUser = (userData) => api.post('/users/register', userData);

// GET /api/v1/users/{id} - Get user by ID
export const getUserById = (id) => api.get(`/users/${id}`);

// GET /api/v1/users - Get all users
export const getAllUsers = () => api.get('/users');

// PUT /api/v1/users/{id} - Update user profile
export const updateUserProfile = (id, userData) => api.put(`/users/${id}`, userData);

// DELETE /api/v1/users/{id} - Delete user
export const deleteUser = (id) => api.delete(`/users/${id}`);

// GET /api/v1/users/email/{email} - Find user by email
export const getUserByEmail = (email) => api.get(`/users/email/${email}`);

// POST /api/v1/users/authenticate - Authenticate user (login)
export const authenticateUser = (credentials) => api.post('/users/authenticate', credentials);

// PUT /api/v1/users/{id}/password - Change password
export const changePassword = (id, passwordData) => api.put(`/users/${id}/password`, passwordData);
