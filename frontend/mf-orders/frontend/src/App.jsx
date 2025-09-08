import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import OrdersList from "./pages/OrdersList";
import OrderDetails from "./pages/OrderDetails";
import AddOrder from "./pages/AddOrder";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<OrdersList />} />
        <Route path="/orders/:id" element={<OrderDetails />} />
        <Route path="/add" element={<AddOrder />} />
      </Routes>
    </Router>
  );
}

export default App;
