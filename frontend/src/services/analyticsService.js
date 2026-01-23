import api from './api';

// GET /api/v1/analytics/dashboard - Get dashboard data
export const getDashboardData = () => api.get('/analytics/dashboard');

// GET /api/v1/analytics/user/{userId} - Get user analytics
export const getUserAnalytics = (userId) => api.get(`/analytics/user/${userId}`);
