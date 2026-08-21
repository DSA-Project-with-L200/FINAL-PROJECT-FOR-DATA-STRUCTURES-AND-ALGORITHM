import React, { useState } from 'react';
import Navbar from './components/Navbar';
import CampusMap from './components/CampusMap';
import DispatchEngine from './components/DispatchEngine';
import DataStructuresInspector from './components/DataStructuresInspector';
import KnapsackOptimizer from './components/KnapsackOptimizer';
import BenchmarkRunner from './components/BenchmarkRunner';
import DatabaseBrowser from './components/DatabaseBrowser';
import TraceLogger from './components/TraceLogger';

export default function App() {
  const [activeTab, setActiveTab] = useState('map');
  const [logs, setLogs] = useState([
    { time: new Date().toLocaleTimeString(), tag: 'INIT', message: 'Loaded 50 University of Ghana campus locations & 100 road edges.' }
  ]);

  const addLog = (tag, message) => {
    const newLog = {
      time: new Date().toLocaleTimeString(),
      tag,
      message
    };
    setLogs((prev) => [newLog, ...prev.slice(0, 49)]);
  };

  return (
    <div className="app-container">
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />

      <main className="main-layout" style={{ display: 'block', padding: '1.5rem 2rem' }}>
        {activeTab === 'map' && <CampusMap onLog={addLog} />}
        {activeTab === 'dispatch' && <DispatchEngine onLog={addLog} />}
        {activeTab === 'ds' && <DataStructuresInspector />}
        {activeTab === 'knapsack' && <KnapsackOptimizer />}
        {activeTab === 'benchmarks' && <BenchmarkRunner onLog={addLog} />}
        {activeTab === 'db' && <DatabaseBrowser />}

        <TraceLogger logs={logs} />
      </main>
    </div>
  );
}
