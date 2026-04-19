import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const isLoggedIn = !!localStorage.getItem('userId');

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
    window.location.reload();
  };

  const navStyle: React.CSSProperties = {
    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
    padding: '12px 24px', backgroundColor: '#1a1a2e', color: '#fff',
  };
  const linkStyle: React.CSSProperties = {
    color: '#e0e0e0', textDecoration: 'none', marginRight: 16, fontSize: 14,
  };

  return (
    <nav style={navStyle}>
      <strong style={{ fontSize: 18 }}>Job Tracker</strong>
      <div>
        {isLoggedIn ? (
          <>
            <Link to="/dashboard" style={linkStyle}>Dashboard</Link>
            <Link to="/applications" style={linkStyle}>Applications</Link>
            <Link to="/resume" style={linkStyle}>Resume</Link>
            <button onClick={handleLogout}
              style={{ background: '#e94560', color: '#fff', border: 'none', padding: '6px 14px', borderRadius: 4, cursor: 'pointer' }}>
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" style={linkStyle}>Login</Link>
            <Link to="/register" style={linkStyle}>Register</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;