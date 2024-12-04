import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { checkSystemConfigured } from "./dummyApi";
import Header from "./components/Header";
import VendorPage from "./pages/VendorPage";
import CustomerPage from "./pages/CustomerPage";
import SystemHaltedPage from "./pages/SystemHaltedPage";
import AdminPage from "./pages/AdminPage";

const App = () => {
  const [isConfigured, setIsConfigured] = useState(null); // null to indicate loading state

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const status = await checkSystemConfigured();
        setIsConfigured(status);
      } catch (error) {
        console.error("Failed to check system status:", error);
        setIsConfigured(false); // Assume the system is not configured if the request fails
      }
    };

    fetchStatus();
  }, []);

  if (isConfigured === null) {
    return <div>Loading...</div>; // Show a loading state
  }

  return (
    <Router>
      <Header />
      <div style={{ padding: 20 }}>
        <Routes>
          {/* Admin route bypasses isConfigured check */}
          <Route path="/admin" element={<AdminPage />} />
          {/* Non-admin routes */}
          {isConfigured ? (
            <>
              <Route path="/vendor" element={<VendorPage />} />
              <Route path="/customer" element={<CustomerPage />} />
            </>
          ) : (
            <Route path="*" element={<SystemHaltedPage />} />
          )}
        </Routes>
      </div>
    </Router>
  );
};

export default App;
