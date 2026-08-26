import React, { useState } from 'react';
import { CAMPUS_LOCATIONS, RAW_ROADS, INITIAL_DRIVERS, REALISTIC_REQUESTERS } from '../data/campusData';
import { Database, Table, Search, Download, FileText } from 'lucide-react';

export default function DatabaseBrowser() {
  const [selectedTable, setSelectedTable] = useState('users');
  const [searchQuery, setSearchQuery] = useState('');

  // Sample seed datasets mirroring SQLite DB tables
  const sampleUsers = [
    { userId: 1, fullName: "Kofi Mensah", userType: "STUDENT", email: "kofi.mensah@st.ug.edu.gh", phone: "+233 24 100 2001", homeLocationId: 4, hasDisability: 0 },
    { userId: 2, fullName: "Ama Asante", userType: "STUDENT", email: "ama.asante@st.ug.edu.gh", phone: "+233 20 100 2002", homeLocationId: 8, hasDisability: 1 },
    { userId: 3, fullName: "Kwame Appiah", userType: "STUDENT", email: "kwame.appiah@st.ug.edu.gh", phone: "+233 27 100 2003", homeLocationId: 5, hasDisability: 0 },
    { userId: 4, fullName: "Dr. Abena Osei", userType: "FACULTY", email: "abena.osei@ug.edu.gh", phone: "+233 54 100 2004", homeLocationId: 3, hasDisability: 0 },
    { userId: 5, fullName: "Prof. Ernest Aryeetey", userType: "FACULTY", email: "e.aryeetey@ug.edu.gh", phone: "+233 26 100 2005", homeLocationId: 6, hasDisability: 0 },
    { userId: 6, fullName: "Esi Boateng", userType: "STAFF", email: "esi.boateng@ug.edu.gh", phone: "+233 24 100 2006", homeLocationId: 7, hasDisability: 0 },
    { userId: 13, fullName: "Chief Inspector John Mensah", userType: "GUEST", email: "j.mensah@police.gov.gh", phone: "+233 24 888 9911", homeLocationId: 16, hasDisability: 0 },
    { userId: 14, fullName: "Dr. Mary Hopkins", userType: "GUEST", email: "m.hopkins@oxford.ac.uk", phone: "+233 55 222 3344", homeLocationId: 14, hasDisability: 0 },
    { userId: 16, fullName: "Driver Kweku Mensah", userType: "DRIVER", email: "kweku.driver@ugdispatch.gh", phone: "+233 24 123 4567", homeLocationId: 4, hasDisability: 0 },
    { userId: 17, fullName: "Driver Emmanuel Ofori", userType: "DRIVER", email: "emmanuel.driver@ugdispatch.gh", phone: "+233 20 987 6543", homeLocationId: 12, hasDisability: 0 },
  ];

  const sampleStudents = [
    { studentId: 1, userId: 1, indexNumber: "10982341", hallOfResidence: "Pentagon Hostel", department: "Computer Science", academicYear: "Level 300" },
    { studentId: 2, userId: 2, indexNumber: "10982342", hallOfResidence: "Volta Hall", department: "Chemistry", academicYear: "Level 200" },
    { studentId: 3, userId: 3, indexNumber: "10982343", hallOfResidence: "Legon Hall", department: "Law", academicYear: "Level 400" },
    { studentId: 4, userId: 7, indexNumber: "10982347", hallOfResidence: "Diaspora Hall", department: "Political Science", academicYear: "Level 100" },
    { studentId: 5, userId: 8, indexNumber: "10982348", hallOfResidence: "Hilla Limann Hall", department: "UGBS", academicYear: "Level 300" },
  ];

  const sampleGuests = [
    { guestId: 1, userId: 13, passCode: "GST-9081", visitingDepartment: "UG Fire Station & Security", hostPersonName: "UG Chief Security Officer", durationDays: 2 },
    { guestId: 2, userId: 14, passCode: "GST-9082", visitingDepartment: "Institute of African Studies", hostPersonName: "Prof. Ernest Aryeetey", durationDays: 5 },
    { guestId: 3, userId: 15, passCode: "GST-9083", visitingDepartment: "Main Administration", hostPersonName: "Esi Boateng", durationDays: 1 },
  ];

  const sampleDrivers = INITIAL_DRIVERS.map((d, i) => ({
    driverId: i + 1,
    userId: 16 + i,
    fullName: d.name,
    licenseNumber: `DL-2022-${100 + i}`,
    vehiclePlate: `GR-${1000 + i * 111}-22`,
    vehicleType: d.type,
    capacity: d.type === 'SHUTTLE' ? 18 : d.type === 'ACCESSIBLE_VAN' ? 6 : 4,
    homeLocationId: d.locationId,
    availabilityStatus: d.status,
    isWheelchairAccessible: d.isWheelchairAccessible ? 1 : 0,
    contactPhone: d.phone,
    rating: 4.9
  }));

  const sampleRequests = REALISTIC_REQUESTERS.map((r, i) => ({
    requestId: 1001 + i,
    userId: i + 1,
    requesterName: r.name,
    userCategory: r.category,
    pickupLocationId: (i * 4 + 3) % 50 + 1,
    destinationLocationId: r.category === 'EMERGENCY_MEDICAL' ? 1 : ((i * 7 + 12) % 50 + 1),
    status: i % 2 === 0 ? "PENDING" : "DISPATCHED",
    waitTimeMinutes: (i * 6 + 5) % 40 + 5,
    isMedicalUrgency: r.category === 'EMERGENCY_MEDICAL' ? 1 : 0
  }));

  const schemas = {
    users: `CREATE TABLE users (
  userId INTEGER PRIMARY KEY,
  fullName TEXT NOT NULL,
  userType TEXT NOT NULL, -- 'STUDENT', 'GUEST', 'FACULTY', 'STAFF', 'DRIVER'
  email TEXT,
  phone TEXT,
  homeLocationId INTEGER,
  hasDisability INTEGER DEFAULT 0,
  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)
);`,
    students: `CREATE TABLE students (
  studentId INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER NOT NULL UNIQUE,
  indexNumber TEXT UNIQUE NOT NULL,
  hallOfResidence TEXT NOT NULL,
  department TEXT NOT NULL,
  academicYear TEXT DEFAULT 'Level 300',
  FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);`,
    guests: `CREATE TABLE guests (
  guestId INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER NOT NULL UNIQUE,
  passCode TEXT UNIQUE NOT NULL,
  visitingDepartment TEXT NOT NULL,
  hostPersonName TEXT,
  durationDays INTEGER DEFAULT 1,
  FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);`,
    drivers: `CREATE TABLE drivers (
  driverId INTEGER PRIMARY KEY AUTOINCREMENT,
  userId INTEGER UNIQUE,
  fullName TEXT NOT NULL,
  licenseNumber TEXT UNIQUE NOT NULL,
  vehiclePlate TEXT NOT NULL,
  vehicleType TEXT NOT NULL,
  capacity INTEGER NOT NULL,
  homeLocationId INTEGER,
  availabilityStatus TEXT NOT NULL,
  isWheelchairAccessible INTEGER DEFAULT 0,
  contactPhone TEXT,
  rating REAL DEFAULT 4.9,
  FOREIGN KEY(userId) REFERENCES users(userId),
  FOREIGN KEY(homeLocationId) REFERENCES locations(locationId)
);`,
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
  userId INTEGER,
  requesterName TEXT,
  userCategory TEXT,
  pickupLocationId INTEGER,
  destinationLocationId INTEGER,
  status TEXT,
  waitTimeMinutes REAL,
  isMedicalUrgency INTEGER,
  FOREIGN KEY(userId) REFERENCES users(userId)
);`
  };

  // Get current table data
  let data = [];
  if (selectedTable === 'users') data = sampleUsers;
  else if (selectedTable === 'students') data = sampleStudents;
  else if (selectedTable === 'guests') data = sampleGuests;
  else if (selectedTable === 'drivers') data = sampleDrivers;
  else if (selectedTable === 'locations') data = CAMPUS_LOCATIONS;
  else if (selectedTable === 'roads') data = RAW_ROADS.map(([id, u, v, dist, cong]) => ({ roadId: id, sourceLocationId: u, destLocationId: v, distanceMeters: dist, congestionFactor: cong, isBidirectional: 1 }));
  else if (selectedTable === 'service_requests') data = sampleRequests;

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
              style={{ width: '220px' }}
              value={selectedTable}
              onChange={(e) => setSelectedTable(e.target.value)}
            >
              <option value="users">users (Parent Entity Table)</option>
              <option value="students">students (Subtype Extension)</option>
              <option value="guests">guests (Subtype Extension)</option>
              <option value="drivers">drivers (Subtype Extension)</option>
              <option value="locations">locations (50 rows)</option>
              <option value="roads">roads (100 rows)</option>
              <option value="service_requests">service_requests (300 rows)</option>
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
            <div style={{ fontSize: '0.75rem', fontWeight: 'bold', color: 'var(--text-main)', marginBottom: '4px' }}>⚡ Relational Schema Architecture:</div>
            <div style={{ fontSize: '0.725rem', color: 'var(--text-muted)', lineHeight: '1.5' }}>
              Parent: <b>`users`</b> (userId PK)<br />
              Subtypes: <b>`students`</b>, <b>`guests`</b>, <b>`drivers`</b> (FK ➔ `users.userId`)<br />
              Database: <b>`campus_dispatch.db`</b>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
