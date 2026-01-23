import api from './api';

// POST /api/v1/products - Create product
export const createProduct = (productData) => api.post('/products', productData);

// GET /api/v1/products/{id} - Get product by ID
export const getProductById = (id) => api.get(`/products/${id}`);

// GET /api/v1/products - Get all products
export const getAllProducts = () => api.get('/products');

// GET /api/v1/products/active - Get active products
export const getActiveProducts = () => api.get('/products/active');

// GET /api/v1/products/category/{categoryId} - Get products by category
export const getProductsByCategory = (categoryId) => api.get(`/products/category/${categoryId}`);

// GET /api/v1/products/search?name={name} - Search products by name
export const searchProductsByName = (name) => api.get('/products/search', { params: { name } });

// GET /api/v1/products/price-range?minPrice={min}&maxPrice={max} - Get products by price range
export const getProductsByPriceRange = (minPrice, maxPrice) =>
    api.get('/products/price-range', { params: { minPrice, maxPrice } });

// PUT /api/v1/products/{id} - Update product
export const updateProduct = (id, productData) => api.put(`/products/${id}`, productData);

// PATCH /api/v1/products/{id}/price?newPrice={price} - Update product price
export const updateProductPrice = (id, newPrice) =>
    api.patch(`/products/${id}/price`, null, { params: { newPrice } });

// PATCH /api/v1/products/{id}/activate - Activate product
export const activateProduct = (id) => api.patch(`/products/${id}/activate`);

// PATCH /api/v1/products/{id}/deactivate - Deactivate product
export const deactivateProduct = (id) => api.patch(`/products/${id}/deactivate`);

// DELETE /api/v1/products/{id} - Delete product
export const deleteProduct = (id) => api.delete(`/products/${id}`);

// GET /api/v1/products/count - Get total product count
export const getTotalProductCount = () => api.get('/products/count');

// GET /api/v1/products/count/category/{categoryId} - Get product count by category
export const getProductCountByCategory = (categoryId) => api.get(`/products/count/category/${categoryId}`);

// GET /api/v1/products/paginated - Get products with pagination
export const getProductsPaginated = (page = 0, size = 10, sortBy = 'product_id', sortDirection = 'ASC') =>
    api.get('/products/paginated', { params: { page, size, sortBy, sortDirection } });

// GET /api/v1/products/search/paginated - Search products with pagination
export const searchProductsPaginated = (term, page = 0, size = 10) =>
    api.get('/products/search/paginated', { params: { term, page, size } });

// GET /api/v1/products/filter - Get products with filters
export const getProductsWithFilters = (filters) =>
    api.get('/products/filter', { params: filters });

// GET /api/v1/products/sorted/quicksort - Get products sorted with QuickSort
export const getProductsSortedWithQuickSort = (sortBy = 'product_id', ascending = true) =>
    api.get('/products/sorted/quicksort', { params: { sortBy, ascending } });

// GET /api/v1/products/sorted/mergesort - Get products sorted with MergeSort
export const getProductsSortedWithMergeSort = (sortBy = 'product_id', ascending = true) =>
    api.get('/products/sorted/mergesort', { params: { sortBy, ascending } });

// GET /api/v1/products/{id}/binary-search - Search product by ID with Binary Search
export const searchProductByIdWithBinarySearch = (id) => api.get(`/products/${id}/binary-search`);

// GET /api/v1/products/algorithms/sort-comparison - Compare sorting algorithms
export const compareSortingAlgorithms = (datasetSize = 1000) =>
    api.get('/products/algorithms/sort-comparison', { params: { datasetSize } });

// GET /api/v1/products/algorithms/search-comparison - Compare search algorithms
export const compareSearchAlgorithms = (datasetSize = 1000) =>
    api.get('/products/algorithms/search-comparison', { params: { datasetSize } });

// GET /api/v1/products/algorithms/recommendations - Get algorithm recommendations
export const getAlgorithmRecommendations = (datasetSize = 5000) =>
    api.get('/products/algorithms/recommendations', { params: { datasetSize } });

// GET /api/v1/products/sorted/{algorithm} - Get products with custom sorting algorithm
export const getProductsWithAlgorithm = (algorithm, sortBy = 'price', sortDirection = 'ASC') =>
    api.get(`/products/sorted/${algorithm}`, { params: { sortBy, sortDirection } });

// GET /api/v1/products/new-arrivals - Get recently added products
export const getNewArrivals = (limit = 10) =>
    api.get('/products/new-arrivals', { params: { limit } });
