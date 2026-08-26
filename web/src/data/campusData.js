// --- UNIVERSITY OF GHANA (LEGON) CAMPUS DATA & ALGORITHM UTILITIES ---

export const CAMPUS_LOCATIONS = [
  { id: 1, name: "UG Hospital", zone: "Medical", lat: 5.6416, lng: -0.1824, x: 500, y: 400, isHospital: true },
  { id: 2, name: "Night Market", zone: "Commercial", lat: 5.6534, lng: -0.1916, x: 300, y: 150 },
  { id: 3, name: "Commonwealth Hall", zone: "Residential", lat: 5.6558, lng: -0.1866, x: 440, y: 100 },
  { id: 4, name: "Pentagon Hostel", zone: "Residential", lat: 5.6455, lng: -0.1925, x: 220, y: 320 },
  { id: 5, name: "Legon Hall", zone: "Residential", lat: 5.6530, lng: -0.1878, x: 380, y: 160 },
  { id: 6, name: "Akuafo Hall", zone: "Residential", lat: 5.6521, lng: -0.1873, x: 400, y: 180 },
  { id: 7, name: "Mensah Sarbah Hall", zone: "Residential", lat: 5.6515, lng: -0.1892, x: 340, y: 200 },
  { id: 8, name: "Volta Hall", zone: "Residential", lat: 5.6542, lng: -0.1870, x: 420, y: 130 },
  { id: 9, name: "JSB (Jean Nelson Aka)", zone: "Residential", lat: 5.6461, lng: -0.1905, x: 260, y: 300 },
  { id: 10, name: "Hilla Limann Hall", zone: "Residential", lat: 5.6442, lng: -0.1918, x: 240, y: 350 },
  { id: 11, name: "Alexander Kwapong Hall", zone: "Residential", lat: 5.6448, lng: -0.1910, x: 250, y: 330 },
  { id: 12, name: "Balme Library", zone: "Academic", lat: 5.6508, lng: -0.1852, x: 450, y: 220 },
  { id: 13, name: "UGCS (Computing Centre)", zone: "Academic", lat: 5.6512, lng: -0.1845, x: 470, y: 210 },
  { id: 14, name: "Great Hall", zone: "Academic", lat: 5.6582, lng: -0.1867, x: 440, y: 60 },
  { id: 15, name: "Athletic Oval", zone: "Sports", lat: 5.6548, lng: -0.1802, x: 580, y: 120 },
  { id: 16, name: "UG Main Gate", zone: "Access", lat: 5.6402, lng: -0.1795, x: 600, y: 440 },
  { id: 17, name: "Okponglo Gate", zone: "Access", lat: 5.6425, lng: -0.1768, x: 650, y: 380 },
  { id: 18, name: "UG Stadium", zone: "Sports", lat: 5.6560, lng: -0.1810, x: 570, y: 100 },
  { id: 19, name: "Bush Canteen", zone: "Commercial", lat: 5.6502, lng: -0.1898, x: 320, y: 230 },
  { id: 20, name: "Engineering Block", zone: "Academic", lat: 5.6535, lng: -0.1820, x: 530, y: 150 },
  { id: 21, name: "UGBS (Business School)", zone: "Academic", lat: 5.6519, lng: -0.1832, x: 500, y: 190 },
  { id: 22, name: "Law Faculty", zone: "Academic", lat: 5.6501, lng: -0.1829, x: 510, y: 240 },
  { id: 23, name: "Central Cafeteria", zone: "Commercial", lat: 5.6495, lng: -0.1865, x: 410, y: 250 },
  { id: 24, name: "Science Block", zone: "Academic", lat: 5.6510, lng: -0.1838, x: 480, y: 220 },
  { id: 25, name: "Botany Gardens", zone: "Recreation", lat: 5.6565, lng: -0.1840, x: 500, y: 90 },
  { id: 26, name: "Chemistry Dept", zone: "Academic", lat: 5.6505, lng: -0.1842, x: 470, y: 230 },
  { id: 27, name: "Physics Dept", zone: "Academic", lat: 5.6508, lng: -0.1839, x: 480, y: 225 },
  { id: 28, name: "Math Dept", zone: "Academic", lat: 5.6515, lng: -0.1840, x: 480, y: 205 },
  { id: 29, name: "Economics Dept", zone: "Academic", lat: 5.6525, lng: -0.1850, x: 450, y: 180 },
  { id: 30, name: "Political Science", zone: "Academic", lat: 5.6528, lng: -0.1854, x: 440, y: 170 },
  { id: 31, name: "N Block", zone: "Academic", lat: 5.6502, lng: -0.1848, x: 460, y: 240 },
  { id: 32, name: "Diaspora Hall", zone: "Residential", lat: 5.6438, lng: -0.1932, x: 200, y: 360 },
  { id: 33, name: "TF Hostel", zone: "Residential", lat: 5.6428, lng: -0.1945, x: 180, y: 380 },
  { id: 34, name: "Bani Hostel", zone: "Residential", lat: 5.6420, lng: -0.1950, x: 160, y: 400 },
  { id: 35, name: "African Union Hall", zone: "Residential", lat: 5.6445, lng: -0.1920, x: 230, y: 340 },
  { id: 36, name: "International Students Hall", zone: "Residential", lat: 5.6450, lng: -0.1935, x: 200, y: 330 },
  { id: 37, name: "ISSER", zone: "Academic", lat: 5.6540, lng: -0.1855, x: 450, y: 140 },
  { id: 38, name: "Institute of African Studies", zone: "Academic", lat: 5.6545, lng: -0.1860, x: 440, y: 130 },
  { id: 39, name: "Noguchi Memorial", zone: "Medical", lat: 5.6435, lng: -0.1835, x: 480, y: 360 },
  { id: 40, name: "UG Fire Station", zone: "Emergency", lat: 5.6430, lng: -0.1818, x: 520, y: 370 },
  { id: 41, name: "CSD (Career Services)", zone: "Administration", lat: 5.6500, lng: -0.1855, x: 440, y: 240 },
  { id: 42, name: "Banking Square", zone: "Commercial", lat: 5.6488, lng: -0.1848, x: 460, y: 270 },
  { id: 43, name: "University Bookshop", zone: "Commercial", lat: 5.6506, lng: -0.1858, x: 430, y: 220 },
  { id: 44, name: "Graduate School", zone: "Academic", lat: 5.6552, lng: -0.1858, x: 450, y: 110 },
  { id: 45, name: "SSNIT Hostel", zone: "Residential", lat: 5.6415, lng: -0.1960, x: 140, y: 410 },
  { id: 46, name: "Valco Trust Hostel", zone: "Residential", lat: 5.6475, lng: -0.1905, x: 280, y: 270 },
  { id: 47, name: "Athletic Hostel", zone: "Residential", lat: 5.6568, lng: -0.1805, x: 580, y: 80 },
  { id: 48, name: "UG Credit Union", zone: "Commercial", lat: 5.6492, lng: -0.1852, x: 450, y: 260 },
  { id: 49, name: "UG Post Office", zone: "Administrative", lat: 5.6498, lng: -0.1856, x: 440, y: 250 },
  { id: 50, name: "Main Administration", zone: "Administrative", lat: 5.6575, lng: -0.1860, x: 450, y: 70 }
];

export const RAW_ROADS = [
  [1, 1, 2, 1817.2, 1.0],
  [2, 2, 7, 475.6, 1.2],
  [3, 8, 3, 292.1, 1.0],
  [4, 4, 5, 1297.8, 1.2],
  [5, 5, 6, 366.0, 1.0],
  [6, 6, 8, 599.7, 1.0],
  [7, 6, 7, 664.2, 1.0],
  [8, 3, 14, 518.6, 1.0],
  [9, 9, 10, 2673.4, 1.0],
  [10, 10, 11, 2440.9, 1.0],
  [11, 11, 12, 1962.4, 1.4],
  [12, 12, 13, 171.9, 1.0],
  [13, 13, 14, 1342.9, 1.2],
  [14, 14, 15, 1128.7, 1.0],
  [15, 15, 16, 1852.1, 1.6],
  [16, 16, 17, 2816.8, 1.8],
  [17, 17, 18, 406.6, 1.5],
  [18, 18, 19, 1410.2, 1.1],
  [19, 19, 20, 933.3, 1.0],
  [20, 20, 21, 1007.1, 1.2],
  [21, 21, 22, 1000.0, 1.0],
  [22, 22, 23, 1603.2, 1.3],
  [23, 23, 24, 1682.4, 1.1],
  [24, 24, 25, 728.7, 1.0],
  [25, 25, 26, 492.2, 1.2],
  [26, 26, 27, 159.5, 1.0],
  [27, 27, 28, 304.0, 1.1],
  [28, 28, 29, 555.8, 1.0],
  [29, 29, 30, 523.1, 1.3],
  [30, 30, 31, 251.1, 1.0],
  [31, 31, 32, 2158.3, 1.4],
  [32, 32, 33, 3441.4, 1.0],
  [33, 33, 34, 978.4, 1.0],
  [34, 34, 35, 682.4, 1.2],
  [35, 35, 36, 2883.3, 1.0],
  [36, 36, 37, 1842.6, 1.5],
  [37, 37, 38, 5358.1, 1.0],
  [38, 38, 39, 8055.0, 1.3],
  [39, 39, 40, 1942.2, 1.0],
  [40, 40, 41, 1745.5, 1.1],
  [41, 41, 42, 1542.4, 1.0],
  [42, 42, 43, 1338.6, 1.2],
  [43, 43, 44, 2069.7, 1.0],
  [44, 44, 45, 2439.9, 1.5],
  [45, 45, 46, 1401.0, 1.1],
  [46, 46, 47, 1437.7, 1.4],
  [47, 47, 48, 2457.2, 1.0],
  [48, 48, 49, 2228.4, 1.2],
  [49, 49, 50, 1843.5, 1.0],
  [50, 50, 1, 1613.9, 1.3],
  [51, 1, 3, 2014.0, 1.1],
  [52, 2, 4, 2073.6, 1.0],
  [53, 3, 5, 481.6, 1.2],
  [54, 4, 6, 1007.8, 1.0],
  [55, 5, 7, 687.3, 1.1],
  [56, 6, 8, 599.7, 1.0],
  [57, 7, 9, 1388.3, 1.3],
  [58, 8, 10, 592.1, 1.0],
  [59, 9, 11, 556.8, 1.0],
  [60, 10, 12, 483.1, 1.4],
  [61, 11, 13, 1901.6, 1.0],
  [62, 12, 14, 1178.8, 1.2],
  [63, 13, 15, 498.1, 1.0],
  [64, 14, 16, 1287.5, 1.5],
  [65, 15, 17, 1547.3, 1.7],
  [66, 16, 18, 2558.1, 1.4],
  [67, 17, 19, 1250.5, 1.1],
  [68, 18, 20, 2300.0, 1.0],
  [69, 19, 21, 956.2, 1.2],
  [70, 20, 22, 407.2, 1.0],
  [71, 21, 23, 825.3, 1.3],
  [72, 22, 24, 335.5, 1.1],
  [73, 23, 25, 1073.7, 1.0],
  [74, 24, 26, 875.2, 1.2],
  [75, 25, 27, 440.8, 1.0],
  [76, 26, 28, 169.7, 1.1],
  [77, 27, 29, 402.3, 1.0],
  [78, 28, 30, 256.1, 1.3],
  [79, 29, 31, 666.0, 1.0],
  [80, 30, 32, 1938.9, 1.4],
  [81, 31, 33, 1599.4, 1.0],
  [82, 32, 34, 3132.9, 1.0],
  [83, 33, 35, 599.1, 1.2],
  [84, 34, 36, 3156.9, 1.0],
  [85, 35, 37, 1080.5, 1.5],
  [86, 36, 38, 7198.5, 1.0],
  [87, 37, 39, 2701.2, 1.3],
  [88, 38, 40, 6129.1, 1.0],
  [89, 39, 41, 2555.1, 1.1],
  [90, 40, 42, 1031.6, 1.0],
  [91, 41, 43, 547.9, 1.2],
  [92, 42, 44, 2411.8, 1.0],
  [93, 43, 45, 448.2, 1.5],
  [94, 44, 46, 2296.6, 1.1],
  [95, 45, 47, 1339.8, 1.4],
  [96, 46, 48, 1469.4, 1.0],
  [97, 47, 49, 722.2, 1.2],
  [98, 48, 50, 3521.0, 1.0],
  [99, 49, 1, 1487.1, 1.3],
  [100, 50, 2, 2888.9, 1.1]
];

// --- SAMPLE DRIVERS / DISPATCH VEHICLES QUEUE ---
export const INITIAL_DRIVERS = [
  { id: "D-101", name: "Driver Kweku (Taxi #01)", vehicle: "Toyota Corolla (Sedan)", locationId: 4, type: "TAXI", status: "AVAILABLE", isWheelchairAccessible: false, phone: "+233 24 123 4567" },
  { id: "D-102", name: "Driver Emmanuel (Van #02)", vehicle: "Nissan NV200 (Ramp Van)", locationId: 12, type: "ACCESSIBLE_VAN", status: "AVAILABLE", isWheelchairAccessible: true, phone: "+233 20 987 6543" },
  { id: "D-103", name: "Driver Yaw (Ambulance #01)", vehicle: "Mercedes ICU Ambulance", locationId: 1, type: "AMBULANCE", status: "AVAILABLE", isWheelchairAccessible: true, phone: "+233 27 555 0199" },
  { id: "D-104", name: "Driver Kofi (Taxi #03)", vehicle: "Hyundai Elantra (SUV)", locationId: 2, type: "TAXI", status: "AVAILABLE", isWheelchairAccessible: false, phone: "+233 54 888 1234" },
  { id: "D-105", name: "Driver Samuel (Shuttle #01)", vehicle: "Toyota Coaster (18-Seater)", locationId: 16, type: "SHUTTLE", status: "AVAILABLE", isWheelchairAccessible: false, phone: "+233 26 333 4455" },
  { id: "D-106", name: "Driver Grace (Van #03)", vehicle: "Ford Transit (Wheelchair Lift)", locationId: 8, type: "ACCESSIBLE_VAN", status: "AVAILABLE", isWheelchairAccessible: true, phone: "+233 24 777 9900" },
  { id: "D-107", name: "Driver Dennis (Ambulance #02)", vehicle: "Toyota HiAce Paramedic", locationId: 40, type: "AMBULANCE", status: "AVAILABLE", isWheelchairAccessible: true, phone: "+233 23 111 2233" },
  { id: "D-108", name: "Driver Nana (Taxi #05)", vehicle: "Kia Forte (Sedan)", locationId: 21, type: "TAXI", status: "AVAILABLE", isWheelchairAccessible: false, phone: "+233 50 444 7788" },
];

export const REALISTIC_REQUESTERS = [
  { name: "Dr. Abena Osei", category: "EMERGENCY_MEDICAL", role: "Faculty (Chemistry Dept)", isWheelchair: false, note: "Severe allergic reaction during lab work" },
  { name: "Ama Asante", category: "STUDENT_MOBILITY", role: "Disabled Student (Volta Hall)", isWheelchair: true, note: "Wheelchair ramp mobility assistance to exam venue" },
  { name: "Kwame Appiah", category: "EMERGENCY_MEDICAL", role: "Student (Pentagon Hostel)", isWheelchair: false, note: "Acute abdominal pain / urgent ER dispatch" },
  { name: "Prof. Ernest Aryeetey", category: "STAFF_TRANSPORT", role: "Director (ISSER)", isWheelchair: false, note: "Official transport to Vice-Chancellor office" },
  { name: "Esi Boateng", category: "STAFF_TRANSPORT", role: "Senior Registrar (Main Admin)", isWheelchair: false, note: "Academic board meeting document logistics" },
  { name: "Yaw Addo", category: "STUDENT_MOBILITY", role: "Student (Night Market)", isWheelchair: false, note: "Late night library return shuttle" },
  { name: "Akosua Mensah", category: "STUDENT_MOBILITY", role: "Student (Balme Library)", isWheelchair: true, note: "Requires accessible van with low floor ramp" },
  { name: "Kweku Baah", category: "EVENT_LOGISTICS", role: "SRC Executive (Legon Hall)", isWheelchair: false, note: "Equipment transport for Ghana Games at Stadium" },
  { name: "Adwoa Serwaa", category: "STAFF_TRANSPORT", role: "Lecturer (UGBS)", isWheelchair: false, note: "Inter-faculty lecture series pickup" },
  { name: "Fiifi Annan", category: "EMERGENCY_MEDICAL", role: "Athlete (Athletic Oval)", isWheelchair: false, note: "Heat stroke during track practice" },
  { name: "Nii Lartey", category: "STUDENT_MOBILITY", role: "Student (Commonwealth Hall)", isWheelchair: false, note: "Group ride to Engineering lab" },
  { name: "Efua Sutherland", category: "STAFF_TRANSPORT", role: "Dean of Arts (Great Hall)", isWheelchair: false, note: "Distinguished lecture speaker escort" },
];

export function calculateHaversineDistance(lat1, lon1, lat2, lon2) {
  const R = 6371000;
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return Math.round(R * c);
}

export function calculatePriorityScore(category, urgencyLevel, waitTimeMinutes, destLocationId, isWheelchair = false) {
  let catWeight = 10;
  let baseWeight = 20;
  if (category === 'EMERGENCY_MEDICAL') { catWeight = 50; baseWeight = 100; }
  else if (category === 'STUDENT_MOBILITY') { catWeight = 30; baseWeight = 60; }
  else if (category === 'STAFF_TRANSPORT') { catWeight = 20; baseWeight = 40; }
  else if (category === 'EVENT_LOGISTICS') { catWeight = 15; baseWeight = 30; }

  const urgencyVal = urgencyLevel === 'CRITICAL' ? 300 : (urgencyLevel === 'HIGH' ? 200 : (urgencyLevel === 'MEDIUM' ? 100 : 20));
  const hospitalBonus = (destLocationId === 1 || destLocationId === 39) ? 250 : 0;
  const disabilityBonus = isWheelchair ? 150 : 0;
  const waitBonus = waitTimeMinutes * 15;

  return (catWeight * baseWeight) + urgencyVal + waitBonus + hospitalBonus + disabilityBonus;
}

export function generateInitialRequests() {
  const categories = ['EMERGENCY_MEDICAL', 'STUDENT_MOBILITY', 'STAFF_TRANSPORT', 'EVENT_LOGISTICS'];
  const urgencies = ['CRITICAL', 'HIGH', 'MEDIUM', 'LOW'];
  const requests = [];

  for (let i = 0; i < REALISTIC_REQUESTERS.length; i++) {
    const item = REALISTIC_REQUESTERS[i];
    const cat = item.category;
    const urg = cat === 'EMERGENCY_MEDICAL' ? (i % 2 === 0 ? 'CRITICAL' : 'HIGH') : (i % 3 === 0 ? 'HIGH' : 'MEDIUM');
    const pLoc = (i * 4 + 3) % 50 + 1;
    let dLoc = cat === 'EMERGENCY_MEDICAL' ? 1 : ((i * 7 + 12) % 50 + 1);
    if (dLoc === pLoc) dLoc = (pLoc % 50) + 1;
    const waitTime = (i * 6 + 5) % 40 + 5;
    const score = calculatePriorityScore(cat, urg, waitTime, dLoc, item.isWheelchair);

    requests.push({
      id: 1000 + i + 1,
      requesterName: item.name,
      role: item.role,
      category: cat,
      urgency: urg,
      pickupLocation: pLoc,
      destLocation: dLoc,
      waitTime: waitTime,
      hasDisability: item.isWheelchair,
      note: item.note,
      priorityScore: score,
      status: 'PENDING'
    });
  }
  return requests.sort((a, b) => b.priorityScore - a.priorityScore);
}

// --- LIVE JAVA BACKEND API INTEGRATION ---
const BACKEND_URL = 'http://localhost:8080';

export async function fetchBackendStatus() {
  try {
    const res = await fetch(`${BACKEND_URL}/api/status`);
    if (res.ok) return await res.json();
  } catch (e) {
    return null;
  }
  return null;
}

export async function fetchBackendLocations() {
  try {
    const res = await fetch(`${BACKEND_URL}/api/locations`);
    if (res.ok) {
      const data = await res.json();
      if (data && data.length > 0) return data;
    }
  } catch (e) {
    // Fallback to local
  }
  return CAMPUS_LOCATIONS;
}

export async function fetchBackendRoads() {
  try {
    const res = await fetch(`${BACKEND_URL}/api/roads`);
    if (res.ok) {
      const data = await res.json();
      if (data && data.length > 0) return data;
    }
  } catch (e) {
    // Fallback to local
  }
  return RAW_ROADS;
}

export async function fetchBackendRequests() {
  try {
    const res = await fetch(`${BACKEND_URL}/api/requests`);
    if (res.ok) {
      const data = await res.json();
      if (data && data.length > 0) return data;
    }
  } catch (e) {
    // Fallback to local
  }
  return null;
}
