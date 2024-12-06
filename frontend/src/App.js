import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import ConfigurationPage from "./pages/ConfigurationPage";
import SimulationPage from "./pages/simulationPage";

const App = () => {

  return (
    <Router>
      <Header />
      <div style={{ padding: 20 }}>
        <Routes>
          {/* Admin route bypasses isConfigured check */}
          <Route path="/config" element={<ConfigurationPage />} />
          <Route path="/simulation" element={<SimulationPage />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
