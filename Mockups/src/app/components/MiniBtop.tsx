import { useState, useEffect } from 'react';

// Block character for bar fill (btop-style)
const BLOCK = '⣀';

function bar(percent: number, width: number = 20): string {
  const filled = Math.round((percent / 100) * width);
  return BLOCK.repeat(filled) + ' '.repeat(Math.max(0, width - filled));
}

// Simulated data — wobble slightly for mockup realism
function useBtopData() {
  const [tick, setTick] = useState(0);
  const [time, setTime] = useState(() =>
    new Date().toLocaleTimeString('en-GB', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' })
  );
  useEffect(() => {
    const id = setInterval(() => setTick((t) => t + 1), 2000);
    return () => clearInterval(id);
  }, []);
  useEffect(() => {
    const id = setInterval(() => {
      setTime(new Date().toLocaleTimeString('en-GB', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' }));
    }, 1000);
    return () => clearInterval(id);
  }, []);

  const wobble = (base: number, range: number) =>
    Math.min(100, Math.max(0, base + (tick % 5) * (range / 4) - range / 2));

  return {
    time,
    battery: 80,
    cpuTotal: wobble(15, 8),
    loadAvg: [3.59, 3.68, 3.59],
    cores: Array.from({ length: 6 }, (_, i) => wobble(12 + i * 2, 6)),
    memTotal: 18,
    memUsed: 7.48,
    memAvailable: 10.5,
    memCached: 3.36,
    memFree: 0.242,
    swapUsed: 23,
    proc: [
      { pid: 76180, name: 'Cursor Helper (R', mem: '1.4G', cpu: 4.7 },
      { pid: 1171, name: 'Firefox GPU Help', mem: '150M', cpu: 0 },
      { pid: 76175, name: 'Cursor Helper (G', mem: '115M', cpu: 0 },
      { pid: 1168, name: 'firefox', mem: '448M', cpu: 0 },
      { pid: 33185, name: 'Telegram', mem: '203M', cpu: 0.6 },
      { pid: 76171, name: 'Cursor', mem: '306M', cpu: 0 },
    ],
  };
}

export function MiniBtop() {
  const d = useBtopData();
  const memPct = Math.round((d.memUsed / d.memTotal) * 100);

  return (
    <div className="flex flex-col h-full min-h-0 bg-[#0d1117] text-[#e6edf3] font-mono overflow-hidden">
      {/* Title bar */}
      <div className="px-2 py-1 border-b border-[#30363d] text-[10px] text-[#8b949e] shrink-0 flex items-center justify-between">
        <span>btop — system</span>
        <span className="text-[#6e7681]">{d.time}  BAT▼ {d.battery}% ■■■■■■■■■■</span>
      </div>

      {/* Content — compact btop layout */}
      <div className="flex-1 min-h-0 overflow-auto p-1.5 text-[10px] leading-tight whitespace-pre">
        {/* Top: cpu box + mem box side by side */}
        <div className="flex gap-2 mb-1">
          {/* CPU panel */}
          <div className="flex-shrink-0 border border-[#30363d] rounded bg-[#161b22] overflow-hidden">
            <div className="px-1.5 py-0.5 border-b border-[#30363d] text-[#8b949e]">¹cpu  M3 Pro</div>
            <div className="px-1.5 py-0.5">
              <div className="text-[#8b949e]">CPU {bar(d.cpuTotal, 16)} {d.cpuTotal.toFixed(0)}%</div>
              {d.cores.slice(0, 4).map((pct, i) => (
                <div key={i} className="text-[#8b949e]">
                  C{i} {bar(pct, 12)} {pct.toFixed(0)}%
                </div>
              ))}
              <div className="text-[#6e7681] mt-0.5">Load: {d.loadAvg.join(' ')}</div>
            </div>
          </div>
          {/* Mem panel */}
          <div className="flex-1 min-w-0 border border-[#30363d] rounded bg-[#161b22] overflow-hidden">
            <div className="px-1.5 py-0.5 border-b border-[#30363d] text-[#8b949e]">²mem</div>
            <div className="px-1.5 py-0.5 space-y-0.5">
              <div>Total: {d.memTotal} GiB  Used: {d.memUsed} GiB  {memPct}%</div>
              <div className="text-[#8b949e]">{bar(memPct, 24)}</div>
              <div>Available: {d.memAvailable} GiB  Cached: {d.memCached} GiB</div>
              <div>Swap: {d.swapUsed}% used</div>
            </div>
          </div>
        </div>

        {/* Processes */}
        <div className="border border-[#30363d] rounded bg-[#161b22] overflow-hidden">
          <div className="px-1.5 py-0.5 border-b border-[#30363d] text-[#8b949e]">
            ⁴proc  Pid  Program           Mem    Cpu%
          </div>
          <div className="px-1.5 py-0.5">
            {d.proc.map((p, i) => (
              <div key={i} className="flex gap-2 truncate">
                <span className="w-6 text-[#8b949e]">{p.pid}</span>
                <span className="flex-1 min-w-0 truncate text-[#e6edf3]">{p.name}</span>
                <span className="text-[#8b949e]">{p.mem}</span>
                <span className="text-[#7ee787]">{p.cpu}%</span>
              </div>
            ))}
          </div>
        </div>

        {/* Bottom tab bar (btop style) */}
        <div className="mt-1 px-1.5 py-0.5 border border-[#30363d] rounded bg-[#161b22] text-[#6e7681] flex flex-wrap gap-x-2 gap-y-0">
          <span className="text-[#7ee787]">¹cpu</span>
          <span>²mem</span>
          <span>³net</span>
          <span>⁴proc</span>
          <span>filter</span>
          <span>per-core</span>
          <span>reverse</span>
          <span>tree</span>
        </div>
      </div>
    </div>
  );
}
