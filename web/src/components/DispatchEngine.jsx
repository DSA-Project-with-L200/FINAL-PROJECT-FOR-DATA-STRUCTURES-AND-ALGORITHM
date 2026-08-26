import React, { useState } from 'react';
import { generateInitialRequests, calculatePriorityScore, CAMPUS_LOCATIONS } from '../data/campusData';
import { Zap, ShieldAlert, ArrowUpRight, RotateCcw, PlusCircle } from 'lucide-react';

export default function DispatchEngine({ onLog }) {
  const [heapQueue, setHeapQueue] = useState(generateInitialRequests());
  const [fifoQueue, setFifoQueue] = useState([...heapQueue]);
  const [urgentDeque, setUrgentDeque] = useState([]);
  const [undoStack, setUndoStack] = useState([]);

  // New Request Form state
  const [category, setCategory] = useState('EMERGENCY_MEDICAL');
  const [urgency, setUrgency] = useState('CRITICAL');
  const [pickup, setPickup] = useState(4); // Pentagon
  const [dest, setDest] = useState(1); // UG Hospital
  const [waitTime, setWaitTime] = useState(10);

  const previewScore = calculatePriorityScore(category, urgency, waitTime, dest);

  const handleAddRequest = (e) => {
    e.preventDefault();
    const newReq = {
      id: Date.now() % 10000,
      name: `Req #${Date.now() % 10000}`,
      category,
      urgency,
      pickupLocation: pickup,
      destLocation: dest,
      waitTime,
      priorityScore: previewScore,
      status: 'PENDING'
    };

    if (category === 'EMERGENCY_MEDICAL' && urgency === 'CRITICAL') {
      setUrgentDeque([newReq, ...urgentDeque]);
      if (onLog) onLog('DEQUE_PREEMPTION', `Emergency Request #${newReq.id} inserted at FRONT of Deque (Score: ${newReq.priorityScore})`);
    }

    const updatedHeap = [...heapQueue, newReq].sort((a, b) => b.priorityScore - a.priorityScore);
    setHeapQueue(updatedHeap);
    setFifoQueue([...fifoQueue, newReq]);

    if (onLog) onLog('MAX_HEAP', `Enqueued Request #${newReq.id} with Priority Score ${newReq.priorityScore}`);
  };

  const dispatchMaxPriority = () => {
    if (urgentDeque.length > 0) {
      const dispatched = urgentDeque[0];
      setUrgentDeque(urgentDeque.slice(1));
      setUndoStack([dispatched, ...undoStack]);
      if (onLog) onLog('DISPATCH', `Dispatched EMERGENCY DEQUE Request #${dispatched.id} (Score: ${dispatched.priorityScore})`);
      return;
    }

    if (heapQueue.length === 0) return;
    const dispatched = heapQueue[0];
    const newHeap = heapQueue.slice(1);
    setHeapQueue(newHeap);
    setUndoStack([dispatched, ...undoStack]);

    if (onLog) onLog('MAX_HEAP_DISPATCH', `Extracted MAX Root Request #${dispatched.id} (Priority: ${dispatched.priorityScore} pts)`);
  };

  const handleUndo = () => {
    if (undoStack.length === 0) return;
    const restored = undoStack[0];
    setUndoStack(undoStack.slice(1));
    setHeapQueue([...heapQueue, restored].sort((a, b) => b.priorityScore - a.priorityScore));

    if (onLog) onLog('UNDO_STACK', `Popped Stack Top: Restored Request #${restored.id} back to Queue`);
  };

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 340px', gap: '1.5rem' }}>
      {/* Left Column: Visualizers */}
      <div>
        {/* Max Heap Array Visualizer */}
        <div className="card">
          <div className="card-title">
            <span><Zap size={18} inline style={{ color: 'var(--accent-gold)' }} /> CustomMaxHeap Visualizer (Binary Max-Heap Array)</span>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{heapQueue.length} Items Enqueued</span>
          </div>

          <div className="ds-container" style={{ marginBottom: '1rem' }}>
            {heapQueue.slice(0, 10).map((item, idx) => (
              <div key={item.id} className="array-box" style={{ borderColor: idx === 0 ? 'var(--accent-gold)' : 'var(--border-color)' }}>
                <div className="idx">{idx === 0 ? '👑 Root [0]' : `[${idx}]`}</div>
                <div className="val">#{item.id}</div>
                <div className="sub">{item.priorityScore} pts</div>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', gap: '0.75rem' }}>
            <button className="btn btn-primary" onClick={dispatchMaxPriority}>
              <ArrowUpRight size={16} /> Dispatch Highest Priority (Heap Root)
            </button>
            <button className="btn btn-warning" onClick={handleUndo} disabled={undoStack.length === 0}>
              <RotateCcw size={16} /> Undo Last Action ({undoStack.length})
            </button>
          </div>
        </div>

        {/* Emergency Preemption Deque & Circular Queue */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
          <div className="card">
            <div className="card-title" style={{ color: 'var(--accent-rose)' }}>
              <span><ShieldAlert size={16} inline /> CustomDeque Emergency Preemption</span>
              <span style={{ fontSize: '0.75rem' }}>{urgentDeque.length} Urgent</span>
            </div>
            <div className="ds-container">
              {urgentDeque.length === 0 ? (
                <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', padding: '10px' }}>No preemption requests</div>
              ) : (
                urgentDeque.map((item, idx) => (
                  <div key={item.id} className="array-box" style={{ borderColor: 'var(--accent-rose)' }}>
                    <div className="idx">Front {idx}</div>
                    <div className="val">#{item.id}</div>
                    <div className="sub">🚨 {item.priorityScore}</div>
                  </div>
                ))
              )}
            </div>
          </div>

          <div className="card">
            <div className="card-title" style={{ color: 'var(--accent-blue)' }}>
              <span>Circular Queue (FIFO)</span>
              <span style={{ fontSize: '0.75rem' }}>{fifoQueue.length} Pending</span>
            </div>
            <div className="ds-container">
              {fifoQueue.slice(0, 5).map((item, idx) => (
                <div key={item.id} className="array-box">
                  <div className="idx">Head {idx}</div>
                  <div className="val">#{item.id}</div>
                  <div className="sub">{item.waitTime}m wait</div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Right Column: New Request Enqueue Panel */}
      <div>
        <div className="card">
          <div className="card-title">
            <span><PlusCircle size={18} inline style={{ color: 'var(--accent-blue)' }} /> Submit New Request</span>
          </div>

          <form onSubmit={handleAddRequest}>
            <div className="form-group">
              <label>Service Category</label>
              <select className="form-control" value={category} onChange={(e) => setCategory(e.target.value)}>
                <option value="EMERGENCY_MEDICAL">Emergency Medical (W=50)</option>
                <option value="STUDENT_MOBILITY">Student Mobility (W=30)</option>
                <option value="STAFF_TRANSPORT">Staff Transport (W=25)</option>
                <option value="GUEST_TRANSPORT">Guest Transport (W=15)</option>
                <option value="EVENT_LOGISTICS">Event Logistics (W=10)</option>
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
                  <option key={l.id} value={l.id}>#{l.id}: {l.name}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Destination Location</label>
              <select className="form-control" value={dest} onChange={(e) => setDest(Number(e.target.value))}>
                {CAMPUS_LOCATIONS.map((l) => (
                  <option key={l.id} value={l.id}>#{l.id}: {l.name}</option>
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

            <div style={{ background: '#0f172a', padding: '10px', borderRadius: '8px', marginBottom: '1rem', border: '1px solid var(--border-color)', textAlign: 'center' }}>
              <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>CALCULATED PRIORITY SCORE P(R):</div>
              <div style={{ fontSize: '1.4rem', fontWeight: '800', color: 'var(--accent-gold)' }}>{previewScore} PTS</div>
            </div>

            <button type="submit" className="btn btn-primary">
              Enqueue Request into Max-Heap
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
