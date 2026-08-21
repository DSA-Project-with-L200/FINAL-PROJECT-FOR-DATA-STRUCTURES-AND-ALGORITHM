import React from 'react';
import { Layers, Network, Database, Hash, GitBranch } from 'lucide-react';

export default function DataStructuresInspector() {
  const structures = [
    { name: "CustomDynamicArray", complexity: "Amortized O(1) Push", module: "M1: Foundations", icon: Layers, desc: "Resizable contiguous array structure powering queue buffers." },
    { name: "CustomHashTable", complexity: "O(1) Average Lookup", module: "M7: Hash Tables", icon: Hash, desc: "Chained hash table indexing requests & resources by ID." },
    { name: "CustomBST & RedBlackTree", complexity: "O(log N) Balanced Search", module: "M8: Trees", icon: GitBranch, desc: "Self-balancing binary search tree maintaining sorted priority ranges." },
    { name: "CustomBTree", complexity: "O(log N) Multi-Way Disk Index", module: "M8: Advanced Trees", icon: Database, desc: "Multi-way B-Tree structure for efficient secondary storage index." },
    { name: "CustomSkipList", complexity: "O(log N) Probabilistic Search", module: "M8: Skip Lists", icon: Network, desc: "Probabilistic multi-level skip list for fast ordered traversal." },
    { name: "CustomDisjointSet", complexity: "O(α(N)) Union-Find", module: "M6: Graph Structures", icon: Network, desc: "Disjoint Set with Path Compression powering Kruskal's MST algorithm." },
  ];

  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.25rem' }}>
      {structures.map((ds, idx) => {
        const Icon = ds.icon;
        return (
          <div key={idx} className="card">
            <div className="card-title" style={{ color: 'var(--accent-blue)' }}>
              <span><Icon size={18} inline style={{ marginRight: '6px' }} /> {ds.name}</span>
              <span style={{ fontSize: '0.7rem', color: 'var(--accent-emerald)', fontFamily: 'var(--font-mono)' }}>{ds.complexity}</span>
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--accent-gold)', marginBottom: '0.5rem' }}>{ds.module}</div>
            <p style={{ fontSize: '0.825rem', color: 'var(--text-muted)', lineHeight: '1.5' }}>{ds.desc}</p>
          </div>
        );
      })}
    </div>
  );
}
