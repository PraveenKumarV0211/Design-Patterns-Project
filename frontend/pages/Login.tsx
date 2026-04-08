import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await api.post('/auth/login', { username, password });
            localStorage.setItem('userId', res.data.id);
            localStorage.setItem('username', username);
            localStorage.setItem('password', password);
            navigate('/dashboard');
            window.location.reload();
        } catch {
            setError('Invalid credentials');
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: '60px auto' }}>
            <h2>Login</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: 12 }}>
                    <label>Username</label><br />
                    <input value={username} onChange={e => setUsername(e.target.value)}
                           style={{ width: '100%', padding: 8 }} required />
                </div>
                <div style={{ marginBottom: 12 }}>
                    <label>Password</label><br />
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)}
                           style={{ width: '100%', padding: 8 }} required />
                </div>
                <button type="submit" style={{ padding: '8px 24px', backgroundColor: '#1a1a2e', color: '#fff', border: 'none', borderRadius: 4 }}>
                    Login
                </button>
            </form>
        </div>
    );
};

export default Login;