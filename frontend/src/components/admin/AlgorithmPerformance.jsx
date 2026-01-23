import { useState, useEffect } from 'react';
import {
    compareSortingAlgorithms,
    compareSearchAlgorithms,
    getAlgorithmRecommendations
} from '../../services/productService';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend
} from 'recharts';
import { Activity, Zap, Search, Database } from 'lucide-react';

const AlgorithmPerformance = () => {
    const [sortingData, setSortingData] = useState([]);
    const [searchData, setSearchData] = useState([]);
    const [recommendations, setRecommendations] = useState(null);
    const [loading, setLoading] = useState(true);
    const [datasetSize, setDatasetSize] = useState(5000);

    const loadData = async () => {
        setLoading(true);
        try {
            const [sortRes, searchRes, recRes] = await Promise.all([
                compareSortingAlgorithms(datasetSize),
                compareSearchAlgorithms(datasetSize),
                getAlgorithmRecommendations(datasetSize)
            ]);

            // Transform map objects to array for Recharts
            const sortArray = Object.values(sortRes.data).map(item => ({
                name: item.algorithmName,
                time: item.executionTimeMs,
                memory: item.memoryUsedBytes / 1024 // Convert to KB
            }));

            const searchArray = Object.values(searchRes.data).map(item => ({
                name: item.algorithmName,
                time: item.executionTimeMs,
                memory: item.memoryUsedBytes / 1024 // Convert to KB
            }));

            setSortingData(sortArray);
            setSearchData(searchArray);
            setRecommendations(recRes.data);
        } catch (error) {
            console.error("Error loading algorithm performance data", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, [datasetSize]);

    return (
        <div className="space-y-8">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h1 className="text-4xl font-extrabold text-gray-900 tracking-tight">Algorithm Analysis</h1>
                    <p className="text-gray-500 mt-2">Real-time performance benchmarking using your actual product catalog.</p>
                    <p className="text-xs text-gray-400 mt-1">* Note: For accurate O(n log n) benchmarking, your product data is replicated to reach the target dataset size.</p>
                </div>
                <div className="flex items-center gap-4 bg-white p-2 rounded-xl border border-gray-100 shadow-sm">
                    <span className="text-sm font-semibold text-gray-700 pl-2">Dataset Size:</span>
                    <select
                        value={datasetSize}
                        onChange={(e) => setDatasetSize(Number(e.target.value))}
                        className="bg-gray-50 border-transparent rounded-lg text-sm font-medium focus:ring-primary-500 focus:border-primary-500"
                    >
                        <option value="1000">1,000 items</option>
                        <option value="5000">5,000 items</option>
                        <option value="10000">10,000 items</option>
                        <option value="50000">50,000 items</option>
                    </select>
                </div>
            </div>

            {loading ? (
                <div className="flex flex-col items-center justify-center h-96">
                    <div className="w-12 h-12 border-4 border-primary-600 border-t-transparent rounded-full animate-spin mb-4"></div>
                    <p className="text-gray-500 font-medium">Running performance benchmarks...</p>
                </div>
            ) : (
                <>
                    {/* Recommendations Section */}
                    {recommendations && (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="bg-gradient-to-br from-blue-600 to-purple-700 rounded-3xl p-8 text-white shadow-lg shadow-secondary-200">
                                <div className="flex items-center gap-3 mb-6">
                                    <div className="p-2 bg-white/20 rounded-xl">
                                        <Zap className="w-6 h-6 text-white" />
                                    </div>
                                    <h3 className="text-xl font-bold">Optimal Configuration</h3>
                                </div>
                                <div className="space-y-4">
                                    <div className="bg-white/10 rounded-2xl p-4 backdrop-blur-sm">
                                        <p className="text-xs font-bold text-primary-100 uppercase tracking-wider mb-1">Recommended Sorting</p>
                                        <p className="text-lg font-bold">{recommendations.sortingAlgorithm}</p>
                                    </div>
                                    <div className="bg-white/10 rounded-2xl p-4 backdrop-blur-sm">
                                        <p className="text-xs font-bold text-primary-100 uppercase tracking-wider mb-1">Recommended Search</p>
                                        <p className="text-lg font-bold">{recommendations.searchAlgorithm}</p>
                                    </div>
                                </div>
                            </div>

                            <div className="bg-white rounded-3xl p-8 border border-gray-100 shadow-sm">
                                <div className="flex items-center gap-3 mb-6">
                                    <div className="p-2 bg-gray-100 rounded-xl">
                                        <Database className="w-6 h-6 text-gray-600" />
                                    </div>
                                    <h3 className="text-xl font-bold text-gray-900">System Insights</h3>
                                </div>
                                <div className="space-y-4">
                                    <div className="p-4 bg-gray-50 rounded-2xl border border-gray-100">
                                        <p className="text-xs font-bold text-gray-400 uppercase tracking-wider mb-1">Pagination Strategy</p>
                                        <p className="text-gray-800 font-medium">{recommendations.paginationStrategy}</p>
                                    </div>
                                    <div className="p-4 bg-gray-50 rounded-2xl border border-gray-100">
                                        <p className="text-xs font-bold text-gray-400 uppercase tracking-wider mb-1">Memory Optimization</p>
                                        <p className="text-gray-800 font-medium">{recommendations.memoryOptimization}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {/* Charts Grid */}
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                        {/* Sorting Performance */}
                        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
                            <div className="flex items-center justify-between mb-8">
                                <div className="flex items-center gap-3">
                                    <div className="p-2 bg-indigo-50 rounded-lg">
                                        <Activity className="w-5 h-5 text-indigo-600" />
                                    </div>
                                    <div>
                                        <h3 className="text-xl font-bold text-gray-900">Sorting Performance</h3>
                                        <p className="text-sm text-gray-500">Execution time (ms)</p>
                                    </div>
                                </div>
                            </div>
                            <div className="h-80 w-full">
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart data={sortingData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f3f4f6" />
                                        <XAxis dataKey="name" stroke="#9ca3af" fontSize={12} tickLine={false} axisLine={false} />
                                        <YAxis stroke="#9ca3af" fontSize={12} tickLine={false} axisLine={false} />
                                        <Tooltip
                                            cursor={{ fill: '#f9fafb' }}
                                            contentStyle={{ borderRadius: '16px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)' }}
                                            formatter={(value) => [`${value.toFixed(4)} ms`, 'Time']}
                                        />
                                        <Bar dataKey="time" name="Time (ms)" fill="#4f46e5" radius={[6, 6, 0, 0]} barSize={50} />
                                    </BarChart>
                                </ResponsiveContainer>
                            </div>
                        </div>

                        {/* Search Performance */}
                        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
                            <div className="flex items-center justify-between mb-8">
                                <div className="flex items-center gap-3">
                                    <div className="p-2 bg-emerald-50 rounded-lg">
                                        <Search className="w-5 h-5 text-emerald-600" />
                                    </div>
                                    <div>
                                        <h3 className="text-xl font-bold text-gray-900">Search Performance</h3>
                                        <p className="text-sm text-gray-500">Execution time (ms)</p>
                                    </div>
                                </div>
                            </div>
                            <div className="h-80 w-full">
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart data={searchData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                                        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f3f4f6" />
                                        <XAxis dataKey="name" stroke="#9ca3af" fontSize={12} tickLine={false} axisLine={false} />
                                        <YAxis stroke="#9ca3af" fontSize={12} tickLine={false} axisLine={false} />
                                        <Tooltip
                                            cursor={{ fill: '#f9fafb' }}
                                            contentStyle={{ borderRadius: '16px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)' }}
                                            formatter={(value) => [`${value.toFixed(4)} ms`, 'Time']}
                                        />
                                        <Bar dataKey="time" name="Time (ms)" fill="#10b981" radius={[6, 6, 0, 0]} barSize={50} />
                                    </BarChart>
                                </ResponsiveContainer>
                            </div>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default AlgorithmPerformance;
