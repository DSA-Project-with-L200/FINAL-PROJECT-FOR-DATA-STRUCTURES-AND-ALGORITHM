import React from 'react';

export default function TraceLogger({ logs }) {
  return (
    <div className="card" style={{ marginTop: '1.5rem', marginBottom: 0 }}>
      <div className="card-title" style={{ fontSize: '0.85rem' }}>
        <span>📜 Live Execution Audit Log & Trace Console</span>
        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{logs.length} Log Entries</span>
      </div>

      <div className="console-log">
        {logs.length === 0 ? (
          <div style={{ color: 'var(--text-muted)' }}>[SYSTEM]: Engine initialized. Ready for operations.</div>
        ) : (
          logs.map((log, idx) => (
            <div key={idx} className="log-item">
              <span className="time">[{log.time}]</span> <span className="tag">[{log.tag}]</span> {log.message}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
