import React, { useState, useEffect, useRef } from 'react';
import { CAMPUS_LOCATIONS, RAW_ROADS, calculateHaversineDistance } from '../data/campusData';
import { ZoomIn, ZoomOut, RotateCcw, Navigation, Route, Layers, MapPin } from 'lucide-react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

export default function CampusMap({ onLog }) {
  const mapContainerRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const tileLayerRef = useRef(null);
  const markersRef = useRef([]);
  const polylinesRef = useRef([]);

  const canvasRef = useRef(null);
  const [mapMode, setMapMode] = useState('google-roadmap'); // 'google-roadmap', 'google-satellite', 'google-hybrid', 'google-terrain', 'canvas'
  const [sourceNode, setSourceNode] = useState(4); // Pentagon
  const [targetNode, setTargetNode] = useState(1); // UG Hospital
  const [highlightedPath, setHighlightedPath] = useState([]);
  const [highlightedNodes, setHighlightedNodes] = useState(new Set());
  const [routeInfo, setRouteInfo] = useState(null);

  // Canvas Zoom & Pan state
  const [zoomScale, setZoomScale] = useState(1.0);
  const [panOffset, setPanOffset] = useState({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState(false);
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 });

  // Initialize Leaflet Map with Google Maps Tile Engine
  useEffect(() => {
    if (!mapContainerRef.current) return;

    if (!mapInstanceRef.current) {
      const map = L.map(mapContainerRef.current, {
        center: [5.6505, -0.1862], // Center on UG Legon Campus
        zoom: 15,
        zoomControl: false,
      });

      // Default Google Maps Roadmap Layer
      const tileLayer = L.tileLayer('https://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
        maxZoom: 20,
        attribution: '© Google Maps',
      }).addTo(map);

      mapInstanceRef.current = map;
      tileLayerRef.current = tileLayer;
    }

    updateGoogleMapLayers();
  }, []);

  // Run Dijkstra pathfinder on load or source/target change
  useEffect(() => {
    runDijkstra(sourceNode, targetNode);
  }, [sourceNode, targetNode]);

  // Update map overlays whenever path or mode changes
  useEffect(() => {
    if (mapMode.startsWith('google')) {
      updateGoogleMapLayers();
    } else {
      drawCanvas();
    }
  }, [mapMode, highlightedPath, highlightedNodes, sourceNode, targetNode, zoomScale, panOffset]);

  // Switch Google Maps Layer Style
  const handleMapStyleChange = (style) => {
    setMapMode(style);
    if (!mapInstanceRef.current) return;

    if (style.startsWith('google')) {
      if (tileLayerRef.current) mapInstanceRef.current.removeLayer(tileLayerRef.current);

      let tileUrl = 'https://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}'; // Roadmap
      if (style === 'google-satellite') tileUrl = 'https://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}';
      else if (style === 'google-hybrid') tileUrl = 'https://mt1.google.com/vt/lyrs=y&x={x}&y={y}&z={z}';
      else if (style === 'google-terrain') tileUrl = 'https://mt1.google.com/vt/lyrs=p&x={x}&y={y}&z={z}';

      tileLayerRef.current = L.tileLayer(tileUrl, { maxZoom: 20, attribution: '© Google Maps' }).addTo(mapInstanceRef.current);
      setTimeout(() => mapInstanceRef.current.invalidateSize(), 100);
    }
  };

  // Render Google Map Markers and Polylines
  const updateGoogleMapLayers = () => {
    const map = mapInstanceRef.current;
    if (!map) return;

    // Clear previous elements
    markersRef.current.forEach((m) => map.removeLayer(m));
    polylinesRef.current.forEach((p) => map.removeLayer(p));
    markersRef.current = [];
    polylinesRef.current = [];

    // Draw 100 Road Network Polylines
    RAW_ROADS.forEach(([id, u, v]) => {
      const srcLoc = CAMPUS_LOCATIONS.find((l) => l.id === u);
      const destLoc = CAMPUS_LOCATIONS.find((l) => l.id === v);
      if (!srcLoc || !destLoc) return;

      const isPath = isEdgeInPath(u, v);
      const color = isPath ? '#10b981' : '#3b82f6';
      const weight = isPath ? 7 : 3;
      const opacity = isPath ? 1.0 : 0.6;

      const polyline = L.polyline(
        [
          [srcLoc.lat, srcLoc.lng],
          [destLoc.lat, destLoc.lng],
        ],
        { color, weight, opacity }
      ).addTo(map);

      polylinesRef.current.push(polyline);
    });

    // Draw 50 Location Pins
    CAMPUS_LOCATIONS.forEach((loc) => {
      const isSource = loc.id === sourceNode;
      const isTarget = loc.id === targetNode;
      const isHighlighted = highlightedNodes.has(loc.id);

      let pinColor = '#38bdf8';
      if (loc.isHospital) pinColor = '#f43f5e';
      else if (isSource) pinColor = '#10b981';
      else if (isTarget) pinColor = '#f59e0b';
      else if (isHighlighted) pinColor = '#38bdf8';

      const customIcon = L.divIcon({
        className: 'custom-google-marker',
        html: `<div style="background:${pinColor}; width:16px; height:16px; border-radius:50%; border:2px solid #fff; box-shadow:0 0 12px ${pinColor};"></div>`,
        iconSize: [16, 16],
        iconAnchor: [8, 8],
      });

      const marker = L.marker([loc.lat, loc.lng], { icon: customIcon }).addTo(map);
      marker.bindTooltip(`<b>#${loc.id}: ${loc.name}</b><br>Zone: ${loc.zone}<br>GPS: ${loc.lat}, ${loc.lng}`, {
        permanent: false,
        direction: 'top',
      });

      marker.on('click', (e) => {
        if (e.originalEvent.shiftKey) {
          setTargetNode(loc.id);
        } else {
          setSourceNode(loc.id);
        }
      });

      markersRef.current.push(marker);
    });
  };

  const isEdgeInPath = (u, v) => {
    for (let i = 0; i < highlightedPath.length - 1; i++) {
      if (
        (highlightedPath[i] === u && highlightedPath[i + 1] === v) ||
        (highlightedPath[i] === v && highlightedPath[i + 1] === u)
      )
        return true;
    }
    return false;
  };

  const runDijkstra = (src, tgt) => {
    const adj = {};
    CAMPUS_LOCATIONS.forEach((l) => (adj[l.id] = []));
    RAW_ROADS.forEach(([id, u, v, weight]) => {
      adj[u].push({ node: v, weight });
      adj[v].push({ node: u, weight });
    });

    const dist = {};
    const prev = {};
    const visited = new Set();
    CAMPUS_LOCATIONS.forEach((l) => (dist[l.id] = Infinity));
    dist[src] = 0;

    while (visited.size < CAMPUS_LOCATIONS.length) {
      let u = null;
      let minD = Infinity;
      CAMPUS_LOCATIONS.forEach((l) => {
        if (!visited.has(l.id) && dist[l.id] < minD) {
          minD = dist[l.id];
          u = l.id;
        }
      });

      if (u === null || dist[u] === Infinity) break;
      visited.add(u);
      if (u === tgt) break;

      adj[u].forEach((edge) => {
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
    const nodesSet = new Set();
    let curr = tgt;
    while (curr !== undefined) {
      path.unshift(curr);
      nodesSet.add(curr);
      curr = prev[curr];
    }

    setHighlightedPath(path);
    setHighlightedNodes(nodesSet);

    const srcLoc = CAMPUS_LOCATIONS.find((l) => l.id === src);
    const tgtLoc = CAMPUS_LOCATIONS.find((l) => l.id === tgt);
    const haversineDist = calculateHaversineDistance(srcLoc.lat, srcLoc.lng, tgtLoc.lat, tgtLoc.lng);

    setRouteInfo({
      roadDistance: dist[tgt],
      haversineDistance: haversineDist,
      pathNames: path.map((id) => CAMPUS_LOCATIONS.find((l) => l.id === id).name),
      srcCoords: `${srcLoc.lat}, ${srcLoc.lng}`,
      tgtCoords: `${tgtLoc.lat}, ${tgtLoc.lng}`,
    });

    if (onLog) {
      onLog('DIJKSTRA', `Shortest path computed from ${srcLoc.name} to ${tgtLoc.name}: Road ${dist[tgt]}m, GPS Direct ${haversineDist}m`);
    }
  };

  // Canvas Fallback Renderer
  const drawCanvas = () => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    canvas.width = canvas.parentElement.clientWidth;
    canvas.height = canvas.parentElement.clientHeight;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.save();
    ctx.translate(panOffset.x, panOffset.y);
    ctx.scale(zoomScale, zoomScale);

    const scaleX = canvas.width / 800;
    const scaleY = canvas.height / 500;

    RAW_ROADS.forEach(([id, u, v]) => {
      const src = CAMPUS_LOCATIONS.find((l) => l.id === u);
      const dest = CAMPUS_LOCATIONS.find((l) => l.id === v);
      if (!src || !dest) return;

      const isPath = isEdgeInPath(u, v);
      ctx.beginPath();
      ctx.moveTo(src.x * scaleX, src.y * scaleY);
      ctx.lineTo(dest.x * scaleX, dest.y * scaleY);
      ctx.strokeStyle = isPath ? '#10b981' : '#1e293b';
      ctx.lineWidth = (isPath ? 4 : 1.5) / zoomScale;
      ctx.stroke();
    });

    CAMPUS_LOCATIONS.forEach((loc) => {
      const cx = loc.x * scaleX;
      const cy = loc.y * scaleY;
      const isSource = loc.id === sourceNode;
      const isTarget = loc.id === targetNode;
      const isHighlighted = highlightedNodes.has(loc.id);

      ctx.beginPath();
      ctx.arc(cx, cy, (loc.isHospital ? 10 : isSource || isTarget ? 8 : 5) / zoomScale, 0, 2 * Math.PI);
      ctx.fillStyle = isSource ? '#10b981' : isTarget ? '#f59e0b' : isHighlighted ? '#38bdf8' : '#38bdf8';
      ctx.fill();
      ctx.fillStyle = '#ffffff';
      ctx.font = '10px Inter';
      ctx.fillText(`${loc.name} (#${loc.id})`, cx + 8, cy + 3);
    });

    ctx.restore();
  };

  const zoomIn = () => {
    if (mapMode.startsWith('google') && mapInstanceRef.current) {
      mapInstanceRef.current.zoomIn();
    } else {
      setZoomScale((z) => Math.min(z * 1.25, 4.0));
    }
  };

  const zoomOut = () => {
    if (mapMode.startsWith('google') && mapInstanceRef.current) {
      mapInstanceRef.current.zoomOut();
    } else {
      setZoomScale((z) => Math.max(z / 1.25, 0.5));
    }
  };

  const centerMap = () => {
    setZoomScale(1.0);
    setPanOffset({ x: 0, y: 0 });
    if (mapMode.startsWith('google') && mapInstanceRef.current) {
      mapInstanceRef.current.setView([5.6505, -0.1862], 15);
    }
  };

  return (
    <div style={{ width: '100%' }}>
      {/* Control Panel */}
      <div className="card" style={{ marginBottom: '1rem' }}>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'center', flexWrap: 'wrap' }}>
          <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontWeight: 600 }}>SOURCE:</span>
            <select
              className="form-control"
              style={{ width: '220px' }}
              value={sourceNode}
              onChange={(e) => setSourceNode(Number(e.target.value))}
            >
              {CAMPUS_LOCATIONS.map((loc) => (
                <option key={loc.id} value={loc.id}>
                  #{loc.id}: {loc.name} ({loc.zone})
                </option>
              ))}
            </select>
          </div>

          <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
            <span style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontWeight: 600 }}>DESTINATION:</span>
            <select
              className="form-control"
              style={{ width: '220px' }}
              value={targetNode}
              onChange={(e) => setTargetNode(Number(e.target.value))}
            >
              {CAMPUS_LOCATIONS.map((loc) => (
                <option key={loc.id} value={loc.id}>
                  #{loc.id}: {loc.name} ({loc.zone})
                </option>
              ))}
            </select>
          </div>

          <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center', marginLeft: 'auto' }}>
            <Layers size={16} style={{ color: 'var(--accent-blue)' }} />
            <select
              className="form-control"
              style={{ width: '180px' }}
              value={mapMode}
              onChange={(e) => handleMapStyleChange(e.target.value)}
            >
              <option value="google-roadmap">Google Maps (Roads)</option>
              <option value="google-satellite">Google Maps (Satellite)</option>
              <option value="google-hybrid">Google Maps (Hybrid)</option>
              <option value="google-terrain">Google Maps (Terrain)</option>
              <option value="canvas">Custom Canvas</option>
            </select>
          </div>

          <button
            className="btn btn-primary"
            style={{ width: 'auto', padding: '0.6rem 1.2rem' }}
            onClick={() => runDijkstra(sourceNode, targetNode)}
          >
            <Navigation size={16} /> Run Dijkstra Route
          </button>
        </div>
      </div>

      {/* Google Maps / Canvas Viewport */}
      <div className="graph-viewport">
        <div className="map-toolbar">
          <button className="btn btn-secondary" onClick={zoomIn} title="Zoom In">
            <ZoomIn size={16} />
          </button>
          <button className="btn btn-secondary" onClick={zoomOut} title="Zoom Out">
            <ZoomOut size={16} />
          </button>
          <button className="btn btn-secondary" onClick={centerMap} title="Center Map">
            <RotateCcw size={16} />
          </button>
        </div>

        <div
          ref={mapContainerRef}
          style={{
            width: '100%',
            height: '100%',
            display: mapMode.startsWith('google') ? 'block' : 'none',
          }}
        />

        <canvas
          ref={canvasRef}
          style={{
            width: '100%',
            height: '100%',
            display: mapMode === 'canvas' ? 'block' : 'none',
          }}
        />
      </div>

      {/* Route Info Results */}
      {routeInfo && (
        <div className="card" style={{ marginTop: '1rem', borderColor: 'var(--accent-emerald)' }}>
          <div className="card-title" style={{ color: 'var(--accent-emerald)' }}>
            <span><Route size={18} inline style={{ marginRight: '6px' }} /> Google Maps Dijkstra Route Results</span>
            <span style={{ fontFamily: 'var(--font-mono)' }}>
              Road: <b>{routeInfo.roadDistance}m</b> | GPS Direct: <b>{routeInfo.haversineDistance}m</b>
            </span>
          </div>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-main)', lineHeight: '1.6' }}>
            <b>Optimal Traversal ({routeInfo.pathNames.length} nodes):</b> {routeInfo.pathNames.join(' ➔ ')}
          </p>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '0.5rem' }}>
            📍 <i>Google Maps GPS Coordinates:</i> ({routeInfo.srcCoords}) ➔ ({routeInfo.tgtCoords})
          </div>
        </div>
      )}
    </div>
  );
}
