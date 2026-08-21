# Manual Human Data Collection Guide (AI-Resistant Methodology)

> **University of Ghana (Legon) Campus Dispatch & Optimization System**  
> *Fieldwork Protocol for Human Data Collection & Dataset Construction*

This document provides a step-by-step methodology for human team members to manually survey, measure, and record the campus transportation network data across the University of Ghana (Legon) campus. This protocol satisfies the **AI-Resistance & Dataset Verification Requirements** outlined in Section 2 and Section 15 of the project brief.

---

## 1. Fieldwork Overview & Equipment Required

### Purpose
To collect real-world spatial, topological, and operational data for:
- 50 Campus Location Nodes (`data/locations.csv`)
- 100 Connecting Road Segments (`data/roads.csv`)
- 30 Official Campus Taxis & Emergency Vans (`data/resources.csv`)
- 300 Representative Service Requests (`data/service_requests.csv`)

### Required Tools
1. **Smartphone with GPS App:** Google Maps, OsmAnd, Strava, or GPS Logger (accuracy within 3–5 meters)
2. **Field Notebook & Pen:** For recording node IDs, landmark names, and road observations
3. **Measuring Instrument:** Vehicle trip odometer or walking wheel (optional, GPS track distances are acceptable)
4. **Camera:** For photo verification of location landmarks and taxi ranks

---

## 2. Step-by-Step Data Collection Protocol

### Phase 1: Location Node Survey (`locations.csv`)

**Objective:** Map out 50 distinct locations covering academic, residential, commercial, administrative, sports, and medical zones across Legon campus.

#### Protocol:
1. **Divide Campus into 5 Survey Sectors:**
   - **Sector A (North/Residential):** Pentagon Hostel, JSB, Hilla Limann, Diaspora Halls, TF, Bani.
   - **Sector B (Central/Traditional Halls):** Commonwealth Hall, Legon Hall, Akuafo Hall, Mensah Sarbah, Volta Hall.
   - **Sector C (Central/Academic):** Balme Library, UGCS, Science Block, Law Faculty, UGBS, Chemistry, Physics, Math.
   - **Sector D (East/Sports & Services):** Great Hall, Athletic Oval, UG Stadium, Bush Canteen, Main Admin, ISSER.
   - **Sector E (South/Entrance & Health):** UG Hospital, Night Market, Engineering Block, Noguchi Memorial, UG Fire Station, UG Main Gate, Okponglo.

2. **Collect Node Metadata at Each Landmark:**
   - Stand directly at the main entrance of the building/landmark.
   - Open your GPS app and record the **Latitude** and **Longitude** (e.g., UG Hospital: `5.651, -0.187`).
   - Assign a sequential `locationId` (1 to 50).
   - Classify into one of 8 functional zones: `Medical`, `Residential`, `Academic`, `Commercial`, `Administration`, `Sports`, `Emergency`, `Access`.

#### Data Format Standard:
```csv
locationId,name,zone,latitude,longitude
1,UG Hospital,Medical,5.651,-0.187
2,Night Market,Commercial,5.648,-0.191
3,Commonwealth Hall,Residential,5.652,-0.188
```

---

### Phase 2: Road Edge & Distance Mapping (`roads.csv`)

**Objective:** Identify 100 direct road segments connecting adjacent locations and measure physical distances.

#### Protocol:
1. **Identify Direct Connections:** Walk or drive along the primary roads connecting adjacent nodes (e.g., from Pentagon Hostel to Legon Hall).
2. **Measure Distance:**
   - Record distance in meters using GPS track distance or vehicle odometer (e.g., 200m between Pentagon and Legon Hall).
3. **Determine Traffic Congestion Multipliers (`congestionFactor`):**
   - Observe road width, speed bumps, and peak hour delays (7:30 AM – 9:00 AM & 4:30 PM – 6:30 PM).
   - Assign congestion multiplier:
     - `1.0`: Normal free-flowing road (e.g., road near Botany Gardens)
     - `1.2 – 1.3`: Moderate traffic / speed bump delay (e.g., road near Night Market)
     - `1.5 – 1.8`: Heavy congestion / major bottleneck (e.g., UG Main Gate to Okponglo)
4. **Check Directionality:** Record `isBidirectional` as `true` (1) for two-way roads or `false` (0) for one-way loops.

#### Data Format Standard:
```csv
roadId,sourceLocationId,destLocationId,distanceMeters,congestionFactor,isBidirectional
1,1,2,500.0,1.0,true
2,2,3,300.0,1.2,true
4,4,5,200.0,1.5,true
```

---

### Phase 3: Vehicle & Driver Registration (`resources.csv`)

**Objective:** Catalog 30 verified campus taxis and emergency response vehicles.

#### Protocol:
1. **Visit Official Campus Taxi Ranks:**
   - Rank 1: UG Main Gate Taxi Rank
   - Rank 2: Night Market Rank
   - Rank 3: Pentagon Rank
   - Rank 4: UG Hospital Emergency Bay
2. **Log Taxi Details:**
   - `resourceId`: Assign ID 1 to 30.
   - `driverName`: Driver name (with permission/consent, or anonymized Ghanaian name).
   - `vehiclePlate`: Registration plate format (e.g., `GR-1234-21`).
   - `type`: `CAMPUS_TAXI` or `EMERGENCY_VAN`.
   - `homeLocationId`: ID of their primary station node.
   - `capacity`: Passenger capacity (typically 4).
   - `availabilityStatus`: Initial status (`AVAILABLE`, `DISPATCHED`, or `OFF_DUTY`).

#### Data Format Standard:
```csv
resourceId,driverName,vehiclePlate,type,homeLocationId,capacity,availabilityStatus
1,Kofi Mensah,GR-1234-21,CAMPUS_TAXI,3,4,AVAILABLE
2,Kwame Asante,GW-5678-22,EMERGENCY_VAN,1,6,AVAILABLE
```

---

### Phase 4: Service Request Logging (`service_requests.csv`)

**Objective:** Synthesize 300 realistic service requests based on actual campus transport demand patterns.

#### Demand Distribution Breakdown:
- **Tier 1A (Emergency / Medical):** ~10% (Pickup: Any Hall ➔ Destination: UG Hospital)
- **Tier 1B (Disabled Students):** ~15% (Pickup: Hostels ➔ Destination: Academic Blocks)
- **Tier 2 (Standard UG Students):** ~60% (Pickup: Pentagon/Night Market ➔ Destination: Lecture Halls)
- **Tier 3 (Staff / External Visitors):** ~15% (Pickup: Main Admin/Gate ➔ Destination: ISSER/Departments)

#### Priority Scoring Verification:
Ensure each request satisfies the priority scoring formula upon ingestion:
$$\text{Priority Score } P(R_i) = (W_{\text{cat}} \times \text{Base}) + (W_{\text{urg}} \times \text{Medical}) + (\text{WaitTime} \times 15) + \text{HospitalBonus}$$

---

## 3. Data Integrity & Validation Checklist

Before importing your collected data into SQLite, verify:

- [ ] All 50 location IDs are unique and sequential from 1 to 50.
- [ ] No road references a non-existent `sourceLocationId` or `destLocationId`.
- [ ] GPS coordinates fall strictly within the Legon campus bounding box ($\text{Lat: } 5.635 \text{ to } 5.660, \text{ Lng: } -0.200 \text{ to } -0.170$).
- [ ] Every road segment is reachable (the graph is strongly connected with no isolated orphan nodes).
- [ ] CSV files contain no trailing commas or missing headers.

---

## 4. Academic Honesty Statement Template

Include this statement in your final report submission:

> *"We certify that the dataset stored in `data/locations.csv` and `data/roads.csv` was manually collected and verified by our group members through spatial mapping of the University of Ghana (Legon) campus layout. All GPS coordinates, road lengths, and taxi details reflect real local campus geography."*
