import React, { useEffect, useState } from 'react';
import NotificationService, { NotificationType } from '../services/NotificationService';

interface ToastMessage {
    id: number;
    message: string;
    type: NotificationType;
}

const COLORS: Record<NotificationType, string> = {
    info: '#8e44ad',
    success: '#27ae60',
    error: '#e74c3c',
};

const ICONS: Record<NotificationType, string> = {
    info: '⏳',
    success: '✓',
    error: '✗',
};

let nextId = 0;

const Toast: React.FC = () => {
    const [toasts, setToasts] = useState<ToastMessage[]>([]);

    useEffect(() => {
        // Subscribe to NotificationService (Observer Pattern)
        const unsubscribe = NotificationService.subscribe((message, type) => {
            const id = ++nextId;
            setToasts(prev => [...prev, { id, message, type }]);
            const duration = type === 'info' ? 12000 : 4000;
            setTimeout(() => setToasts(prev => prev.filter(t => t.id !== id)), duration);
        });
        return unsubscribe;
    }, []);

    if (toasts.length === 0) return null;

    return (
        <div style={{ position: 'fixed', bottom: 24, right: 24, zIndex: 9999, display: 'flex', flexDirection: 'column', gap: 10 }}>
            {toasts.map(toast => (
                <div key={toast.id} style={{
                    background: COLORS[toast.type],
                    color: '#fff',
                    padding: '12px 20px',
                    borderRadius: 8,
                    boxShadow: '0 4px 12px rgba(0,0,0,0.2)',
                    fontSize: 14,
                    maxWidth: 340,
                    display: 'flex',
                    alignItems: 'center',
                    gap: 10,
                    animation: 'slideIn 0.2s ease',
                }}>
                    <span style={{ fontSize: 16 }}>{ICONS[toast.type]}</span>
                    <span>{toast.message}</span>
                    <button onClick={() => setToasts(prev => prev.filter(t => t.id !== toast.id))}
                        style={{ marginLeft: 'auto', background: 'none', border: 'none', color: '#fff', cursor: 'pointer', fontSize: 16, lineHeight: 1 }}>
                        ×
                    </button>
                </div>
            ))}
            <style>{`@keyframes slideIn { from { transform: translateX(60px); opacity: 0; } to { transform: translateX(0); opacity: 1; } }`}</style>
        </div>
    );
};

export default Toast;
