import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/api";
import StatusBar from "../components/StatusBar";

export default function OrderDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [showDetails, setShowDetails] = useState(false);

  useEffect(() => {
    api
      .get(`/${id}`) // GET /orders/{id}
      .then((res) => setOrder(res.data))
      .catch((err) => console.error(err));
  }, [id]);

  if (!order) return <p className="text-center mt-10">Loading...</p>;

  return (
    <div className="p-8 max-w-2xl mx-auto">
      {/* ✅ Back button uses navigate(-1) */}
      <button
        onClick={() => navigate(-1)}
        className="text-blue-400 hover:underline mb-6 inline-block"
      >
        ← Back to Orders
      </button>

      <div className="bg-gray-800 p-6 rounded-xl shadow-lg">
        <h2 className="text-3xl font-bold mb-4">Order #{order.orderId}</h2>
        <p className="text-gray-300">Customer ID: {order.orderCustomerId}</p>
        <p className="text-gray-300">
          Order Date: {new Date(order.orderDate).toLocaleString()}
        </p>
        <StatusBar status={order.orderStatus} />

        {/* ✅ Details toggle button */}
        <button
          onClick={() => setShowDetails(!showDetails)}
          className="mt-4 px-4 py-2 bg-blue-600 hover:bg-blue-500 text-white rounded-lg"
        >
          {showDetails ? "Hide Details" : "Show Details"}
        </button>

        {/* ✅ Expandable details */}
        {showDetails && (
          <div className="mt-4 p-4 bg-gray-700 rounded-lg">
            <p className="text-gray-300">
              Product Name: {order.productName || "null"}
            </p>
            <p className="text-gray-300">
              Product Catalog: {order.productCatalog || "null"}
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
