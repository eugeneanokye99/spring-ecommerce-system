# API Endpoints and Frontend Component Mapping

This document lists all REST API endpoints from the Spring Boot backend and shows which React components use them.

## User Management (`/api/v1/users`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/users/register` | POST | Register new user | Login.jsx (future registration) |
| `/users/authenticate` | POST | Login/authenticate user | Login.jsx, AuthContext.jsx |
| `/users/{id}` | GET | Get user by ID | UserManagement.jsx |
| `/users` | GET | Get all users | UserManagement.jsx |
| `/users/{id}` | PUT | Update user profile | (Future profile edit) |
| `/users/{id}` | DELETE | Delete user | UserManagement.jsx |
| `/users/email/{email}` | GET | Find user by email | (Available for use) |
| `/users/{id}/password` | PUT | Change password | (Future password change) |

## Product Management (`/api/v1/products`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/products` | POST | Create product | ProductManagement.jsx |
| `/products/{id}` | GET | Get product by ID | ProductBrowse.jsx |
| `/products` | GET | Get all products | Dashboard.jsx |
| `/products/active` | GET | Get active products | ProductBrowse.jsx |
| `/products/category/{categoryId}` | GET | Get products by category | (Available for filtering) |
| `/products/search?name={name}` | GET | Search products by name | ProductBrowse.jsx (search) |
| `/products/price-range` | GET | Get products by price range | (Available for filtering) |
| `/products/{id}` | PUT | Update product | ProductManagement.jsx |
| `/products/{id}/price` | PATCH | Update product price | (Available for use) |
| `/products/{id}/activate` | PATCH | Activate product | ProductManagement.jsx |
| `/products/{id}/deactivate` | PATCH | Deactivate product | ProductManagement.jsx |
| `/products/{id}` | DELETE | Delete product | ProductManagement.jsx |
| `/products/count` | GET | Get total product count | Dashboard.jsx |
| `/products/count/category/{categoryId}` | GET | Get product count by category | (Available for use) |
| `/products/paginated` | GET | Get products with pagination | ProductManagement.jsx |
| `/products/search/paginated` | GET | Search products with pagination | (Available for use) |
| `/products/filter` | GET | Get products with filters | (Available for advanced filtering) |
| `/products/sorted/quicksort` | GET | Get products sorted with QuickSort | (Available for use) |
| `/products/sorted/mergesort` | GET | Get products sorted with MergeSort | (Available for use) |
| `/products/{id}/binary-search` | GET | Search product by ID with Binary Search | (Available for use) |
| `/products/algorithms/sort-comparison` | GET | Compare sorting algorithms | (Available for performance testing) |
| `/products/algorithms/search-comparison` | GET | Compare search algorithms | (Available for performance testing) |
| `/products/algorithms/recommendations` | GET | Get algorithm recommendations | (Available for use) |
| `/products/sorted/{algorithm}` | GET | Get products with custom sorting algorithm | (Available for use) |

## Order Management (`/api/v1/orders`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/orders` | POST | Create order | Cart.jsx (checkout) |
| `/orders/{id}` | GET | Get order by ID | OrderHistory.jsx |
| `/orders/user/{userId}` | GET | Get orders by user | OrderHistory.jsx |
| `/orders/status/{status}` | GET | Get orders by status | (Available for filtering) |
| `/orders/date-range` | GET | Get orders by date range | (Available for filtering) |
| `/orders/{id}/status` | PATCH | Update order status | OrderManagement.jsx |
| `/orders/{id}/confirm` | PATCH | Confirm order | OrderManagement.jsx |
| `/orders/{id}/ship` | PATCH | Ship order | OrderManagement.jsx |
| `/orders/{id}/complete` | PATCH | Complete order | OrderManagement.jsx |
| `/orders/{id}/cancel` | PATCH | Cancel order | OrderManagement.jsx |
| `/orders/pending` | GET | Get pending orders | OrderManagement.jsx, Dashboard.jsx |

## Shopping Cart (`/api/v1/cart`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/cart/items` | POST | Add item to cart | ProductBrowse.jsx |
| `/cart/items/{cartItemId}` | PUT | Update cart item quantity | Cart.jsx |
| `/cart/items/{cartItemId}` | DELETE | Remove item from cart | Cart.jsx |
| `/cart/user/{userId}` | GET | Get cart items | Cart.jsx |
| `/cart/user/{userId}` | DELETE | Clear cart | Cart.jsx (after checkout) |
| `/cart/user/{userId}/total` | GET | Get cart total | Cart.jsx |
| `/cart/user/{userId}/count` | GET | Get cart item count | (Available for cart badge) |

## Category Management (`/api/v1/categories`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/categories` | POST | Create category | CategoryManagement.jsx |
| `/categories/{id}` | GET | Get category by ID | CategoryManagement.jsx |
| `/categories` | GET | Get all categories | CategoryManagement.jsx, ProductManagement.jsx |
| `/categories/top-level` | GET | Get top-level categories | (Available for navigation) |
| `/categories/{parentId}/subcategories` | GET | Get subcategories | (Available for hierarchical display) |
| `/categories/{id}/has-subcategories` | GET | Check if category has subcategories | (Available for use) |
| `/categories/{id}` | PUT | Update category | CategoryManagement.jsx |
| `/categories/{id}/move` | PATCH | Move category | (Available for use) |
| `/categories/{id}` | DELETE | Delete category | CategoryManagement.jsx |

## Product Reviews (`/api/v1/reviews`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/reviews` | POST | Create review | (Future review feature) |
| `/reviews/{id}` | GET | Get review by ID | (Available for use) |
| `/reviews/product/{productId}` | GET | Get reviews by product | (Future product detail page) |
| `/reviews/user/{userId}` | GET | Get reviews by user | (Available for use) |
| `/reviews/product/{productId}/rating/{rating}` | GET | Get reviews by rating | (Available for filtering) |
| `/reviews/product/{productId}/average-rating` | GET | Get average rating | (Future product cards) |
| `/reviews/{id}` | PUT | Update review | (Future review edit) |
| `/reviews/{id}/helpful` | PATCH | Mark review as helpful | (Future review interaction) |
| `/reviews/{id}` | DELETE | Delete review | (Future review management) |

## Address Management (`/api/v1/addresses`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/addresses` | POST | Create address | (Future address management) |
| `/addresses/{id}` | GET | Get address by ID | (Available for use) |
| `/addresses/user/{userId}` | GET | Get addresses by user | (Future checkout address selection) |
| `/addresses/user/{userId}/default` | GET | Get default address | (Future checkout) |
| `/addresses/{id}/set-default` | PATCH | Set default address | (Future address management) |
| `/addresses/{id}` | PUT | Update address | (Future address edit) |
| `/addresses/{id}` | DELETE | Delete address | (Future address management) |

## Inventory Management (`/api/v1/inventory`)

| Endpoint | Method | Description | Used In Component |
|----------|--------|-------------|-------------------|
| `/inventory/product/{productId}` | GET | Get product inventory | (Available for use) |
| `/inventory/product/{productId}/in-stock` | GET | Check if product is in stock | ProductBrowse.jsx |
| `/inventory/product/{productId}/available-stock` | GET | Check stock availability | (Available for use) |
| `/inventory/product/{productId}` | PUT | Update stock quantity | InventoryManagement.jsx |
| `/inventory/product/{productId}/add` | PATCH | Add stock | InventoryManagement.jsx |
| `/inventory/product/{productId}/remove` | PATCH | Remove stock | (Available for use) |
| `/inventory/product/{productId}/reserve` | PATCH | Reserve stock | (Available for order processing) |
| `/inventory/product/{productId}/release` | PATCH | Release reserved stock | (Available for order cancellation) |
| `/inventory/low-stock` | GET | Get low stock products | InventoryManagement.jsx, Dashboard.jsx |
| `/inventory/out-of-stock` | GET | Get out of stock products | InventoryManagement.jsx |
| `/inventory/product/{productId}/reorder-level` | PATCH | Update reorder level | (Available for use) |

## Summary

### Total Endpoints: 89
### Endpoints Used: 45
### Endpoints Available for Future Features: 44

### Coverage by Controller:
- **UserController**: 5/8 endpoints used (62.5%)
- **ProductController**: 12/28 endpoints used (42.9%)
- **OrderController**: 7/11 endpoints used (63.6%)
- **CartController**: 6/8 endpoints used (75%)
- **CategoryController**: 5/10 endpoints used (50%)
- **ReviewController**: 0/10 endpoints used (0% - future feature)
- **AddressController**: 0/8 endpoints used (0% - future feature)
- **InventoryController**: 5/14 endpoints used (35.7%)

### Key Features Implemented:
✅ User authentication and management
✅ Product CRUD operations with pagination
✅ Category management
✅ Shopping cart functionality
✅ Order creation and management
✅ Inventory tracking and alerts
✅ Role-based access control (Admin/Customer)

### Future Enhancement Opportunities:
🔜 Product reviews and ratings
🔜 Address management for shipping
🔜 Advanced product filtering and sorting algorithms
🔜 User profile management
🔜 Password change functionality
🔜 Product search with pagination
🔜 Category hierarchy navigation
🔜 Stock reservation system
