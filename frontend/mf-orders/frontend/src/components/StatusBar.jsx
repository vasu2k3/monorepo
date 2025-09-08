export default function StatusBar({ status }) {
  const statusColors = {
    PROCESSING: "bg-blue-500",
    COMPLETE: "bg-green-600",
    CANCELED: "bg-red-600",
    PENDING: "bg-yellow-500",
    CLOSED: "bg-gray-500",
    ON_HOLD: "bg-purple-500",
    PAYMENT_REVIEW: "bg-teal-500",
    PENDING_PAYMENT: "bg-orange-500",
    SUSPECTED_FRAUD: "bg-red-800",
  };

  const statusWidth = {
    PROCESSING: "50%",
    COMPLETE: "100%",
    PENDING: "30%",
    CANCELED: "100%",
    ON_HOLD: "40%",
    CLOSED: "100%",
    PAYMENT_REVIEW: "60%",
    PENDING_PAYMENT: "40%",
    SUSPECTED_FRAUD: "100%",
  };

  return (
    <div className="w-full mt-4">
      <div className="bg-gray-700 rounded-lg overflow-hidden">
        <div
          className={`h-4 ${statusColors[status] || "bg-gray-400"} transition-all duration-500`}
          style={{ width: statusWidth[status] || "60%" }}
        ></div>
      </div>
      <p className="text-center mt-2 text-lg font-semibold">{status}</p>
    </div>
  );
}
