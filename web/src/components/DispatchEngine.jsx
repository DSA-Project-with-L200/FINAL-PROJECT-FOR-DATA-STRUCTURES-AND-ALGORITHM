import React, { useState, useEffect } from 'react';
import {
  generateInitialRequests,
  calculatePriorityScore,
  CAMPUS_LOCATIONS,
  RAW_ROADS,
  INITIAL_DRIVERS,
  REALISTIC_REQUESTERS
} from '../data/campusData';
import {
  Zap,
  ShieldAlert,
  ArrowUpRight,
  RotateCcw,
  PlusCircle,
  Car,
  CheckCircle2,
  Navigation,
  Award,
  Sparkles,
  Clock,
  ArrowRight,
  Layers,
  X,
  Play,
  Pause
} from 'lucide-react';

// Shortest path finder using Dijkstra's Algorithm on 50 campus graph nodes & 100 weighted roads
function computeDijkstraPath(sourceId, destId) {
  if (sourceId === destId) return { distance: 0, pathNodeCount: 1, pathNames: [CAMPUS_LOCATIONS.find(l => l.id === sourceId)?.name || 'Same'] };

  const adj = {};
  CAMPUS_LOCATIONS.forEach(l => (adj[l.id] = []));
  RAW_ROADS.forEach(([id, u, v, weight]) => {
    adj[u].push({ node: v, weight });
    adj[v].push({ node: u, weight });
  });

  const dist = {};
  const prev = {};
  const visited = new Set();
  CAMPUS_LOCATIONS.forEach(l => (dist[l.id] = Infinity));
  dist[sourceId] = 0;

  while (visited.size < CAMPUS_LOCATIONS.length) {
    let u = null;
    let minD = Infinity;
    CAMPUS_LOCATIONS.forEach(l => {
      if (!visited.has(l.id) && dist[l.id] < minD) {
        minD = dist[l.id];
        u = l.id;
      }
    });

    if (u === null || dist[u] === Infinity) break;
    visited.add(u);
    if (u === destId) break;

    adj[u].forEach(edge => {
      if (!visited.has(edge.node)) {
        let alt = dist[u] + edge.weight;
        if (alt < dist[edge.node]) {
          dist[edge.node] = alt;
          prev[edge.node] = u;
        }
      }
    });
  }

  const path = [];
  let curr = destId;
  while (curr !== undefined) {
    path.unshift(curr);
    curr = prev[curr];
  }

  const totalDist = dist[destId] === Infinity ? 450 : Math.round(dist[destId]);
  const nodeNames = path.map(id => CAMPUS_LOCATIONS.find(l => l.id === id)?.name || `#${id}`);

  return {
    distance: totalDist,
    pathNodeCount: path.length,
    pathNames: nodeNames
  };
}


// Driver Live Car Movement Simulation Modal (Leaflet + Animated Vehicle Pin)
function DriverNavigationModal({ dispatch, onClose, onLog }) {
  const mapContainerRef = React.useRef(null);
  const mapInstanceRef = React.useRef(null);
  const driverMarkerRef = React.useRef(null);
  const [isPlaying, setIsPlaying] = React.useState(true);
  const [animProgress, setAnimProgress] = React.useState(0);
  const [simSpeed, setSimSpeed] = React.useState(1);

  const routeLatLngs = React.useMemo(() => {
    if (!dispatch || !dispatch.route || !dispatch.route.pathNodeIds) return [];
    return dispatch.route.pathNodeIds.map(id => {
      const loc = CAMPUS_LOCATIONS.find(l => l.id === id);
      return loc ? [loc.lat, loc.lng] : null;
    }).filter(Boolean);
  }, [dispatch]);

  React.useEffect(() => {
    if (!mapContainerRef.current || routeLatLngs.length === 0) return;

    const map = L.map(mapContainerRef.current, {
      center: routeLatLngs[0],
      zoom: 16,
      zoomControl: true,
    });
    mapInstanceRef.current = map;

    L.tileLayer('https://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
      maxZoom: 20,
      attribution: '© Google Maps',
    }).addTo(map);

    L.polyline(routeLatLngs, {
      color: '#10b981',
      weight: 6,
      opacity: 0.95,
      lineCap: 'round'
    }).addTo(map);

    const srcLoc = routeLatLngs[0];
    const destLoc = routeLatLngs[routeLatLngs.length - 1];

    const srcIcon = L.divIcon({
      className: 'pickup-marker',
      html: `<div style="background:#10b981; color:#fff; padding:4px 8px; border-radius:12px; font-weight:800; font-size:11px; border:2px solid #fff; box-shadow:0 0 10px #10b981;">📍 Pickup: ${dispatch.pickupName}</div>`,
      iconSize: [140, 24],
      iconAnchor: [70, 12]
    });
    L.marker(srcLoc, { icon: srcIcon }).addTo(map);

    const destIcon = L.divIcon({
      className: 'dest-marker',
      html: `<div style="background:#f43f5e; color:#fff; padding:4px 8px; border-radius:12px; font-weight:800; font-size:11px; border:2px solid #fff; box-shadow:0 0 10px #f43f5e;">🏥 Dest: ${dispatch.destName}</div>`,
      iconSize: [140, 24],
      iconAnchor: [70, 12]
    });
    L.marker(destLoc, { icon: destIcon }).addTo(map);

    const carIcon = L.divIcon({
      className: 'moving-car-icon',
      html: `<div style="
        background: #f59e0b;
        color: #ffffff;
        width: 36px;
        height: 36px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        border: 3px solid #ffffff;
        box-shadow: 0 0 16px #f59e0b, 0 4px 12px rgba(0,0,0,0.4);
        transition: all 0.1s linear;
      ">🚕</div>`,
      iconSize: [36, 36],
      iconAnchor: [18, 18]
    });

    const driverMarker = L.marker(srcLoc, { icon: carIcon }).addTo(map);
    driverMarker.bindTooltip(`<b>🚕 Driver ${dispatch.driver.name}</b><br>Vehicle: ${dispatch.driver.vehicle}`, { permanent: false, direction: 'top' });
    driverMarkerRef.current = driverMarker;

    setTimeout(() => map.invalidateSize(), 200);

    return () => {
      if (mapInstanceRef.current) {
        mapInstanceRef.current.remove();
        mapInstanceRef.current = null;
      }
    };
  }, [dispatch, routeLatLngs]);

  React.useEffect(() => {
    let interval = null;
    if (isPlaying) {
      interval = setInterval(() => {
        setAnimProgress(prev => {
          if (prev >= 100) {
            setIsPlaying(false);
            if (onLog) onLog('RIDE_ARRIVED', `🏁 Vehicle ${dispatch.driver.vehicle} arrived at ${dispatch.destName}!`);
            confetti({ particleCount: 70, spread: 60, origin: { y: 0.6 } });
            return 100;
          }
          return prev + (1.5 * simSpeed);
        });
      }, 150);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isPlaying, simSpeed, dispatch]);

  React.useEffect(() => {
    if (!driverMarkerRef.current || routeLatLngs.length === 0) return;

    const totalSegments = routeLatLngs.length - 1;
    if (totalSegments <= 0) return;

    const currentSegmentIndex = Math.min(
      Math.floor((animProgress / 100) * totalSegments),
      totalSegments - 1
    );

    const segmentProgress = ((animProgress / 100) * totalSegments) - currentSegmentIndex;

    const startPt = routeLatLngs[currentSegmentIndex];
    const endPt = routeLatLngs[currentSegmentIndex + 1];

    const currentLat = startPt[0] + (endPt[0] - startPt[0]) * segmentProgress;
    const currentLng = startPt[1] + (endPt[1] - startPt[1]) * segmentProgress;

    driverMarkerRef.current.setLatLng([currentLat, currentLng]);

    if (mapInstanceRef.current && isPlaying) {
      mapInstanceRef.current.panTo([currentLat, currentLng], { animate: true });
    }
  }, [animProgress, routeLatLngs, isPlaying]);

  const currentDistRemaining = Math.round(dispatch.route.distance * (1 - animProgress / 100));
  const etaSec = Math.ceil((currentDistRemaining / 10));

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      background: 'rgba(15, 23, 42, 0.8)', backdropFilter: 'blur(8px)',
      zIndex: 2000, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '1.5rem'
    }}>
      <div style={{
        background: '#ffffff', borderRadius: '16px', width: '100%', maxWidth: '850px',
        maxHeight: '90vh', display: 'flex', flexDirection: 'column', overflow: 'hidden',
        boxShadow: '0 20px 50px rgba(0,0,0,0.3)', border: '1px solid var(--border-color)'
      }}>
        <div style={{
          padding: '1rem 1.5rem', background: '#0f172a', color: '#ffffff',
          display: 'flex', justifyContent: 'space-between', alignItems: 'center'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            <span style={{ background: '#f59e0b', color: '#fff', padding: '4px 10px', borderRadius: '8px', fontWeight: 800, fontSize: '0.8rem' }}>
              🚕 LIVE CAR MOVEMENT SIMULATION
            </span>
            <div>
              <h3 style={{ margin: 0, fontSize: '1.05rem', fontWeight: 800 }}>
                {dispatch.request.requesterName} ➔ {dispatch.driver.name}
              </h3>
              <p style={{ margin: 0, fontSize: '0.75rem', color: '#94a3b8' }}>
                Dispatch ID: {dispatch.id} | Vehicle: {dispatch.driver.vehicle}
              </p>
            </div>
          </div>
          <button onClick={onClose} style={{ background: 'transparent', border: 'none', color: '#94a3b8', cursor: 'pointer' }}>
            <X size={24} />
          </button>
        </div>

        <div style={{ height: '380px', width: '100%', position: 'relative' }}>
          <div ref={mapContainerRef} style={{ height: '100%', width: '100%' }} />
        </div>

        <div style={{ padding: '1.25rem 1.5rem', background: '#f8fafc', borderTop: '1px solid #e2e8f0' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem', flexWrap: 'wrap', gap: '0.5rem' }}>
            <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
              <button
                className="btn btn-primary"
                style={{ width: 'auto', padding: '0.5rem 1rem', background: isPlaying ? '#64748b' : '#10b981' }}
                onClick={() => setIsPlaying(!isPlaying)}
              >
                {isPlaying ? <Pause size={16} /> : <Play size={16} />}
                {isPlaying ? 'Pause Car' : 'Resume Car'}
              </button>

              <button
                className="btn btn-secondary"
                style={{ width: 'auto', padding: '0.5rem 1rem' }}
                onClick={() => { setAnimProgress(0); setIsPlaying(true); }}
              >
                <RotateCcw size={16} /> Replay
              </button>

              <div style={{ display: 'flex', gap: '4px', alignItems: 'center', marginLeft: '0.5rem' }}>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Speed:</span>
                {[1, 2, 5].map((spd) => (
                  <button
                    key={spd}
                    className="btn btn-secondary"
                    style={{
                      width: 'auto', padding: '3px 8px', fontSize: '0.75rem',
                      background: simSpeed === spd ? '#0284c7' : '#e2e8f0',
                      color: simSpeed === spd ? 'white' : '#0f172a'
                    }}
                    onClick={() => setSimSpeed(spd)}
                  >
                    {spd}x
                  </button>
                ))}
              </div>
            </div>

            <div style={{ textAlign: 'right' }}>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>REMAINING DISTANCE & ETA</div>
              <div style={{ fontSize: '1.1rem', fontWeight: 800, color: '#0284c7' }}>
                {currentDistRemaining}m | ETA: {etaSec}s
              </div>
            </div>
          </div>

          <div style={{ background: '#e2e8f0', height: '8px', borderRadius: '4px', overflow: 'hidden' }}>
            <div style={{ width: `${animProgress}%`, height: '100%', background: 'linear-gradient(90deg, #10b981, #f59e0b)', transition: 'width 0.15s linear' }} />
          </div>
        </div>
      </div>
      {activeDispatchModal && (
        <DriverNavigationModal
          dispatch={activeDispatchModal}
          onClose={() => setActiveDispatchModal(null)}
          onLog={onLog}
        />
      )}
    </div>
  );
}

export default function DispatchEngine({ onLog }) {
  // Main Max Heap Queue (sorted by Priority Score)
  const [heapQueue, setHeapQueue] = useState(generateInitialRequests());
  // Driver Fleet Queue (Circular Queue for allocation rotation)
  const [driverQueue, setDriverQueue] = useState(INITIAL_DRIVERS);
  // Active Linked Dispatches (Requester <-> Assigned Driver <-> Dijkstra Path)
  const [activeDispatches, setActiveDispatches] = useState([]);
  // Emergency Preemption Deque (Front insertion)
  const [urgentDeque, setUrgentDeque] = useState([]);
  // LIFO Undo Stack
  const [undoStack, setUndoStack] = useState([]);
  const [activeDispatchModal, setActiveDispatchModal] = useState(null);

  // Selected request for formula inspection
  const [selectedReqForFormula, setSelectedReqForFormula] = useState(null);
  const [isDemoMode, setIsDemoMode] = useState(false);
  const [isAutoAgingActive, setIsAutoAgingActive] = useState(false);

  // New Request Form State
  const [requesterName, setRequesterName] = useState('Dr. Abena Osei');
  const [role, setRole] = useState('Faculty (Chemistry Dept)');
  const [category, setCategory] = useState('EMERGENCY_MEDICAL');
  const [urgency, setUrgency] = useState('CRITICAL');
  const [pickup, setPickup] = useState(26); // Chemistry Dept
  const [dest, setDest] = useState(1); // UG Hospital
  const [waitTime, setWaitTime] = useState(12);
  const [hasDisability, setHasDisability] = useState(false);
  const [note, setNote] = useState('Urgent medical evaluation needed');

  const previewScore = calculatePriorityScore(category, urgency, waitTime, dest, hasDisability);

  // Keep selected request for formula defaulted to heap root
  useEffect(() => {
    if (!selectedReqForFormula && heapQueue.length > 0) {
      setSelectedReqForFormula(heapQueue[0]);
    }
  }, [heapQueue]);

  // Auto-aging interval ticker (ages requests every 10 seconds if enabled)
  useEffect(() => {
    let interval = null;
    if (isAutoAgingActive) {
      interval = setInterval(() => {
        ageWaitTimes(2);
      }, 10000);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isAutoAgingActive, heapQueue, urgentDeque]);

  // Simulate wait time aging (+5 minutes for all pending requests)
  const ageWaitTimes = (additionalMinutes = 5) => {
    if (heapQueue.length === 0 && urgentDeque.length === 0) return;

    // Age requests in Max Heap and re-calculate priority score
    const agedHeap = heapQueue
      .map(req => {
        const newWait = req.waitTime + additionalMinutes;
        const newScore = calculatePriorityScore(
          req.category,
          req.urgency,
          newWait,
          req.destLocation,
          req.hasDisability
        );
        return {
          ...req,
          waitTime: newWait,
          priorityScore: newScore
        };
      })
      .sort((a, b) => b.priorityScore - a.priorityScore);

    // Age requests in Emergency Deque
    const agedDeque = urgentDeque.map(req => {
      const newWait = req.waitTime + additionalMinutes;
      const newScore = calculatePriorityScore(
        req.category,
        req.urgency,
        newWait,
        req.destLocation,
        req.hasDisability
      );
      return {
        ...req,
        waitTime: newWait,
        priorityScore: newScore
      };
    });

    setHeapQueue(agedHeap);
    setUrgentDeque(agedDeque);

    if (agedHeap.length > 0) {
      const topReq = agedHeap[0];
      const ptsIncrease = additionalMinutes * 15;
      if (onLog) {
        onLog(
          'WAIT_TIME_AGING',
          `⏳ WAIT TIME PRIORITY INCREASE: Added +${additionalMinutes} mins wait time to ${agedHeap.length} pending requests (+${ptsIncrease} pts per request). Max Heap root "${topReq.requesterName}" priority score increased to ${topReq.priorityScore} pts (Re-heapified Max-Heap).`
        );
      }
    }
  };

  // Handle adding a new request
  const handleAddRequest = (e) => {
    e.preventDefault();
    const newReq = {
      id: Date.now() % 10000,
      requesterName,
      role,
      category,
      urgency,
      pickupLocation: pickup,
      destLocation: dest,
      waitTime,
      hasDisability,
      note,
      priorityScore: previewScore,
      status: 'PENDING'
    };

    if (category === 'EMERGENCY_MEDICAL' && urgency === 'CRITICAL') {
      setUrgentDeque([newReq, ...urgentDeque]);
      if (onLog) {
        onLog(
          'DEQUE_PREEMPTION',
          `🚨 CRITICAL Emergency: "${requesterName}" (${role}) inserted at FRONT of Deque (Score: ${newReq.priorityScore} pts)`
        );
      }
    }

    const updatedHeap = [...heapQueue, newReq].sort((a, b) => b.priorityScore - a.priorityScore);
    setHeapQueue(updatedHeap);
    setSelectedReqForFormula(newReq);

    if (onLog) {
      onLog(
        'MAX_HEAP_ENQUEUE',
        `Inserted "${requesterName}" into CustomMaxHeap at computed priority ${newReq.priorityScore} pts`
      );
    }
  };

  // Main Dispatch Action: Link highest priority request to an available driver
  const dispatchHighestPriority = () => {
    let reqToDispatch = null;
    let isFromDeque = false;

    if (urgentDeque.length > 0) {
      reqToDispatch = urgentDeque[0];
      setUrgentDeque(urgentDeque.slice(1));
      isFromDeque = true;
    } else if (heapQueue.length > 0) {
      reqToDispatch = heapQueue[0];
      setHeapQueue(heapQueue.slice(1));
    } else {
      alert('No pending requests in queue!');
      return;
    }

    // Find available driver from Circular Queue
    const availableDriverIndex = driverQueue.findIndex(d => d.status === 'AVAILABLE');
    let assignedDriver = null;

    if (availableDriverIndex !== -1) {
      assignedDriver = driverQueue[availableDriverIndex];
    } else {
      assignedDriver = driverQueue[0];
    }

    // Compute Dijkstra shortest path from Pickup to Destination
    const route = computeDijkstraPath(reqToDispatch.pickupLocation, reqToDispatch.destLocation);
    const pickupLocObj = CAMPUS_LOCATIONS.find(l => l.id === reqToDispatch.pickupLocation);
    const destLocObj = CAMPUS_LOCATIONS.find(l => l.id === reqToDispatch.destLocation);

    const newDispatch = {
      id: `DSP-${Math.floor(1000 + Math.random() * 9000)}`,
      request: reqToDispatch,
      driver: { ...assignedDriver, status: 'IN_TRANSIT' },
      route,
      pickupName: pickupLocObj?.name || `#${reqToDispatch.pickupLocation}`,
      destName: destLocObj?.name || `#${reqToDispatch.destLocation}`,
      timestamp: new Date().toLocaleTimeString(),
      status: 'IN_TRANSIT',
      step: 'En Route to Pickup'
    };

    // Update Driver Queue: mark driver as IN_TRANSIT and rotate to tail
    const updatedDrivers = driverQueue.map(d =>
      d.id === assignedDriver.id ? { ...d, status: 'IN_TRANSIT' } : d
    );

    const activeDriver = updatedDrivers.find(d => d.id === assignedDriver.id);
    const remainingDrivers = updatedDrivers.filter(d => d.id !== assignedDriver.id);
    setDriverQueue([...remainingDrivers, activeDriver]);

    setActiveDispatches([newDispatch, ...activeDispatches]);
    setUndoStack([newDispatch, ...undoStack]);

    if (onLog) {
      onLog(
        isFromDeque ? 'EMERGENCY_DISPATCH' : 'MAX_HEAP_DISPATCH',
        `⚡ LINKED Requester "${reqToDispatch.requesterName}" (${reqToDispatch.priorityScore} pts) ➔ Driver "${assignedDriver.name}" (${assignedDriver.vehicle}). Dijkstra Route: ${route.distance}m via ${route.pathNodeCount} nodes.`
      );
    }
  };

  // Complete an active dispatch and return driver to available queue
  const completeDispatch = (dispatchId) => {
    const target = activeDispatches.find(d => d.id === dispatchId);
    if (!target) return;

    setActiveDispatches(activeDispatches.filter(d => d.id !== dispatchId));

    setDriverQueue(prev =>
      prev.map(d => (d.id === target.driver.id ? { ...d, status: 'AVAILABLE' } : d))
    );

    if (onLog) {
      onLog(
        'DISPATCH_COMPLETE',
        `✅ Dispatch ${target.id} completed! Driver "${target.driver.name}" returned to Available Circular Queue.`
      );
    }
  };

  // Rollback / Undo last dispatch using CustomStack LIFO
  const handleUndo = () => {
    if (undoStack.length === 0) return;
    const lastDispatch = undoStack[0];
    setUndoStack(undoStack.slice(1));

    setActiveDispatches(prev => prev.filter(d => d.id !== lastDispatch.id));

    setHeapQueue(prev => [...prev, lastDispatch.request].sort((a, b) => b.priorityScore - a.priorityScore));

    setDriverQueue(prev =>
      prev.map(d => (d.id === lastDispatch.driver.id ? { ...d, status: 'AVAILABLE' } : d))
    );

    if (onLog) {
      onLog(
        'UNDO_STACK_POP',
        `↺ Undo Executed (LIFO Stack): Rolled back Dispatch ${lastDispatch.id}. Re-enqueued "${lastDispatch.request.requesterName}" into Max-Heap.`
      );
    }
  };

  // Rotate Driver Queue manually
  const rotateDriverQueue = () => {
    if (driverQueue.length <= 1) return;
    const [head, ...tail] = driverQueue;
    setDriverQueue([...tail, head]);
    if (onLog) {
      onLog('CIRCULAR_QUEUE_ROTATE', `Rotated Driver Queue: "${head.name}" moved from Head to Tail.`);
    }
  };

  // Interactive Presentation Demo Run
  const runPresentationDemo = () => {
    setIsDemoMode(true);
    if (onLog) onLog('PRESENTATION_DEMO', '🚀 Launching UG Campus Dispatch Automated Defense Demo...');

    setTimeout(() => {
      const emergencyReq = {
        id: 9999,
        requesterName: 'Prof. Abena Mensah',
        role: 'Dean of Science (Noguchi)',
        category: 'EMERGENCY_MEDICAL',
        urgency: 'CRITICAL',
        pickupLocation: 39,
        destLocation: 1,
        waitTime: 15,
        hasDisability: true,
        note: 'Severe respiratory distress at lab - Urgent ICU Ambulance required',
        priorityScore: calculatePriorityScore('EMERGENCY_MEDICAL', 'CRITICAL', 15, 1, true),
        status: 'PENDING'
      };

      setUrgentDeque(prev => [emergencyReq, ...prev]);
      if (onLog) {
        onLog('DEMO_STEP_1', `🚨 STEP 1: Emergency Preemption triggered! Inserted "${emergencyReq.requesterName}" at Front of CustomDeque (${emergencyReq.priorityScore} pts)`);
      }
    }, 600);

    setTimeout(() => {
      dispatchHighestPriority();
      if (onLog) {
        onLog('DEMO_STEP_2', '⚡ STEP 2: Extracted Deque Front item & computed Dijkstra Shortest Path route on Google Maps canvas!');
      }
      setIsDemoMode(false);
    }, 1800);
  };

  // Check Heap Invariant for display
  const isHeapValid = () => {
    for (let i = 0; i < Math.floor(heapQueue.length / 2); i++) {
      const left = 2 * i + 1;
      const right = 2 * i + 2;
      if (left < heapQueue.length && heapQueue[i].priorityScore < heapQueue[left].priorityScore) return false;
      if (right < heapQueue.length && heapQueue[i].priorityScore < heapQueue[right].priorityScore) return false;
    }
    return true;
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
      {/* Presentation Demo Banner & Key Performance Indicators */}
      <div
        className="card"
        style={{
          background: 'linear-gradient(135deg, #0f172a 0%, #1e293b 100%)',
          color: '#ffffff',
          borderColor: '#334155',
          marginBottom: 0
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', flexWrap: 'wrap' }}>
              <span
                style={{
                  background: '#0284c7',
                  color: '#fff',
                  padding: '2px 8px',
                  borderRadius: '6px',
                  fontSize: '0.75rem',
                  fontWeight: 800,
                  letterSpacing: '0.05em'
                }}
              >
                LIVE DEFENSE PRESENTATION MODE
              </span>
              <h2 style={{ fontSize: '1.2rem', fontWeight: 800, color: '#f8fafc', margin: 0 }}>
                Priority Dispatch Engine & Live Driver Linking Visualizer
              </h2>
            </div>
            <p style={{ fontSize: '0.8rem', color: '#94a3b8', marginTop: '4px' }}>
              Demonstrating <b>CustomMaxHeap ($O(\log N)$)</b> priority queue, <b>CustomDeque</b> preemption, <b>Circular Driver Queue</b>, and <b>Dijkstra ($O((V+E)\log V)$)</b> shortest route linking.
            </p>
          </div>

          <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
            <button
              className="btn btn-warning"
              style={{ width: 'auto', padding: '0.55rem 1rem', fontSize: '0.8rem' }}
              onClick={() => ageWaitTimes(5)}
              title="Simulate +5 mins wait time aging for pending requests"
            >
              <Clock size={15} /> Age Wait Times (+5m)
            </button>

            <button
              className={`btn ${isAutoAgingActive ? 'btn-danger' : 'btn-secondary'}`}
              style={{ width: 'auto', padding: '0.55rem 1rem', fontSize: '0.8rem' }}
              onClick={() => setIsAutoAgingActive(!isAutoAgingActive)}
            >
              {isAutoAgingActive ? '⏸ Stop Auto-Aging' : '▶ Auto-Age (10s)'}
            </button>

            <button
              className="btn btn-accent"
              style={{ width: 'auto', padding: '0.55rem 1.1rem', fontSize: '0.85rem', fontWeight: 700 }}
              onClick={runPresentationDemo}
              disabled={isDemoMode}
            >
              <Sparkles size={16} /> {isDemoMode ? 'Running Demo...' : '⚡ Run Presentation Demo'}
            </button>
          </div>
        </div>

        {/* Top KPI Metrics Bar */}
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))',
            gap: '0.75rem',
            marginTop: '1.25rem',
            paddingTop: '1rem',
            borderTop: '1px solid #334155'
          }}
        >
          <div style={{ background: 'rgba(255, 255, 255, 0.05)', padding: '10px 14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ fontSize: '0.7rem', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 700 }}>Enqueued Requests</div>
            <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#38bdf8', marginTop: '2px' }}>
              {heapQueue.length} <span style={{ fontSize: '0.75rem', color: '#64748b' }}>in Max-Heap</span>
            </div>
          </div>

          <div style={{ background: 'rgba(255, 255, 255, 0.05)', padding: '10px 14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ fontSize: '0.7rem', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 700 }}>Heap Root Priority</div>
            <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#f59e0b', marginTop: '2px' }}>
              {heapQueue.length > 0 ? `${heapQueue[0].priorityScore} pts` : '0 pts'}
            </div>
            <div style={{ fontSize: '0.7rem', color: '#cbd5e1', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
              👑 {heapQueue.length > 0 ? heapQueue[0].requesterName : 'None'}
            </div>
          </div>

          <div style={{ background: 'rgba(255, 255, 255, 0.05)', padding: '10px 14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ fontSize: '0.7rem', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 700 }}>Available Drivers Queue</div>
            <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#10b981', marginTop: '2px' }}>
              {driverQueue.filter(d => d.status === 'AVAILABLE').length} / {driverQueue.length}
            </div>
            <div style={{ fontSize: '0.7rem', color: '#cbd5e1' }}>Circular Queue Head</div>
          </div>

          <div style={{ background: 'rgba(255, 255, 255, 0.05)', padding: '10px 14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ fontSize: '0.7rem', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 700 }}>Active Linked Dispatches</div>
            <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#818cf8', marginTop: '2px' }}>
              {activeDispatches.length} <span style={{ fontSize: '0.75rem', color: '#64748b' }}>dispatched</span>
            </div>
          </div>

          <div style={{ background: 'rgba(255, 255, 255, 0.05)', padding: '10px 14px', borderRadius: '8px', border: '1px solid #334155' }}>
            <div style={{ fontSize: '0.7rem', color: '#94a3b8', textTransform: 'uppercase', fontWeight: 700 }}>Emergency Deque Front</div>
            <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#f43f5e', marginTop: '2px' }}>
              {urgentDeque.length} <span style={{ fontSize: '0.75rem', color: '#64748b' }}>preempted</span>
            </div>
          </div>
        </div>
      </div>

      {/* Main Responsive Grid Layout */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.25rem' }}>
        {/* Left Column: Visualizers & Live Dispatches */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          
          {/* Active Dispatches & Driver Linking Cards */}
          <div className="card" style={{ borderColor: 'var(--accent-indigo)' }}>
            <div className="card-title" style={{ color: 'var(--accent-indigo)' }}>
              <span>
                <Award size={18} inline style={{ marginRight: '6px' }} /> Live Driver-to-Requester Dispatches & Linking Engine
              </span>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                {activeDispatches.length} Active Dispatches Linked
              </span>
            </div>

            {activeDispatches.length === 0 ? (
              <div
                style={{
                  padding: '1.5rem',
                  textAlign: 'center',
                  background: '#f8fafc',
                  borderRadius: '8px',
                  border: '1px dashed var(--border-color)',
                  color: 'var(--text-muted)'
                }}
              >
                <Car size={32} style={{ opacity: 0.3, marginBottom: '8px' }} />
                <div style={{ fontSize: '0.9rem', fontWeight: 600 }}>No Active Driver Dispatches</div>
                <div style={{ fontSize: '0.75rem', marginTop: '4px' }}>
                  Click <b>"Dispatch Highest Priority (Heap Root)"</b> or run the Presentation Demo to link drivers to requests.
                </div>
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.85rem' }}>
                {activeDispatches.map(dsp => (
                  <div
                    key={dsp.id}
                    style={{
                      background: '#ffffff',
                      border: '1px solid var(--accent-indigo)',
                      borderRadius: '10px',
                      padding: '1rem',
                      boxShadow: '0 4px 12px rgba(79, 70, 229, 0.08)'
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem', paddingBottom: '0.5rem', borderBottom: '1px solid #f1f5f9', flexWrap: 'wrap', gap: '0.4rem' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                        <span style={{ background: '#4f46e5', color: '#fff', fontSize: '0.7rem', fontWeight: 800, padding: '2px 8px', borderRadius: '4px' }}>
                          {dsp.id}
                        </span>
                        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Dispatched at {dsp.timestamp}</span>
                      </div>
                      <span style={{ fontSize: '0.75rem', background: '#dcfce7', color: '#15803d', padding: '2px 8px', borderRadius: '12px', fontWeight: 'bold' }}>
                        ● {dsp.step}
                      </span>
                    </div>

                    {/* Side-by-Side Requester <-> Route <-> Driver Card */}
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '0.75rem', alignItems: 'center' }}>
                      {/* Requester Details */}
                      <div style={{ background: '#f8fafc', padding: '10px', borderRadius: '8px', border: '1px solid #e2e8f0' }}>
                        <div style={{ fontSize: '0.65rem', color: 'var(--text-muted)', fontWeight: 700, textTransform: 'uppercase' }}>REQUESTER (CUSTOMER)</div>
                        <div style={{ fontSize: '0.95rem', fontWeight: 800, color: 'var(--text-main)', marginTop: '2px' }}>
                          {dsp.request.requesterName}
                        </div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--accent-blue)', fontWeight: 600 }}>
                          {dsp.request.role}
                        </div>
                        <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                          📍 Pickup: <b>{dsp.pickupName}</b> ➔ Dest: <b>{dsp.destName}</b>
                        </div>
                        <div style={{ marginTop: '6px', display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
                          <span style={{ fontSize: '0.65rem', background: '#fef3c7', color: '#b45309', padding: '1px 6px', borderRadius: '4px', fontWeight: 700 }}>
                            {dsp.request.priorityScore} PTS
                          </span>
                          {dsp.request.hasDisability && (
                            <span style={{ fontSize: '0.65rem', background: '#ede9fe', color: '#6d28d9', padding: '1px 6px', borderRadius: '4px', fontWeight: 700 }}>
                              ♿ Wheelchair Access
                            </span>
                          )}
                        </div>
                      </div>

                      {/* Linking Visualizer & Dijkstra Info */}
                      <div style={{ textAlign: 'center', padding: '0 6px' }}>
                        <div style={{ fontSize: '0.65rem', fontWeight: 800, color: 'var(--accent-emerald)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>
                          DIJKSTRA ROUTE
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '4px', margin: '4px 0' }}>
                          <ArrowRight size={18} style={{ color: 'var(--accent-emerald)' }} />
                          <span style={{ fontSize: '0.85rem', fontWeight: 800, color: 'var(--accent-emerald)' }}>
                            {dsp.route.distance}m
                          </span>
                          <ArrowRight size={18} style={{ color: 'var(--accent-emerald)' }} />
                        </div>
                        <div style={{ fontSize: '0.65rem', color: 'var(--text-muted)' }}>
                          {dsp.route.pathNodeCount} Graph Nodes Traversed
                        </div>
                      </div>

                      {/* Assigned Driver Details */}
                      <div style={{ background: '#f8fafc', padding: '10px', borderRadius: '8px', border: '1px solid #e2e8f0' }}>
                        <div style={{ fontSize: '0.65rem', color: 'var(--text-muted)', fontWeight: 700, textTransform: 'uppercase' }}>ASSIGNED DRIVER</div>
                        <div style={{ fontSize: '0.95rem', fontWeight: 800, color: 'var(--text-main)', marginTop: '2px' }}>
                          {dsp.driver.name}
                        </div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--accent-emerald)', fontWeight: 600 }}>
                          🚕 {dsp.driver.vehicle}
                        </div>
                        <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', marginTop: '4px' }}>
                          📞 {dsp.driver.phone || '+233 24 000 0000'}
                        </div>
                        <div style={{ marginTop: '6px' }}>
                          <span style={{ fontSize: '0.65rem', background: '#dbeafe', color: '#1e40af', padding: '1px 6px', borderRadius: '4px', fontWeight: 700 }}>
                            Vehicle ID: {dsp.driver.id}
                          </span>
                        </div>
                      </div>
                    </div>

                    <div style={{ marginTop: '0.75rem', display: 'flex', gap: '0.5rem', justifyContent: 'flex-end' }}>
                      <button
                        className="btn btn-primary"
                        style={{ width: 'auto', padding: '0.4rem 0.8rem', fontSize: '0.75rem', background: 'linear-gradient(135deg, #f59e0b, #d97706)' }}
                        onClick={() => setActiveDispatchModal(dsp)}
                      >
                        <Car size={14} /> 🚕 View Live Car Movement
                      </button>
                      <button
                        className="btn btn-secondary"
                        style={{ width: 'auto', padding: '0.4rem 0.8rem', fontSize: '0.75rem' }}
                        onClick={() => completeDispatch(dsp.id)}
                      >
                        <CheckCircle2 size={14} style={{ color: 'var(--accent-emerald)' }} /> Mark Complete (Return Driver to Queue)
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Custom Max-Heap Visualizer (Array & Root Controls) */}
          <div className="card">
            <div className="card-title">
              <span>
                <Zap size={18} inline style={{ color: 'var(--accent-gold)' }} /> CustomMaxHeap Visualizer (Binary Max-Heap Array)
              </span>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', flexWrap: 'wrap' }}>
                <span
                  style={{
                    fontSize: '0.7rem',
                    background: isHeapValid() ? '#dcfce7' : '#fee2e2',
                    color: isHeapValid() ? '#15803d' : '#991b1b',
                    padding: '2px 8px',
                    borderRadius: '12px',
                    fontWeight: 'bold'
                  }}
                >
                  {isHeapValid() ? '✓ Heap Invariant Satisfied' : '⚠ Violates Heap Property'}
                </span>
                <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{heapQueue.length} Items</span>
              </div>
            </div>

            <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginBottom: '0.75rem' }}>
              Array representation of binary max-heap. Parent index formula: <code>parent = Math.floor((i-1)/2)</code>. Wait time bonus increases score over time ($+15$ pts/min).
            </p>

            <div className="ds-container" style={{ marginBottom: '1rem' }}>
              {heapQueue.slice(0, 10).map((item, idx) => {
                const isSelected = selectedReqForFormula?.id === item.id;
                return (
                  <div
                    key={item.id}
                    className="array-box"
                    style={{
                      borderColor: idx === 0 ? 'var(--accent-gold)' : isSelected ? 'var(--accent-blue)' : 'var(--border-color)',
                      background: idx === 0 ? '#fffbeb' : isSelected ? '#f0f9ff' : '#ffffff',
                      cursor: 'pointer',
                      minWidth: '110px',
                      padding: '8px'
                    }}
                    onClick={() => setSelectedReqForFormula(item)}
                  >
                    <div className="idx" style={{ fontWeight: idx === 0 ? 800 : 600, color: idx === 0 ? '#b45309' : '#64748b' }}>
                      {idx === 0 ? '👑 Root [0]' : `Index [${idx}]`}
                    </div>
                    <div className="val" style={{ fontSize: '0.85rem', color: 'var(--text-main)', marginTop: '2px' }}>
                      {item.requesterName}
                    </div>
                    <div className="sub" style={{ fontWeight: 800, color: 'var(--accent-gold)' }}>
                      {item.priorityScore} pts
                    </div>
                    <div style={{ fontSize: '0.6rem', color: 'var(--accent-emerald)', marginTop: '2px' }}>
                      ⏱ {item.waitTime}m wait
                    </div>
                  </div>
                );
              })}
            </div>

            <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
              <button className="btn btn-primary" style={{ flex: 1, minWidth: '200px' }} onClick={dispatchHighestPriority}>
                <ArrowUpRight size={16} /> Dispatch Highest Priority (Heap Root)
              </button>
              <button
                className="btn btn-warning"
                style={{ width: 'auto' }}
                onClick={() => ageWaitTimes(5)}
                title="Simulate +5 mins wait time priority increase"
              >
                <Clock size={15} /> Age Wait Times (+5m)
              </button>
              <button
                className="btn btn-secondary"
                style={{ width: 'auto' }}
                onClick={handleUndo}
                disabled={undoStack.length === 0}
              >
                <RotateCcw size={16} /> Undo ({undoStack.length})
              </button>
            </div>
          </div>

          {/* Driver Fleet Queue & Deque Visualizers */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '1rem' }}>
            {/* Driver Queue (Circular Queue) */}
            <div className="card" style={{ marginBottom: 0 }}>
              <div className="card-title" style={{ color: 'var(--accent-blue)' }}>
                <span>
                  <Car size={16} inline style={{ marginRight: '4px' }} /> Circular Driver Queue
                </span>
                <button
                  className="btn btn-secondary"
                  style={{ width: 'auto', padding: '2px 8px', fontSize: '0.7rem' }}
                  onClick={rotateDriverQueue}
                >
                  <RotateCcw size={12} /> Rotate Head
                </button>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', maxHeight: '220px', overflowY: 'auto' }}>
                {driverQueue.map((d, idx) => (
                  <div
                    key={d.id}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      padding: '6px 10px',
                      background: idx === 0 ? '#f0f9ff' : '#f8fafc',
                      borderRadius: '6px',
                      border: '1px solid #e2e8f0',
                      fontSize: '0.75rem'
                    }}
                  >
                    <div>
                      <span style={{ fontWeight: 700, color: 'var(--text-main)' }}>
                        {idx === 0 ? '👉 Head: ' : `[${idx}] `}
                        {d.name}
                      </span>
                      <div style={{ fontSize: '0.65rem', color: 'var(--text-muted)' }}>
                        {d.vehicle} {d.isWheelchairAccessible && '♿ Accessible'}
                      </div>
                    </div>
                    <span
                      style={{
                        fontSize: '0.65rem',
                        fontWeight: 700,
                        padding: '2px 6px',
                        borderRadius: '4px',
                        background: d.status === 'AVAILABLE' ? '#dcfce7' : '#fef3c7',
                        color: d.status === 'AVAILABLE' ? '#15803d' : '#b45309'
                      }}
                    >
                      {d.status}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Emergency Preemption Deque */}
            <div className="card" style={{ marginBottom: 0 }}>
              <div className="card-title" style={{ color: 'var(--accent-rose)' }}>
                <span>
                  <ShieldAlert size={16} inline style={{ marginRight: '4px' }} /> CustomDeque Emergency Preemption
                </span>
                <span style={{ fontSize: '0.75rem' }}>{urgentDeque.length} Preempted</span>
              </div>

              <div className="ds-container" style={{ minHeight: '120px' }}>
                {urgentDeque.length === 0 ? (
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', padding: '15px', textAlign: 'center', width: '100%' }}>
                    No preemption emergency requests at front of deque.
                  </div>
                ) : (
                  urgentDeque.map((item, idx) => (
                    <div key={item.id} className="array-box" style={{ borderColor: 'var(--accent-rose)', background: '#fff1f2', minWidth: '100px' }}>
                      <div className="idx" style={{ color: '#be123c', fontWeight: 800 }}>Front [{idx}]</div>
                      <div className="val" style={{ fontSize: '0.8rem', color: '#881337' }}>{item.requesterName}</div>
                      <div className="sub" style={{ color: '#be123c', fontWeight: 800 }}>🚨 {item.priorityScore} pts</div>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>

          {/* Under-The-Hood Priority Formula Inspector */}
          {selectedReqForFormula && (
            <div className="card" style={{ borderColor: 'var(--accent-gold)', marginBottom: 0 }}>
              <div className="card-title" style={{ color: 'var(--accent-gold)' }}>
                <span>
                  <Layers size={18} inline style={{ marginRight: '6px' }} /> Under-the-Hood Priority Score Formula Inspector
                </span>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  Inspecting: <b>{selectedReqForFormula.requesterName}</b>
                </span>
              </div>

              <div style={{ background: '#0f172a', color: '#f8fafc', padding: '1rem', borderRadius: '8px', fontSize: '0.8rem', fontFamily: 'var(--font-mono)' }}>
                <div style={{ color: '#38bdf8', marginBottom: '8px', fontWeight: 700 }}>
                  P(R) = (CategoryWeight × BaseWeight) + UrgencyValue + WaitBonus + HospitalBonus + DisabilityBonus
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))', gap: '8px', color: '#94a3b8' }}>
                  <div>
                    Category: <b style={{ color: '#f59e0b' }}>{selectedReqForFormula.category}</b>
                  </div>
                  <div>
                    Urgency: <b style={{ color: '#ef4444' }}>{selectedReqForFormula.urgency}</b>
                  </div>
                  <div>
                    Wait Time: <b style={{ color: '#10b981' }}>{selectedReqForFormula.waitTime}m</b> (+{selectedReqForFormula.waitTime * 15} pts)
                  </div>
                  <div>
                    Disability: <b style={{ color: '#a855f7' }}>{selectedReqForFormula.hasDisability ? 'YES (+150 pts)' : 'NO'}</b>
                  </div>
                  <div>
                    Hospital Dest: <b style={{ color: '#38bdf8' }}>{selectedReqForFormula.destLocation === 1 ? 'YES (+250 pts)' : 'NO'}</b>
                  </div>
                </div>

                <div style={{ marginTop: '10px', paddingTop: '8px', borderTop: '1px solid #334155', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span>TOTAL COMPUTED PRIORITY SCORE:</span>
                  <span style={{ fontSize: '1.2rem', fontWeight: 800, color: '#f59e0b' }}>
                    {selectedReqForFormula.priorityScore} POINTS
                  </span>
                </div>
              </div>
            </div>
          )}

        </div>

        {/* Right Column: Submit New Request Form */}
        <div>
          <div className="card">
            <div className="card-title">
              <span>
                <PlusCircle size={18} inline style={{ color: 'var(--accent-blue)', marginRight: '6px' }} /> Submit Service Request
              </span>
            </div>

            <form onSubmit={handleAddRequest}>
              <div className="form-group">
                <label>Requester Name</label>
                <select
                  className="form-control"
                  value={requesterName}
                  onChange={(e) => {
                    setRequesterName(e.target.value);
                    const match = REALISTIC_REQUESTERS.find(r => r.name === e.target.value);
                    if (match) {
                      setRole(match.role);
                      setCategory(match.category);
                      setHasDisability(match.isWheelchair);
                      setNote(match.note);
                    }
                  }}
                >
                  {REALISTIC_REQUESTERS.map((r, i) => (
                    <option key={i} value={r.name}>{r.name} ({r.role})</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Role / Affiliation</label>
                <input
                  type="text"
                  className="form-control"
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                />
              </div>

              <div className="form-group">
                <label>Service Category</label>
                <select className="form-control" value={category} onChange={(e) => setCategory(e.target.value)}>
                  <option value="EMERGENCY_MEDICAL">Emergency Medical (CatWeight=50, Base=100)</option>
                  <option value="STUDENT_MOBILITY">Student Mobility (CatWeight=30, Base=60)</option>
                  <option value="STAFF_TRANSPORT">Staff Transport (CatWeight=20, Base=40)</option>
                  <option value="EVENT_LOGISTICS">Event Logistics (CatWeight=15, Base=30)</option>
                </select>
              </div>

              <div className="form-group">
                <label>Urgency Level</label>
                <select className="form-control" value={urgency} onChange={(e) => setUrgency(e.target.value)}>
                  <option value="CRITICAL">Critical (+300 pts)</option>
                  <option value="HIGH">High (+200 pts)</option>
                  <option value="MEDIUM">Medium (+100 pts)</option>
                  <option value="LOW">Low (+20 pts)</option>
                </select>
              </div>

              <div className="form-group">
                <label>Pickup Location</label>
                <select className="form-control" value={pickup} onChange={(e) => setPickup(Number(e.target.value))}>
                  {CAMPUS_LOCATIONS.map((l) => (
                    <option key={l.id} value={l.id}>#{l.id}: {l.name} ({l.zone})</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Destination Location</label>
                <select className="form-control" value={dest} onChange={(e) => setDest(Number(e.target.value))}>
                  {CAMPUS_LOCATIONS.map((l) => (
                    <option key={l.id} value={l.id}>#{l.id}: {l.name} ({l.zone})</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Wait Time (Minutes)</label>
                <input
                  type="number"
                  className="form-control"
                  value={waitTime}
                  onChange={(e) => setWaitTime(Number(e.target.value))}
                  min="1"
                  max="120"
                />
              </div>

              <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <input
                  type="checkbox"
                  id="disabilityCheck"
                  checked={hasDisability}
                  onChange={(e) => setHasDisability(e.target.checked)}
                />
                <label htmlFor="disabilityCheck" style={{ margin: 0, cursor: 'pointer' }}>
                  Wheelchair / Mobility Disability (+150 pts)
                </label>
              </div>

              <div
                style={{
                  background: '#0f172a',
                  padding: '12px',
                  borderRadius: '8px',
                  marginBottom: '1rem',
                  border: '1px solid var(--border-color)',
                  textAlign: 'center'
                }}
              >
                <div style={{ fontSize: '0.7rem', color: '#94a3b8' }}>PREVIEW PRIORITY SCORE P(R):</div>
                <div style={{ fontSize: '1.5rem', fontWeight: '800', color: 'var(--accent-gold)' }}>{previewScore} PTS</div>
              </div>

              <button type="submit" className="btn btn-primary">
                Enqueue Request into Max-Heap
              </button>
            </form>
          </div>
        </div>
      </div>
      {activeDispatchModal && (
        <DriverNavigationModal
          dispatch={activeDispatchModal}
          onClose={() => setActiveDispatchModal(null)}
          onLog={onLog}
        />
      )}
    </div>
  );
}
