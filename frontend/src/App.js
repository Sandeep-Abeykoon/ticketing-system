import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import ConfigurationPage from "./pages/ConfigurationPage";
import SimulationPage from "./pages/simulationPage";
import AnalyticsDashboard from "./pages/DashboardPage";
import TransactionsPage from "./pages/TransactionsPage";
import { WebSocketProvider } from "./components/context/WebSocketContext";

const App = () => {

  return (
    <WebSocketProvider>
      <Router>
        <Header />
        <div style={{ padding: 20 }}>
          <Routes>
            <Route path="/config" element={<ConfigurationPage />} />
            <Route path="/simulation" element={<SimulationPage />} />
            <Route path="/dashboard" element={<AnalyticsDashboard />} />
            <Route path="/transactions" element={<TransactionsPage />} />
          </Routes>
        </div>
      </Router>
    </WebSocketProvider>
  );
};

export default App;
