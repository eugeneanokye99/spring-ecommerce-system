# ShopJoy E-Commerce Frontend

A modern, professional React frontend for the ShopJoy e-commerce system built with React, React Router, TailwindCSS, and Lucide React icons.

## Tech Stack

- **React 18** - UI library
- **React Router v6** - Client-side routing
- **TailwindCSS** - Utility-first CSS framework
- **Lucide React** - Beautiful icon library
- **Axios** - HTTP client for API calls
- **Vite** - Fast build tool and dev server

## Features

### Authentication
- ✅ Login with role-based routing
- ✅ Protected routes for Admin and Customer
- ✅ Persistent authentication with localStorage
- ✅ Demo credentials provided on login page

### Admin Dashboard
- ✅ **Dashboard Overview** - Statistics and quick actions
- ✅ **Product Management** - Full CRUD operations with pagination
- ✅ **Category Management** - Create, edit, and delete categories
- ✅ **Order Management** - View and update order statuses
- ✅ **User Management** - View and manage users
- ✅ **Inventory Management** - Track low stock and out-of-stock items

### Customer Dashboard
- ✅ **Product Browse** - View and search active products
- ✅ **Shopping Cart** - Add, update, and remove items
- ✅ **Checkout** - Place orders from cart
- ✅ **Order History** - View past and current orders

## Getting Started

### Prerequisites
- Node.js 16+ and npm
- Spring Boot backend running on `http://localhost:8080`

### Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:5173`

## Demo Credentials

### Admin Account
- Username: `admin`
- Password: `admin123`

### Customer Account
- Username: `customer`
- Password: `customer123`

## API Integration

The frontend connects to the Spring Boot backend at `http://localhost:8080/api/v1`.

See `API_ENDPOINTS.md` for a complete mapping of endpoints to components.

## License

This project is part of the ShopJoy E-Commerce System.
