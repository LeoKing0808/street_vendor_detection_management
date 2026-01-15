import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import CameraDetail from './pages/CameraDetail';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/camera/:cameraId" element={<CameraDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
