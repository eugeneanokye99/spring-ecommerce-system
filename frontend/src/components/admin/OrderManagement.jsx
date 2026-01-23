import { useState, useEffect } from 'react';
import { getAllOrders, updateOrderStatus, confirmOrder, shipOrder, completeOrder, cancelOrder } from '../../services/orderService';
import { Package, Clock, Truck, CheckCircle, XCircle, Search, Eye, Filter } from 'lucide-react';

const OrderManagement = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [statusFilter, setStatusFilter] = useState('ALL');

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        try {
            setLoading(true);
            const response = await getAllOrders();
            setOrders(response.data || []);
        } catch (error) {
            console.error('Error loading orders:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleStatusChange = async (orderId, action) => {
        try {
            switch (action) {
                case 'confirm':
                    await confirmOrder(orderId);
                    break;
                case 'ship':
                    await shipOrder(orderId);
                    break;
                case 'complete':
                    await completeOrder(orderId);
                    break;
                case 'cancel':
                    await cancelOrder(orderId);
                    break;
            }
            loadOrders();
        } catch (error) {
            alert(error.message);
        }
    };

    const getStatusBadge = (status) => {
        const badges = {
            PENDING: { color: 'bg-yellow-100 text-yellow-800', icon: Clock },
            PROCESSING: { color: 'bg-blue-100 text-blue-800', icon: CheckCircle },
            SHIPPED: { color: 'bg-indigo-100 text-indigo-800', icon: Truck },
            DELIVERED: { color: 'bg-emerald-100 text-emerald-800', icon: Package },
            CANCELLED: { color: 'bg-rose-100 text-rose-800', icon: XCircle },
        };
        const badge = badges[status] || badges.PENDING;
        const Icon = badge.icon;
        return (
            <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-wider ${badge.color}`}>
                <Icon className="w-3 h-3" /> {status}
            </span>
        );
    };

    const filteredOrders = statusFilter === 'ALL'
        ? orders
        : orders.filter(o => o.status === statusFilter);

    return (
        <div className="space-y-8">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h1 className="text-4xl font-extrabold text-gray-900 tracking-tight">Order Operations</h1>
                    <p className="text-gray-500 mt-2">Manage customer orders and workflow transitions.</p>
                </div>

                <div className="flex items-center gap-4 bg-white p-2 rounded-2xl shadow-sm border border-gray-100">
                    <div className="flex items-center gap-2 px-3 text-gray-400">
                        <Filter className="w-4 h-4" />
                        <span className="text-xs font-bold uppercase">Filter By</span>
                    </div>
                    <select
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                        className="bg-gray-50 border-none rounded-xl text-xs font-bold py-2 px-4 focus:ring-2 focus:ring-primary-500 transition-all"
                    >
                        <option value="ALL">All Orders</option>
                        <option value="PENDING">Pending</option>
                        <option value="PROCESSING">Processing</option>
                        <option value="SHIPPED">Shipped</option>
                        <option value="DELIVERED">Delivered</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>
                </div>
            </div>

            <div className="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
                <table className="w-full text-left border-collapse">
                    <thead className="bg-gray-50/50 border-b border-gray-100">
                        <tr>
                            <th className="px-6 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest">Order Details</th>
                            <th className="px-6 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest">Customer</th>
                            <th className="px-6 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Amount</th>
                            <th className="px-6 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Status</th>
                            <th className="px-6 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Workflow Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-50">
                        {loading ? (
                            <tr>
                                <td colSpan="5" className="px-6 py-24 text-center">
                                    <div className="w-10 h-10 border-4 border-primary-600 border-t-transparent rounded-full animate-spin mx-auto"></div>
                                    <p className="mt-4 text-sm font-medium text-gray-500 uppercase tracking-widest">Syncing Orders...</p>
                                </td>
                            </tr>
                        ) : filteredOrders.length === 0 ? (
                            <tr>
                                <td colSpan="5" className="px-6 py-24 text-center">
                                    <div className="w-16 h-16 bg-gray-50 text-gray-200 rounded-full flex items-center justify-center mx-auto mb-4">
                                        <Search className="w-8 h-8" />
                                    </div>
                                    <p className="text-gray-400 font-bold">No orders match your criteria</p>
                                </td>
                            </tr>
                        ) : (
                            filteredOrders.map((order) => (
                                <tr key={order.orderId} className="hover:bg-gray-50/50 transition-colors group">
                                    <td className="px-6 py-5">
                                        <div className="flex flex-col">
                                            <span className="text-sm font-black text-gray-900">#{order.orderId}</span>
                                            <span className="text-[10px] text-gray-400 font-medium mt-1">
                                                {new Date(order.orderDate).toLocaleDateString()}
                                            </span>
                                        </div>
                                    </td>
                                    <td className="px-6 py-5">
                                        <div className="flex items-center gap-3">
                                            <div className="w-8 h-8 bg-primary-50 rounded-full flex items-center justify-center text-[10px] font-black text-primary-700">
                                                {order.userName?.split(' ').map(n => n[0]).join('')}
                                            </div>
                                            <div className="flex flex-col">
                                                <span className="text-sm font-bold text-gray-700">{order.userName}</span>
                                                {order.orderItems && (
                                                    <span className="text-[10px] text-gray-400 font-medium">{order.orderItems.length} items</span>
                                                )}
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-6 py-5 text-right">
                                        <span className="text-sm font-black text-gray-900">${(order.totalAmount || 0).toFixed(2)}</span>
                                    </td>
                                    <td className="px-6 py-5 text-center">
                                        {getStatusBadge(order.status)}
                                    </td>
                                    <td className="px-6 py-5 text-right">
                                        <div className="flex justify-end gap-2">
                                            {order.status === 'PENDING' && (
                                                <button
                                                    onClick={() => handleStatusChange(order.orderId, 'confirm')}
                                                    className="px-4 py-2 bg-blue-600 text-white text-[10px] font-black uppercase tracking-wider rounded-xl hover:bg-blue-700 transition-all shadow-lg shadow-blue-100"
                                                >
                                                    Accept
                                                </button>
                                            )}
                                            {order.status === 'PROCESSING' && (
                                                <button
                                                    onClick={() => handleStatusChange(order.orderId, 'ship')}
                                                    className="px-4 py-2 bg-indigo-600 text-white text-[10px] font-black uppercase tracking-wider rounded-xl hover:bg-indigo-700 transition-all shadow-lg shadow-indigo-100"
                                                >
                                                    Ship Order
                                                </button>
                                            )}
                                            {order.status === 'SHIPPED' && (
                                                <button
                                                    onClick={() => handleStatusChange(order.orderId, 'complete')}
                                                    className="px-4 py-2 bg-emerald-600 text-white text-[10px] font-black uppercase tracking-wider rounded-xl hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-100"
                                                >
                                                    Mark Delivered
                                                </button>
                                            )}
                                            {['PENDING', 'PROCESSING'].includes(order.status) && (
                                                <button
                                                    onClick={() => handleStatusChange(order.orderId, 'cancel')}
                                                    className="px-4 py-2 bg-white text-rose-600 border border-rose-100 text-[10px] font-black uppercase tracking-wider rounded-xl hover:bg-rose-50 transition-all"
                                                >
                                                    Cancel
                                                </button>
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* Legend/Helper */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-blue-50/50 p-6 rounded-3xl border border-blue-50">
                    <h4 className="text-xs font-black text-blue-900 uppercase tracking-widest mb-2">Process Order</h4>
                    <p className="text-xs text-blue-700 leading-relaxed">Accepting an order moves it to processing, notifying the warehouse to begin fulfillment.</p>
                </div>
                <div className="bg-indigo-50/50 p-6 rounded-3xl border border-indigo-50">
                    <h4 className="text-xs font-black text-indigo-900 uppercase tracking-widest mb-2">Shipping Logic</h4>
                    <p className="text-xs text-indigo-700 leading-relaxed">Once items are boxed, mark as shipped to provide the customer with tracking updates.</p>
                </div>
                <div className="bg-emerald-50/50 p-6 rounded-3xl border border-emerald-50">
                    <h4 className="text-xs font-black text-emerald-900 uppercase tracking-widest mb-2">Final Delivery</h4>
                    <p className="text-xs text-emerald-700 leading-relaxed">Completing an order records the final transaction and completes the customer journey.</p>
                </div>
            </div>
        </div>
    );
};

export default OrderManagement;
