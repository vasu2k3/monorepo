import { Link } from "react-router-dom";
import StatusBar from "./StatusBar";

export default function OrderCard({ order }) {
  return (
    <Link to={`/orders/${order.orderId}`}>
      <div className="bg-gray-800 hover:bg-gray-700 transition p-5 rounded-xl shadow-lg cursor-pointer">
        <h2 className="text-xl font-bold">Order #{order.orderId}</h2>
        <p className="text-gray-400">Customer ID: {order.orderCustomerId}</p>
        <p className="text-gray-400">
          Date: {new Date(order.orderDate).toLocaleString()}
        </p>
        <p className="text-gray-400">Status: {order.orderStatus}</p>

        {/* Product Info (if present) */}
        {order.product && (
          <>
            <p className="text-gray-300 mt-2">Product: {order.product.productName}</p>
            <p className="text-gray-400">Catalog: {order.product.catalog?.catalogName}</p>
          </>
        )}

        <StatusBar status={order.orderStatus} />
      </div>
    </Link>
  );
}
