import { useState, useEffect } from 'react';
import { getLowStockProducts, getOutOfStockProducts, updateStock, addStock } from '../../services/inventoryService';
import { AlertTriangle, Package, Plus } from 'lucide-react';

const InventoryManagement = () => {
    const [lowStock, setLowStock] = useState([]);
    const [outOfStock, setOutOfStock] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('low');

    useEffect(() => {
        loadInventory();
    }, []);

    const loadInventory = async () => {
        try {
            const [low, out] = await Promise.all([getLowStockProducts(), getOutOfStockProducts()]);
            setLowStock(low.data || []);
            setOutOfStock(out.data || []);
        } catch (error) {
            console.error('Error loading inventory:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleAddStock = async (productId) => {
        const quantity = prompt('Enter quantity to add:');
        if (quantity && !isNaN(quantity)) {
            try {
                await addStock(productId, parseInt(quantity));
                loadInventory();
            } catch (error) {
                alert(error.message);
            }
        }
    };

    const items = activeTab === 'low' ? lowStock : outOfStock;

    return (
        <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-6">Inventory Management</h1>

            <div className="flex gap-4 mb-6">
                <button onClick={() => setActiveTab('low')} className={`px-6 py-3 rounded-lg font-medium ${activeTab === 'low' ? 'bg-yellow-100 text-yellow-800' : 'bg-gray-100 text-gray-600'}`}>
                    Low Stock ({lowStock.length})
                </button>
                <button onClick={() => setActiveTab('out')} className={`px-6 py-3 rounded-lg font-medium ${activeTab === 'out' ? 'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-600'}`}>
                    Out of Stock ({outOfStock.length})
                </button>
            </div>

            <div className="card overflow-hidden">
                <table className="w-full">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Product</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Current Stock</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Reorder Level</th>
                            <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                        {loading ? (
                            <tr><td colSpan="4" className="px-6 py-12 text-center"><div className="w-8 h-8 border-4 border-primary-600 border-t-transparent rounded-full animate-spin mx-auto"></div></td></tr>
                        ) : items.length === 0 ? (
                            <tr><td colSpan="4" className="px-6 py-12 text-center text-gray-500">No items found</td></tr>
                        ) : (
                            items.map((item) => (
                                <tr key={item.productId} className="hover:bg-gray-50">
                                    <td className="px-6 py-4">
                                        <div className="flex items-center">
                                            <Package className="w-5 h-5 text-gray-400 mr-3" />
                                            <span className="text-sm font-medium text-gray-900">{item.productName}</span>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4">
                                        <span className={`text-sm font-medium ${item.stockQuantity === 0 ? 'text-red-600' : 'text-yellow-600'}`}>
                                            {item.stockQuantity}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 text-sm text-gray-900">{item.reorderLevel}</td>
                                    <td className="px-6 py-4 text-right">
                                        <button onClick={() => handleAddStock(item.productId)} className="btn-primary text-xs px-3 py-1 flex items-center gap-1 ml-auto">
                                            <Plus className="w-3 h-3" /> Add Stock
                                        </button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default InventoryManagement;
