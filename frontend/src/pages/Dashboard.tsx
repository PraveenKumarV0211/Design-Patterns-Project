import React, { useEffect, useState } from 'react';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement } from 'chart.js';
import { Pie, Bar } from 'react-chartjs-2';
import api from '../services/api';
import { DashboardData } from '../types/types';

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement);

const STATUS_COLORS: Record<string, string> = {
    APPLIED: '#3498db', OA: '#f39c12', INTERVIEW: '#9b59b6',
    OFFER: '#2ecc71', REJECTED: '#e74c3c', WITHDRAWN: '#95a5a6',
};

const Dashboard: React.FC = () => {
    const [data, setData] = useState<DashboardData | null>(null);
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        if (userId) {
            api.get(`/applications/user/${userId}/dashboard`).then(res => setData(res.data));
        }
    }, [userId]);

    if (!data) return <p>Loading...</p>;

    const statusLabels = Object.keys(data.statusBreakdown);
    const statusValues = Object.values(data.statusBreakdown);

    const pieData = {
        labels: statusLabels,
        datasets: [{
            data: statusValues,
            backgroundColor: statusLabels.map(s => STATUS_COLORS[s] || '#ccc'),
        }],
    };

    const timeLabels = Object.keys(data.applicationsOverTime);
    const timeValues = Object.values(data.applicationsOverTime);

    const barData = {
        labels: timeLabels,
        datasets: [{
            label: 'Applications',
            data: timeValues,
            backgroundColor: '#3498db',
        }],
    };

    const cardStyle: React.CSSProperties = {
        background: '#f8f9fa', borderRadius: 8, padding: 20, textAlign: 'center', flex: 1, minWidth: 150,
    };

    return (
        <div>
            <h2>Dashboard</h2>
            <div style={{ display: 'flex', gap: 16, marginBottom: 24, flexWrap: 'wrap' }}>
                <div style={cardStyle}>
                    <h3>{data.totalApplications}</h3>
                    <p>Total Applications</p>
                </div>
                <div style={cardStyle}>
                    <h3>{data.successRate.toFixed(1)}%</h3>
                    <p>Success Rate</p>
                </div>
                <div style={cardStyle}>
                    <h3>{data.statusBreakdown['OFFER'] || 0}</h3>
                    <p>Offers</p>
                </div>
                <div style={cardStyle}>
                    <h3>{data.statusBreakdown['INTERVIEW'] || 0}</h3>
                    <p>Interviews</p>
                </div>
            </div>
            <div style={{ display: 'flex', gap: 24, flexWrap: 'wrap' }}>
                <div style={{ flex: 1, minWidth: 300, maxWidth: 400 }}>
                    <h3>Status Breakdown</h3>
                    <Pie data={pieData} />
                </div>
                <div style={{ flex: 2, minWidth: 300 }}>
                    <h3>Applications Over Time</h3>
                    <Bar data={barData} />
                </div>
            </div>
        </div>
    );
};

export default Dashboard;