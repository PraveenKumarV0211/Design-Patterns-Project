import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Toast from './components/Toast';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Applications from './pages/Applications';
import Resume from './pages/Resume';

const App: React.FC = () => {
    const isLoggedIn = !!localStorage.getItem('userId');

    return (
        <Router>
            <Navbar />
            <Toast />
            <div style={{ maxWidth: 1100, margin: '0 auto', padding: '20px' }}>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/dashboard" element={isLoggedIn ? <Dashboard /> : <Navigate to="/login" />} />
                    <Route path="/applications" element={isLoggedIn ? <Applications /> : <Navigate to="/login" />} />
                    <Route path="/resume" element={isLoggedIn ? <Resume /> : <Navigate to="/login" />} />
                    <Route path="*" element={<Navigate to={isLoggedIn ? "/dashboard" : "/login"} />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;