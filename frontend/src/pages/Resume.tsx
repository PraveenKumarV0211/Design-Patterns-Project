import React, { useEffect, useRef, useState } from 'react';
import api from '../services/api';

const Resume: React.FC = () => {
    const [resumeText, setResumeText] = useState('');
    const [saved, setSaved] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');
    const fileRef = useRef<HTMLInputElement>(null);
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        if (!userId) return;
        api.get(`/users/${userId}/resume`)
            .then(res => setResumeText(res.data.resumeText || ''))
            .catch(() => setError('Failed to load resume.'));
    }, [userId]);

    const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file || !userId) return;
        setUploading(true);
        setError('');
        setSaved(false);
        const form = new FormData();
        form.append('file', file);
        try {
            await api.post(`/users/${userId}/resume/upload`, form, {
                headers: { 'Content-Type': 'multipart/form-data' },
            });
            const res = await api.get(`/users/${userId}/resume`);
            setResumeText(res.data.resumeText || '');
            setSaved(true);
        } catch {
            setError('Upload failed. Make sure the file is a PDF or plain text.');
        } finally {
            setUploading(false);
            if (fileRef.current) fileRef.current.value = '';
        }
    };

    const handleSaveText = async () => {
        if (!userId) return;
        setSaving(true);
        setError('');
        setSaved(false);
        try {
            await api.put(`/users/${userId}/resume`, { resumeText });
            setSaved(true);
        } catch {
            setError('Failed to save resume.');
        } finally {
            setSaving(false);
        }
    };

    const cardStyle: React.CSSProperties = {
        background: '#fff', borderRadius: 8, padding: 24, boxShadow: '0 2px 8px rgba(0,0,0,0.08)',
    };
    const btnStyle: React.CSSProperties = {
        padding: '8px 20px', border: 'none', borderRadius: 4, cursor: 'pointer',
        color: '#fff', fontSize: 14,
    };

    return (
        <div>
            <h2 style={{ marginBottom: 8 }}>My Resume</h2>
            <p style={{ color: '#666', marginBottom: 24 }}>
                Your resume is used to generate personalised cover letters. Upload a PDF or paste the text below.
            </p>

            <div style={cardStyle}>
                <h3 style={{ marginTop: 0, marginBottom: 12 }}>Upload PDF</h3>
                <input ref={fileRef} type="file" accept=".pdf,.txt" onChange={handleFileUpload} disabled={uploading} />
                {uploading && <span style={{ marginLeft: 12, color: '#888' }}>Extracting text…</span>}
            </div>

            <div style={{ ...cardStyle, marginTop: 20 }}>
                <h3 style={{ marginTop: 0, marginBottom: 12 }}>Resume Text</h3>
                <textarea
                    value={resumeText}
                    onChange={e => { setResumeText(e.target.value); setSaved(false); }}
                    placeholder="Paste your resume text here, or upload a PDF above…"
                    style={{ width: '100%', height: 400, padding: 12, fontSize: 13, fontFamily: 'monospace', boxSizing: 'border-box', border: '1px solid #ddd', borderRadius: 4, resize: 'vertical' }}
                />
                <div style={{ marginTop: 12, display: 'flex', alignItems: 'center', gap: 12 }}>
                    <button onClick={handleSaveText} disabled={saving} style={{ ...btnStyle, backgroundColor: '#1a1a2e' }}>
                        {saving ? 'Saving…' : 'Save Resume'}
                    </button>
                    {saved && <span style={{ color: '#2ecc71', fontWeight: 500 }}>Saved!</span>}
                    {error && <span style={{ color: '#e74c3c' }}>{error}</span>}
                </div>
            </div>
        </div>
    );
};

export default Resume;
