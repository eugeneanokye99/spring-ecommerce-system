import { useState, useEffect } from 'react';
import { getOrdersByUser } from '../../services/orderService';
import { useAuth } from '../../context/AuthContext';
import { Package, Clock, Truck, CheckCircle, XCircle } from 'lucide-react';

const OrderHistory = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const { user } = useAuth();

    useEffect(() => {
        if (user) {
            loadOrders();
        }
    }, [user]);

    const loadOrders = async () => {
        if (!user) return;
        try {
            const response = await getOrdersByUser(user.userId);
            setOrders(response.data || []);
            console.log(response.data);
        } catch (error) {
            console.error('Error loading orders:', error);
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadge = (status) => {
        const badges = {
            PENDING: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
            PROCESSING: { color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
            SHIPPED: { color: 'bg-purple-100 text-purple-800', icon: Truck },
            DELIVERED: { color: 'bg-green-100 text-green-800', icon: Package },
            CANCELLED: { color: 'bg-red-100 text-red-800', icon: XCircle },
        };
        const badge = badges[status] || badges.PENDING;
        const Icon = badge.icon;
        return (
            <span className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium ${badge.color}`}>
                <Icon className="w-3 h-3" /> {status}
            </span>
        );
    };

    if (loading) {
        return (
            <div className="flex justify-center py-12">
                <div className="w-12 h-12 border-4 border-primary-600 border-t-transparent rounded-full animate-spin"></div>
            </div>
        );
    }

    return (
        <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-6">Order History</h1>

            {orders.length === 0 ? (
                <div className="card p-12 text-center">
                    <Package className="w-16 h-16 text-gray-400 mx-auto mb-4" />
                    <h3 className="text-xl font-semibold text-gray-900 mb-2">No orders yet</h3>
                    <p className="text-gray-600">Your order history will appear here</p>
                </div>
            ) : (
                <div className="space-y-4">
                    {orders.map((order) => (
                        <div key={order.orderId} className="card p-6">
                            <div className="flex items-center justify-between mb-4">
                                <div>
                                    <h3 className="text-lg font-semibold text-gray-900">Order #{order.orderId}</h3>
                                    <p className="text-sm text-gray-600">
                                        {new Date(order.orderDate).toLocaleDateString()}
                                    </p>
                                </div>
                                <div className="text-right">
                                    {getStatusBadge(order.status)}
                                    <p className="text-lg font-bold text-gray-900 mt-2">
                                        ${order.totalAmount?.toFixed(2)}
                                    </p>
                                </div>
                            </div>

                            {order.orderItems && order.orderItems.length > 0 && (
                                <div className="border-t pt-4">
                                    <h4 className="text-sm font-medium text-gray-700 mb-2">Items:</h4>
                                    <div className="space-y-2">
                                        {order.orderItems.map((item, index) => (
                                            <div key={index} className="flex justify-between text-sm">
                                                <span className="text-gray-600">
                                                    {item.productName} x {item.quantity}
                                                </span>
                                                <span className="text-gray-900 font-medium">
                                                    ${item.subtotal?.toFixed(2)}
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default OrderHistory;
