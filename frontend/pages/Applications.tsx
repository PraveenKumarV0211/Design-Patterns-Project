import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Application } from '../types/types';

const STATUS_COLORS: Record<string, string> = {
    APPLIED: '#3498db', OA: '#f39c12', INTERVIEW: '#9b59b6',
    OFFER: '#2ecc71', REJECTED: '#e74c3c', WITHDRAWN: '#95a5a6',
};

const Applications: React.FC = () => {
    const [apps, setApps] = useState<Application[]>([]);
    const [showForm, setShowForm] = useState(false);
    const [editId, setEditId] = useState<number | null>(null);
    const [sortBy, setSortBy] = useState('date');
    const [searchTerm, setSearchTerm] = useState('');
    const [filterStatus, setFilterStatus] = useState('');
    const userId = localStorage.getItem('userId');

    // Form state
    const [companyName, setCompanyName] = useState('');
    const [roleName, setRoleName] = useState('');
    const [jobType, setJobType] = useState<'FULLTIME' | 'INTERNSHIP' | 'CONTRACT'>('FULLTIME');
    const [notes, setNotes] = useState('');
    const [referralInfo, setReferralInfo] = useState('');
    const [salaryDetails, setSalaryDetails] = useState('');
    const [interviewNotes, setInterviewNotes] = useState('');

    const fetchApps = async () => {
        if (!userId) return;
        let url = `/applications/user/${userId}/sort?sortBy=${sortBy}`;
        if (searchTerm) {
            url = `/applications/user/${userId}/search?company=${searchTerm}`;
        } else if (filterStatus) {
            url = `/applications/user/${userId}/filter/status?status=${filterStatus}`;
        }
        const res = await api.get(url);
        setApps(res.data);
    };

    useEffect(() => { fetchApps(); }, [sortBy, filterStatus]);

    const handleSearch = () => { fetchApps(); };

    const resetForm = () => {
        setCompanyName(''); setRoleName(''); setJobType('FULLTIME');
        setNotes(''); setReferralInfo(''); setSalaryDetails(''); setInterviewNotes('');
        setEditId(null); setShowForm(false);
    };

    const handleSubmit = async () => {
        const dto: any = { companyName, roleName, jobType, notes };
        if (referralInfo) dto.referralInfo = referralInfo;
        if (salaryDetails) dto.salaryDetails = parseFloat(salaryDetails);
        if (interviewNotes) dto.interviewNotes = interviewNotes;

        if (editId) {
            await api.put(`/applications/${editId}`, dto);
        } else {
            await api.post(`/applications/user/${userId}`, dto);
        }
        resetForm();
        fetchApps();
    };

    const handleEdit = (app: Application) => {
        setCompanyName(app.companyName); setRoleName(app.roleName);
        setJobType(app.jobType); setNotes(app.notes || '');
        setReferralInfo(app.referralInfo || ''); setSalaryDetails(app.salaryDetails?.toString() || '');
        setInterviewNotes(app.interviewNotes || '');
        setEditId(app.id); setShowForm(true);
    };

    const handleDelete = async (id: number) => {
        await api.delete(`/applications/${id}`);
        fetchApps();
    };

    const handleTransition = async (id: number, action: string) => {
        await api.put(`/applications/${id}/transition?action=${action}`);
        fetchApps();
    };

    const inputStyle: React.CSSProperties = { width: '100%', padding: 8, marginBottom: 8, boxSizing: 'border-box' };
    const btnStyle: React.CSSProperties = {
        padding: '4px 10px', border: 'none', borderRadius: 4, cursor: 'pointer',
        color: '#fff', fontSize: 12, marginRight: 4,
    };

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
                <h2>Applications</h2>
                <button onClick={() => { resetForm(); setShowForm(!showForm); }}
                        style={{ padding: '8px 16px', backgroundColor: '#2ecc71', color: '#fff', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
                    {showForm ? 'Cancel' : '+ New Application'}
                </button>
            </div>

            {/* Search, Filter, Sort */}
            <div style={{ display: 'flex', gap: 12, marginBottom: 16, flexWrap: 'wrap', alignItems: 'center' }}>
                <input placeholder="Search by company..." value={searchTerm}
                       onChange={e => setSearchTerm(e.target.value)}
                       onKeyDown={e => e.key === 'Enter' && handleSearch()}
                       style={{ padding: 8, flex: 1, minWidth: 180 }} />
                <button onClick={handleSearch}
                        style={{ ...btnStyle, backgroundColor: '#3498db', padding: '8px 14px', fontSize: 14 }}>Search</button>
                <select value={filterStatus} onChange={e => { setFilterStatus(e.target.value); setSearchTerm(''); }}
                        style={{ padding: 8 }}>
                    <option value="">All Statuses</option>
                    <option value="APPLIED">Applied</option>
                    <option value="OA">OA</option>
                    <option value="INTERVIEW">Interview</option>
                    <option value="OFFER">Offer</option>
                    <option value="REJECTED">Rejected</option>
                    <option value="WITHDRAWN">Withdrawn</option>
                </select>
                <select value={sortBy} onChange={e => { setSortBy(e.target.value); setSearchTerm(''); setFilterStatus(''); }}
                        style={{ padding: 8 }}>
                    <option value="date">Sort by Date</option>
                    <option value="company">Sort by Company</option>
                </select>
            </div>

            {/* Create/Edit Form */}
            {showForm && (
                <div style={{ background: '#f8f9fa', padding: 20, borderRadius: 8, marginBottom: 20 }}>
                    <h3>{editId ? 'Edit Application' : 'New Application'}</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                        <div>
                            <label>Company *</label>
                            <input value={companyName} onChange={e => setCompanyName(e.target.value)} style={inputStyle} required />
                        </div>
                        <div>
                            <label>Role *</label>
                            <input value={roleName} onChange={e => setRoleName(e.target.value)} style={inputStyle} required />
                        </div>
                        <div>
                            <label>Job Type *</label>
                            <select value={jobType} onChange={e => setJobType(e.target.value as any)} style={inputStyle}>
                                <option value="FULLTIME">Full Time</option>
                                <option value="INTERNSHIP">Internship</option>
                                <option value="CONTRACT">Contract</option>
                            </select>
                        </div>
                        <div>
                            <label>Salary</label>
                            <input type="number" value={salaryDetails} onChange={e => setSalaryDetails(e.target.value)} style={inputStyle} />
                        </div>
                        <div>
                            <label>Referral Info</label>
                            <input value={referralInfo} onChange={e => setReferralInfo(e.target.value)} style={inputStyle} />
                        </div>
                        <div>
                            <label>Interview Notes</label>
                            <input value={interviewNotes} onChange={e => setInterviewNotes(e.target.value)} style={inputStyle} />
                        </div>
                    </div>
                    <div style={{ marginTop: 8 }}>
                        <label>Notes</label>
                        <textarea value={notes} onChange={e => setNotes(e.target.value)}
                                  style={{ ...inputStyle, height: 60 }} />
                    </div>
                    <button onClick={handleSubmit}
                            style={{ padding: '8px 24px', backgroundColor: '#1a1a2e', color: '#fff', border: 'none', borderRadius: 4, cursor: 'pointer', marginTop: 8 }}>
                        {editId ? 'Update' : 'Create'}
                    </button>
                </div>
            )}

            {/* Applications Table */}
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr style={{ background: '#1a1a2e', color: '#fff', textAlign: 'left' }}>
                    <th style={{ padding: 10 }}>Company</th>
                    <th style={{ padding: 10 }}>Role</th>
                    <th style={{ padding: 10 }}>Type</th>
                    <th style={{ padding: 10 }}>Status</th>
                    <th style={{ padding: 10 }}>Date</th>
                    <th style={{ padding: 10 }}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {apps.map(app => (
                    <tr key={app.id} style={{ borderBottom: '1px solid #eee' }}>
                        <td style={{ padding: 10 }}>{app.companyName}</td>
                        <td style={{ padding: 10 }}>{app.roleName}</td>
                        <td style={{ padding: 10 }}>{app.jobType}</td>
                        <td style={{ padding: 10 }}>
                <span style={{
                    background: STATUS_COLORS[app.status] || '#ccc',
                    color: '#fff', padding: '3px 10px', borderRadius: 12, fontSize: 12,
                }}>{app.status}</span>
                        </td>
                        <td style={{ padding: 10 }}>{app.applicationDate}</td>
                        <td style={{ padding: 10 }}>
                            {!['OFFER', 'REJECTED', 'WITHDRAWN'].includes(app.status) && (
                                <button onClick={() => handleTransition(app.id, 'next')}
                                        style={{ ...btnStyle, backgroundColor: '#2ecc71' }}>Next</button>
                            )}
                            {!['REJECTED', 'WITHDRAWN'].includes(app.status) && (
                                <button onClick={() => handleTransition(app.id, 'reject')}
                                        style={{ ...btnStyle, backgroundColor: '#e74c3c' }}>Reject</button>
                            )}
                            {!['WITHDRAWN', 'REJECTED'].includes(app.status) && (
                                <button onClick={() => handleTransition(app.id, 'withdraw')}
                                        style={{ ...btnStyle, backgroundColor: '#95a5a6' }}>Withdraw</button>
                            )}
                            <button onClick={() => handleEdit(app)}
                                    style={{ ...btnStyle, backgroundColor: '#f39c12' }}>Edit</button>
                            <button onClick={() => handleDelete(app.id)}
                                    style={{ ...btnStyle, backgroundColor: '#c0392b' }}>Delete</button>
                        </td>
                    </tr>
                ))}
                {apps.length === 0 && (
                    <tr><td colSpan={6} style={{ padding: 20, textAlign: 'center', color: '#999' }}>No applications found</td></tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default Applications;