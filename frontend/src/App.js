import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import VendorPage from './pages/VendorPage';
import CustomerPage from './pages/CustomerPage';

const App = () => {
  return (
    <Router>
      <Header />
      <div style={{ padding: 20 }}>
        <Routes>
          <Route path="/vendor" element={<VendorPage />} />
          <Route path="/customer" element={<CustomerPage />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
