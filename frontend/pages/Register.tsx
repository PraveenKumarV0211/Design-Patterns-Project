import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const Register: React.FC = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await api.post('/auth/register', { username, email, password });
            setSuccess('Registered! Redirecting to login...');
            setTimeout(() => navigate('/login'), 1500);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Registration failed');
        }
    };

    return (
        <div style={{ maxWidth: 400, margin: '60px auto' }}>
            <h2>Register</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: 12 }}>
                    <label>Username</label><br />
                    <input value={username} onChange={e => setUsername(e.target.value)}
                           style={{ width: '100%', padding: 8 }} required />
                </div>
                <div style={{ marginBottom: 12 }}>
                    <label>Email</label><br />
                    <input type="email" value={email} onChange={e => setEmail(e.target.value)}
                           style={{ width: '100%', padding: 8 }} required />
                </div>
                <div style={{ marginBottom: 12 }}>
                    <label>Password</label><br />
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)}
                           style={{ width: '100%', padding: 8 }} minLength={6} required />
                </div>
                <button type="submit" style={{ padding: '8px 24px', backgroundColor: '#1a1a2e', color: '#fff', border: 'none', borderRadius: 4 }}>
                    Register
                </button>
            </form>
        </div>
    );
};

export default Register;