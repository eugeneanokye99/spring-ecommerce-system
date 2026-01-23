import { createContext, useContext, useState, useEffect } from 'react';
import { authenticateUser } from '../services/userService';

const AuthContext = createContext(null);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Check if user is logged in on mount
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        try {
            const response = await authenticateUser({ username, password });
            const userData = response.data;
            setUser(userData);
            localStorage.setItem('user', JSON.stringify(userData));
            return userData;
        } catch (error) {
            throw error;
        }
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem('user');
    };

    const value = {
        user,
        login,
        logout,
        loading,
        isAuthenticated: !!user,
        isAdmin: user?.userType === 'ADMIN',
        isCustomer: user?.userType === 'CUSTOMER',
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
