import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/api";

export default function AddOrder() {
  const [customerId, setCustomerId] = useState("");
  const [status, setStatus] = useState("PENDING");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);

    const newOrder = {
      orderCustomerId: parseInt(customerId, 10),
      orderStatus: status,
      orderDate: new Date().toISOString(), // let backend save this
    };

    api.post("/", newOrder)
      .then(() => navigate("/")) // redirect to orders list
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  };

  return (
    <div className="p-8 max-w-md mx-auto">
      <Link to="/" className="text-blue-400 hover:underline mb-6 inline-block">
        ← Back to Orders
      </Link>
      <div className="bg-gray-800 p-6 rounded-xl shadow-lg">
        <h2 className="text-2xl font-bold mb-6">Add New Order</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-300 mb-1">Customer ID</label>
            <input
              type="number"
              value={customerId}
              onChange={(e) => setCustomerId(e.target.value)}
              required
              className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label className="block text-gray-300 mb-1">Status</label>
            <select
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              className="w-full px-4 py-2 rounded-lg bg-gray-700 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="PENDING">PENDING</option>
              <option value="PROCESSING">PROCESSING</option>
              <option value="COMPLETE">COMPLETE</option>
              <option value="CANCELED">CANCELED</option>
              <option value="ON_HOLD">ON_HOLD</option>
              <option value="PAYMENT_REVIEW">PAYMENT_REVIEW</option>
              <option value="PENDING_PAYMENT">PENDING_PAYMENT</option>
              <option value="SUSPECTED_FRAUD">SUSPECTED_FRAUD</option>
              <option value="CLOSED">CLOSED</option>
            </select>
          </div>
          <button
            type="submit"
            disabled={loading}
            className="w-full py-2 bg-green-600 hover:bg-green-500 rounded-lg shadow text-white font-semibold"
          >
            {loading ? "Saving..." : "Add Order"}
          </button>
        </form>
      </div>
    </div>
  );
}
