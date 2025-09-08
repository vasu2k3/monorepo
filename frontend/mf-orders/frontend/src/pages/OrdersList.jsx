import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/api";
import OrderCard from "../components/OrderCard";

export default function OrdersList() {
  const [orders, setOrders] = useState([]);
  const [searchId, setSearchId] = useState("");
  const [loading, setLoading] = useState(false);

  // ✅ Fetch all orders
  const fetchOrders = () => {
    setLoading(true);
    api
      .get("/") // calls GET /orders
      .then((res) => setOrders(res.data))
      .catch((err) => {
        console.error(err);
        setOrders([]);
      })
      .finally(() => setLoading(false));
  };

  // ✅ Search by Customer ID
  const searchOrders = () => {
    if (!searchId.trim()) {
      fetchOrders();
      return;
    }
    setLoading(true);
    api
      .get(`/customer/${searchId}`) // calls GET /orders/customer/{id}
      .then((res) => setOrders(res.data))
      .catch((err) => {
        console.error(err);
        setOrders([]);
      })
      .finally(() => setLoading(false));
  };

  // ✅ Load all orders initially
  useEffect(() => {
    fetchOrders();
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-4xl font-bold text-center mb-8">Orders</h1>

      {/* Search Bar */}
      <div className="flex justify-center mb-8 gap-2">
        <input
          type="text"
          placeholder="Search by Customer ID..."
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          className="px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500 w-64"
        />
        <button
          onClick={searchOrders}
          className="px-4 py-2 bg-blue-600 hover:bg-blue-500 rounded-lg shadow text-white"
        >
          Search
        </button>
        <button
          onClick={fetchOrders} // ✅ explicitly calls fetchOrders()
          className="px-4 py-2 bg-gray-600 hover:bg-gray-500 rounded-lg shadow text-white"
        >
          Show All
        </button>
        <Link
          to="/add"
          className="px-4 py-2 bg-green-600 hover:bg-green-500 rounded-lg shadow text-white"
        >
          + Add Order
        </Link>
      </div>

      {loading ? (
        <p className="text-center">Loading...</p>
      ) : orders.length === 0 ? (
        <p className="text-center text-gray-400">No orders found.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {orders.map((order) => (
            <OrderCard key={order.orderId} order={order} />
          ))}
        </div>
      )}
    </div>
  );
}
