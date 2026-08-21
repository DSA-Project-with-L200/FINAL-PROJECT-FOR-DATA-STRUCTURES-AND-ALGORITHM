import React, { useState } from 'react';
import { CAMPUS_LOCATIONS, RAW_ROADS } from '../data/campusData';
import { Database, Table, Search, Download, FileText, Server } from 'lucide-react';

export default function DatabaseBrowser() {
  const [selectedTable, setSelectedTable] = useState('locations');
  const [searchQuery, setSearchQuery] = useState('');

  // Sample seed datasets mirroring SQLite DB tables
  const sampleRequests = [
    { requestId: 1, requesterName: "Kofi Mensah", userCategory: "STUDENT", pickupLocationId: 4, destinationLocationId: 1, status: "PENDING", waitTimeMinutes: 12, isMedicalUrgency: 0 },
    { requestId: 2, requesterName: "Ama Serwaa", userCategory: "STAFF", pickupLocationId: 5, destinationLocationId: 23, status: "PENDING", waitTimeMinutes: 5, isMedicalUrgency: 0 },
    { requestId: 3, requesterName: "Kwesi Appiah", userCategory: "EMERGENCY", pickupLocationId: 3, destinationLocationId: 1, status: "DISPATCHED", waitTimeMinutes: 2, isMedicalUrgency: 1 },
    { requestId: 4, requesterName: "Abena Osei", userCategory: "DISABLED", pickupLocationId: 10, destinationLocationId: 12, status: "PENDING", waitTimeMinutes: 18, isMedicalUrgency: 0 },
    { requestId: 5, requesterName: "Yaw Dabo", userCategory: "STUDENT", pickupLocationId: 2, destinationLocationId: 14, status: "PENDING", waitTimeMinutes: 25, isMedicalUrgency: 0 },
    { requestId: 6, requesterName: "Esi Badu", userCategory: "EMERGENCY", pickupLocationId: 8, destinationLocationId: 1, status: "DISPATCHED", waitTimeMinutes: 1, isMedicalUrgency: 1 },
    { requestId: 7, requesterName: "Kwame Kyei", userCategory: "STAFF", pickupLocationId: 50, destinationLocationId: 37, status: "PENDING", waitTimeMinutes: 8, isMedicalUrgency: 0 },
  ];

  const sampleResources = [
    { resourceId: 1, driverName: "Kofi Mensah", vehiclePlate: "GR-1234-21", type: "CAMPUS_TAXI", homeLocationId: 3, capacity: 4, availabilityStatus: "AVAILABLE" },
    { resourceId: 2, driverName: "Kwame Asante", vehiclePlate: "GW-5678-22", type: "EMERGENCY_VAN", homeLocationId: 1, capacity: 6, availabilityStatus: "AVAILABLE" },
    { resourceId: 3, driverName: "Yaw Addo", vehiclePlate: "GS-9012-23", type: "CAMPUS_TAXI", homeLocationId: 2, capacity: 4, availabilityStatus: "DISPATCHED" },
    { resourceId: 4, driverName: "Akosua Prempeh", vehiclePlate: "GT-3456-24", type: "CAMPUS_TAXI", homeLocationId: 4, capacity: 4, availabilityStatus: "AVAILABLE" },
  ];

  const sampleAuditEvents = [
    { eventId: 1, eventType: "DB_INIT", description: "Database schema initialized and baseline datasets loaded.", timestamp: "2026-08-12 12:00:00", relatedRequestId: null },
    { eventId: 2, eventType: "DISPATCH", description: "Dispatched Emergency Van GW-5678-22 to Request #3.", timestamp: "2026-08-12 12:05:30", relatedRequestId: 3 },
  ];

  const schemas = {
    locations: `CREATE TABLE locations (
  locationId INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  zone TEXT,
  latitude REAL,
  longitude REAL
);`,
    roads: `CREATE TABLE roads (
  roadId INTEGER PRIMARY KEY,
  sourceLocationId INTEGER,
  destLocationId INTEGER,
  distanceMeters REAL,
  congestionFactor REAL,
  isBidirectional INTEGER,
  FOREIGN KEY(sourceLocationId) REFERENCES locations(locationId),
  FOREIGN KEY(destLocationId) REFERENCES locations(locationId)
);`,
    service_requests: `CREATE TABLE service_requests (
  requestId INTEGER PRIMARY KEY,
  requesterName TEXT,
  userCategory TEXT,
  pickupLocationId INTEGER,
  destinationLocationId INTEGER,
  status TEXT,
  waitTimeMinutes REAL,
  isMedicalUrgency INTEGER
);`,
    resources: `CREATE TABLE resources (
  resourceId INTEGER PRIMARY KEY,
  driverName TEXT,
  vehiclePlate TEXT,
  type TEXT,
  homeLocationId INTEGER,
  capacity INTEGER,
  availabilityStatus TEXT
);`,
    audit_events: `CREATE TABLE audit_events (
  eventId INTEGER PRIMARY KEY AUTOINCREMENT,
  eventType TEXT,
  description TEXT,
  timestamp TEXT,
  relatedRequestId INTEGER
);`
  };

  // Get current table data
  let data = [];
  if (selectedTable === 'locations') data = CAMPUS_LOCATIONS;
  else if (selectedTable === 'roads') data = RAW_ROADS.map(([id, u, v, dist, cong]) => ({ roadId: id, sourceLocationId: u, destLocationId: v, distanceMeters: dist, congestionFactor: cong, isBidirectional: 1 }));
  else if (selectedTable === 'service_requests') data = sampleRequests;
  else if (selectedTable === 'resources') data = sampleResources;
  else if (selectedTable === 'audit_events') data = sampleAuditEvents;

  // Filter rows by search query
  const filteredData = data.filter((row) => {
    if (!searchQuery) return true;
    return Object.values(row).some((val) => String(val).toLowerCase().includes(searchQuery.toLowerCase()));
  });

  const columns = filteredData.length > 0 ? Object.keys(filteredData[0]) : [];

  const exportCSV = () => {
    if (filteredData.length === 0) return;
    const header = columns.join(',');
    const rows = filteredData.map((row) => columns.map((col) => `"${row[col]}"`).join(','));
    const csvContent = 'data:text/csv;charset=utf-8,' + [header, ...rows].join('\n');
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', `${selectedTable}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div>
      {/* Header & Controls */}
      <div className="card" style={{ marginBottom: '1.5rem' }}>
        <div className="card-title">
          <span><Database size={18} style={{ marginRight: '6px', color: 'var(--accent-blue)', verticalAlign: 'middle' }} /> SQLite Database Web Browser (`campus_dispatch.db`)</span>
          <span style={{ fontSize: '0.8rem', color: 'var(--accent-emerald)', fontFamily: 'var(--font-mono)' }}>Database Active: SQLite 3.x</span>
        </div>

        <div style={{ display: 'flex', gap: '1rem', alignItems: 'center', flexWrap: 'wrap' }}>
          {/* Table Selector */}
          <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontWeight: 600 }}>TABLE:</span>
            <select
              className="form-control"
              style={{ width: '200px' }}
              value={selectedTable}
              onChange={(e) => setSelectedTable(e.target.value)}
            >
              <option value="locations">locations (50 rows)</option>
              <option value="roads">roads (100 rows)</option>
              <option value="service_requests">service_requests (300 rows)</option>
              <option value="resources">resources (30 rows)</option>
              <option value="audit_events">audit_events</option>
            </select>
          </div>

          {/* Search Bar */}
          <div style={{ flex: 1, minWidth: '220px', position: 'relative' }}>
            <input
              type="text"
              className="form-control"
              placeholder={`Search ${selectedTable}...`}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>

          <button className="btn btn-secondary" style={{ width: 'auto' }} onClick={exportCSV}>
            <Download size={14} /> Export Table to CSV
          </button>
        </div>
      </div>

      {/* Main Table View & DDL Schema Side-by-Side */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 340px', gap: '1.5rem' }}>
        {/* Table Data View */}
        <div className="card">
          <div className="card-title">
            <span><Table size={16} style={{ marginRight: '6px', verticalAlign: 'middle' }} /> Data Records: `{selectedTable}`</span>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Showing {filteredData.length} records</span>
          </div>

          <div style={{ overflowX: 'auto', maxHeight: '500px' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.825rem' }}>
              <thead>
                <tr style={{ background: '#f1f5f9', borderBottom: '2px solid var(--border-color)', textTransform: 'uppercase', fontSize: '0.7rem', color: 'var(--text-muted)' }}>
                  {columns.map((col) => (
                    <th key={col} style={{ padding: '8px 12px', textAlign: 'left' }}>{col}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {filteredData.slice(0, 50).map((row, rIdx) => (
                  <tr key={rIdx} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    {columns.map((col) => (
                      <td key={col} style={{ padding: '8px 12px', fontFamily: col.includes('Id') || col.includes('lat') || col.includes('lng') ? 'var(--font-mono)' : 'inherit' }}>
                        {String(row[col])}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* DDL Schema Sidebar */}
        <div className="card">
          <div className="card-title">
            <span><FileText size={16} style={{ marginRight: '6px', color: 'var(--accent-gold)', verticalAlign: 'middle' }} /> Table Schema (DDL)</span>
          </div>

          <pre style={{ background: '#0f172a', color: '#a7f3d0', padding: '12px', borderRadius: '8px', fontSize: '0.75rem', fontFamily: 'var(--font-mono)', overflowX: 'auto', lineHeight: '1.5' }}>
            {schemas[selectedTable] || '-- Schema not available'}
          </pre>

          <div style={{ marginTop: '1rem', background: '#f1f5f9', padding: '10px', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
            <div style={{ fontSize: '0.75rem', fontWeight: 'bold', color: 'var(--text-main)', marginBottom: '4px' }}>⚡ SQLite Connection Info:</div>
            <div style={{ fontSize: '0.725rem', color: 'var(--text-muted)' }}>
              Database File: <b>`campus_dispatch.db`</b><br />
              JDBC Driver: <b>org.sqlite.JDBC</b><br />
              Location: <b>/Users/Apple/Desktop/DSA final project</b>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
