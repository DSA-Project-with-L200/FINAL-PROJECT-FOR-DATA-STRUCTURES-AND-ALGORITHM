import React from 'react';
import { Cpu, AlertTriangle, CheckCircle } from 'lucide-react';

export default function KnapsackOptimizer() {
  // Counterexample Dataset: Taxi Capacity = 10 seats
  const sampleItems = [
    { id: 1, name: "Medical Staff Crew", weight: 6, priority: 300, ratio: 50.0 },
    { id: 2, name: "Student Emergency Group A", weight: 5, priority: 220, ratio: 44.0 },
    { id: 3, name: "Student Emergency Group B", weight: 5, priority: 220, ratio: 44.0 },
  ];

  const greedyResult = {
    selected: [sampleItems[0]], // Picks Item 1 (Weight 6, Ratio 50.0), then remaining capacity 4 cannot fit Item 2 or Item 3!
    totalWeight: 6,
    totalPriority: 300,
  };

  const dpResult = {
    selected: [sampleItems[1], sampleItems[2]], // Picks Item 2 & Item 3 (Weight 5+5=10), total priority 220+220 = 440!
    totalWeight: 10,
    totalPriority: 440,
  };

  return (
    <div>
      <div className="card" style={{ marginBottom: '1.5rem' }}>
        <div className="card-title" style={{ color: 'var(--accent-gold)' }}>
          <span><Cpu size={18} style={{ marginRight: '6px', verticalAlign: 'middle' }} /> Greedy vs. Dynamic Programming 0/1 Knapsack Optimization</span>
          <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Vehicle Capacity Constraint W = 10 seats</span>
        </div>
        <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', lineHeight: '1.6' }}>
          This visual counterexample proves why a purely <b>Greedy strategy</b> (sorting by Value/Weight ratio) can yield sub-optimal results, whereas <b>Dynamic Programming (0/1 Knapsack)</b> guarantees the globally optimal priority score within vehicle seating capacity constraints.
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
        {/* Greedy Strategy Panel */}
        <div className="card" style={{ borderColor: 'var(--accent-rose)' }}>
          <div className="card-title" style={{ color: 'var(--accent-rose)' }}>
            <span><AlertTriangle size={18} style={{ marginRight: '6px', verticalAlign: 'middle' }} /> Greedy Strategy (Value/Weight Ratio)</span>
            <span style={{ fontSize: '0.8rem' }}>Sub-Optimal (300 pts)</span>
          </div>

          <div style={{ background: '#0f172a', padding: '12px', borderRadius: '8px', marginBottom: '1rem' }}>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Selection Order:</div>
            <div style={{ marginTop: '6px', fontSize: '0.85rem' }}>
              1. <b>Medical Staff Crew</b> (Wt: 6, Score: 300, Ratio: 50.0) ➔ Selected!
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--accent-rose)', marginTop: '4px' }}>
              ❌ Remaining Capacity (4 seats) cannot fit Group A (Wt 5) or Group B (Wt 5). Search stops.
            </div>
          </div>

          <div style={{ padding: '10px', background: 'rgba(244, 63, 94, 0.1)', borderRadius: '8px', border: '1px solid var(--accent-rose)' }}>
            <div style={{ fontSize: '0.8rem' }}>Total Seats Used: <b>6 / 10 seats</b></div>
            <div style={{ fontSize: '1.1rem', fontWeight: 'bold', color: 'var(--accent-rose)' }}>Achieved Priority: 300 pts</div>
          </div>
        </div>

        {/* Dynamic Programming Strategy Panel */}
        <div className="card" style={{ borderColor: 'var(--accent-emerald)' }}>
          <div className="card-title" style={{ color: 'var(--accent-emerald)' }}>
            <span><CheckCircle size={18} inline /> Dynamic Programming 0/1 Knapsack</span>
            <span style={{ fontSize: '0.8rem' }}>Globally Optimal (+46.6% Higher)</span>
          </div>

          <div style={{ background: '#0f172a', padding: '12px', borderRadius: '8px', marginBottom: '1rem' }}>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>DP Table Recurrence Optimal Selection:</div>
            <div style={{ marginTop: '6px', fontSize: '0.85rem' }}>
              1. <b>Student Emergency Group A</b> (Wt: 5, Score: 220)<br />
              2. <b>Student Emergency Group B</b> (Wt: 5, Score: 220)
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--accent-emerald)', marginTop: '4px' }}>
              ✅ Perfect capacity utilization (10/10 seats) yields maximum priority!
            </div>
          </div>

          <div style={{ padding: '10px', background: 'rgba(16, 185, 129, 0.1)', borderRadius: '8px', border: '1px solid var(--accent-emerald)' }}>
            <div style={{ fontSize: '0.8rem' }}>Total Seats Used: <b>10 / 10 seats</b></div>
            <div style={{ fontSize: '1.1rem', fontWeight: 'bold', color: 'var(--accent-emerald)' }}>Achieved Priority: 440 pts</div>
          </div>
        </div>
      </div>
    </div>
  );
}
