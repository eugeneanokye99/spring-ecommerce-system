import api from './api';

// POST /api/v1/categories - Create category
export const createCategory = (categoryData) => api.post('/categories', categoryData);

// GET /api/v1/categories/{id} - Get category by ID
export const getCategoryById = (id) => api.get(`/categories/${id}`);

// GET /api/v1/categories - Get all categories
export const getAllCategories = () => api.get('/categories');

// GET /api/v1/categories/top-level - Get top-level categories
export const getTopLevelCategories = () => api.get('/categories/top-level');

// GET /api/v1/categories/{parentId}/subcategories - Get subcategories
export const getSubcategories = (parentId) => api.get(`/categories/${parentId}/subcategories`);

// GET /api/v1/categories/{id}/has-subcategories - Check if category has subcategories
export const hasSubcategories = (id) => api.get(`/categories/${id}/has-subcategories`);

// PUT /api/v1/categories/{id} - Update category
export const updateCategory = (id, categoryData) => api.put(`/categories/${id}`, categoryData);

// PATCH /api/v1/categories/{id}/move?newParentId={parentId} - Move category
export const moveCategory = (id, newParentId) =>
    api.patch(`/categories/${id}/move`, null, { params: { newParentId } });

// DELETE /api/v1/categories/{id} - Delete category
export const deleteCategory = (id) => api.delete(`/categories/${id}`);
