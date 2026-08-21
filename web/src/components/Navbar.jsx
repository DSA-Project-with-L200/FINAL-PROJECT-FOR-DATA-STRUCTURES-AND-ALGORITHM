import React, { useState, useEffect } from 'react';
import { Map, Zap, Layers, Cpu, BarChart3, Database, Server, CheckCircle2 } from 'lucide-react';
import { fetchBackendStatus } from '../data/campusData';

export default function Navbar({ activeTab, setActiveTab }) {
  const [backendOnline, setBackendOnline] = useState(false);

  useEffect(() => {
    fetchBackendStatus().then((res) => {
      if (res && res.status === 'ONLINE') setBackendOnline(true);
    });
  }, []);

  return (
    <header className="navbar">
      <div className="brand">
        <div className="brand-badge">UG</div>
        <div className="brand-text">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <h1>Ghana Smart Operations Optimizer</h1>
            {backendOnline ? (
              <span style={{ fontSize: '0.7rem', background: '#dcfce7', color: '#15803d', padding: '2px 8px', borderRadius: '12px', fontWeight: 'bold', display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                <CheckCircle2 size={12} /> Live SQLite Backend (`campus_dispatch.db`)
              </span>
            ) : (
              <span style={{ fontSize: '0.7rem', background: '#e0f2fe', color: '#0369a1', padding: '2px 8px', borderRadius: '12px', fontWeight: 'bold', display: 'inline-flex', alignItems: 'center', gap: '4px' }}>
                <Server size={12} /> Web Visualizer Active
              </span>
            )}
          </div>
          <p>UG Campus Taxi & Emergency Dispatch (Legon Context)</p>
        </div>
      </div>

      <nav className="nav-tabs">
        <button
          className={`nav-btn ${activeTab === 'map' ? 'active' : ''}`}
          onClick={() => setActiveTab('map')}
        >
          <Map size={16} /> Campus Network Map
        </button>
        <button
          className={`nav-btn ${activeTab === 'dispatch' ? 'active' : ''}`}
          onClick={() => setActiveTab('dispatch')}
        >
          <Zap size={16} /> Priority Dispatch Engine
        </button>
        <button
          className={`nav-btn ${activeTab === 'ds' ? 'active' : ''}`}
          onClick={() => setActiveTab('ds')}
        >
          <Layers size={16} /> Data Structure Inspector
        </button>
        <button
          className={`nav-btn ${activeTab === 'knapsack' ? 'active' : ''}`}
          onClick={() => setActiveTab('knapsack')}
        >
          <Cpu size={16} /> Greedy vs DP
        </button>
        <button
          className={`nav-btn ${activeTab === 'benchmarks' ? 'active' : ''}`}
          onClick={() => setActiveTab('benchmarks')}
        >
          <BarChart3 size={16} /> Benchmarks
        </button>
        <button
          className={`nav-btn ${activeTab === 'db' ? 'active' : ''}`}
          onClick={() => setActiveTab('db')}
        >
          <Database size={16} /> SQLite Database
        </button>
      </nav>
    </header>
  );
}
