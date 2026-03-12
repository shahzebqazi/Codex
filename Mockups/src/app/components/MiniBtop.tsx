import { useState, useEffect, useRef } from 'react';

// Block character for bar fill (btop-style)
const BLOCK = '⣀';

function bar(percent: number, width: number = 20): string {
  const filled = Math.round((percent / 100) * width);
  return BLOCK.repeat(filled) + ' '.repeat(Math.max(0, width - filled));
}

const CPU_HISTORY_LEN = 32;

// Simulated data — wobble slightly for mockup realism
function useBtopData() {
  const [tick, setTick] = useState(0);
  const [time, setTime] = useState(() =>
    new Date().toLocaleTimeString('en-GB', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' })
  );
  const [cpuHistory, setCpuHistory] = useState<number[]>([]);

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
  const cpuTotal = wobble(15, 8);

  useEffect(() => {
    setCpuHistory((prev) => {
      const next = [...prev, cpuTotal].slice(-CPU_HISTORY_LEN);
      return next;
    });
  }, [cpuTotal]);

  return {
    time,
    battery: 80,
    cpuTotal,
    cpuHistory,
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

const PANEL_CLASS = 'border border-[#30363d] rounded bg-[#0d1117] overflow-hidden';

function BtopToolsRow({ cpuHistory }: { cpuHistory: number[] }) {
  const [calcDisplay, setCalcDisplay] = useState('0');
  const [calcPending, setCalcPending] = useState<string | null>(null);
  const [calcPrev, setCalcPrev] = useState<number | null>(null);

  const [timerSec, setTimerSec] = useState(0);
  const [timerRunning, setTimerRunning] = useState(false);
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const [color, setColor] = useState('#7ee787');

  useEffect(() => {
    if (!timerRunning) return;
    timerRef.current = setInterval(() => setTimerSec((s) => s + 1), 1000);
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [timerRunning]);

  const handleCalc = (key: string) => {
    if (key === 'C') {
      setCalcDisplay('0');
      setCalcPending(null);
      setCalcPrev(null);
      return;
    }
    if (key === '=' && calcPending && calcPrev !== null) {
      const a = calcPrev;
      const b = parseFloat(calcDisplay) || 0;
      let result = 0;
      if (calcPending === '+') result = a + b;
      if (calcPending === '-') result = a - b;
      if (calcPending === '×') result = a * b;
      if (calcPending === '÷') result = b === 0 ? 0 : a / b;
      setCalcDisplay(String(result));
      setCalcPending(null);
      setCalcPrev(null);
      return;
    }
    if (['+', '-', '×', '÷'].includes(key)) {
      setCalcPrev(parseFloat(calcDisplay) || 0);
      setCalcPending(key);
      setCalcDisplay('0');
      return;
    }
    if (key >= '0' && key <= '9' || key === '.') {
      setCalcDisplay((prev) => prev === '0' && key !== '.' ? key : prev + key);
    }
  };

  const hexFromColor = (s: string) => {
    if (/^#[0-9A-Fa-f]{6}$/.test(s)) return s;
    return '#7ee787';
  };

  return (
    <div className="grid grid-cols-2 gap-1.5 mt-1 flex-shrink-0">
      {/* CPU sparkline */}
      <div className={`${PANEL_CLASS} p-1`}>
        <div className="px-1 py-0.5 border-b border-[#30363d] text-[#79c0ff] bg-[#161b22]/80 text-[10px]">³cpu hist</div>
        <div className="flex items-end gap-px h-8 mt-0.5" style={{ minHeight: 32 }}>
          {cpuHistory.length === 0 ? (
            <span className="text-[#6e7681] text-[10px]">...</span>
          ) : (
            cpuHistory.map((pct, i) => (
              <div
                key={i}
                className="flex-1 min-w-0 rounded-sm bg-[#7ee787]/70 hover:bg-[#7ee787] transition-colors"
                style={{ height: `${Math.max(2, (pct / 100) * 100)}%` }}
                title={`${pct.toFixed(0)}%`}
              />
            ))
          )}
        </div>
      </div>

      {/* Mini calculator */}
      <div className={`${PANEL_CLASS} p-1`}>
        <div className="px-1 py-0.5 border-b border-[#30363d] text-[#f0883e] bg-[#161b22]/80 text-[10px]">calc</div>
        <div className="mt-0.5 font-mono text-[10px]">
          <div className="bg-[#161b22] border border-[#30363d] rounded px-1.5 py-0.5 text-right text-[#e6edf3] mb-1 min-h-[18px] truncate">{calcDisplay}</div>
          <div className="grid grid-cols-4 gap-0.5">
            {['7', '8', '9', '÷', '4', '5', '6', '×', '1', '2', '3', '-', 'C', '0', '.', '+'].map((k) => (
              <button
                key={k}
                type="button"
                onClick={() => handleCalc(k)}
                className="py-0.5 rounded bg-[#21262d] border border-[#30363d] text-[#e6edf3] hover:bg-[#30363d] text-[10px]"
              >
                {k}
              </button>
            ))}
            <button
              type="button"
              onClick={() => handleCalc('=')}
              className="col-span-4 py-0.5 rounded bg-[#238636] border border-[#2ea043] text-[#e6edf3] hover:bg-[#2ea043] text-[10px]"
            >
              =
            </button>
          </div>
        </div>
      </div>

      {/* Timer */}
      <div className={`${PANEL_CLASS} p-1`}>
        <div className="px-1 py-0.5 border-b border-[#30363d] text-[#a371f7] bg-[#161b22]/80 text-[10px]">timer</div>
        <div className="mt-0.5 flex items-center gap-1 flex-wrap">
          <span className="font-mono text-[#e6edf3] text-[12px] tabular-nums">
            {String(Math.floor(timerSec / 60)).padStart(2, '0')}:{String(timerSec % 60).padStart(2, '0')}
          </span>
          <button
            type="button"
            onClick={() => setTimerRunning((r) => !r)}
            className="px-1.5 py-0.5 rounded bg-[#21262d] border border-[#30363d] text-[#7ee787] hover:bg-[#30363d] text-[10px]"
          >
            {timerRunning ? 'Stop' : 'Start'}
          </button>
          <button
            type="button"
            onClick={() => { setTimerSec(0); setTimerRunning(false); }}
            className="px-1.5 py-0.5 rounded bg-[#21262d] border border-[#30363d] text-[#f0883e] hover:bg-[#30363d] text-[10px]"
          >
            Reset
          </button>
        </div>
      </div>

      {/* Color picker */}
      <div className={`${PANEL_CLASS} p-1`}>
        <div className="px-1 py-0.5 border-b border-[#30363d] text-[#58a6ff] bg-[#161b22]/80 text-[10px]">color</div>
        <div className="mt-0.5 flex items-center gap-1.5">
          <input
            type="color"
            value={hexFromColor(color)}
            onChange={(e) => setColor(e.target.value)}
            className="w-8 h-6 rounded border border-[#30363d] cursor-pointer bg-transparent"
            title="Pick color"
          />
          <span className="font-mono text-[10px] text-[#e6edf3] truncate">{hexFromColor(color)}</span>
          <div
            className="w-6 h-4 rounded border border-[#30363d] shrink-0"
            style={{ backgroundColor: hexFromColor(color) }}
            title="Swatch"
          />
        </div>
      </div>
    </div>
  );
}

export function MiniBtop() {
  const d = useBtopData();
  const memPct = Math.round((d.memUsed / d.memTotal) * 100);

  return (
    <div className="flex flex-col h-full min-h-0 bg-[#0d1117] text-[#e6edf3] font-mono overflow-hidden">
      {/* Title bar */}
      <div className="px-2 py-1 border-b border-[#30363d] text-[10px] shrink-0 flex items-center justify-between bg-[#161b22]">
        <span className="text-[#58a6ff]">btop</span>
        <span className="text-[#8b949e]">— system</span>
        <span className="text-[#f0883e]">{d.time}</span>
        <span className="text-[#7ee787]">BAT▼ {d.battery}%</span>
        <span className="text-[#a371f7]">■■■■■■■■■■</span>
      </div>

      {/* Content — fills entire Diagnostics area, btop layout with color */}
      <div className="flex-1 min-h-0 overflow-auto p-1.5 text-[10px] leading-tight whitespace-pre flex flex-col">
        {/* Top: cpu box + mem box side by side */}
        <div className="flex gap-2 mb-1 flex-shrink-0">
          {/* CPU panel */}
          <div className="flex-shrink-0 border border-[#388bfd] rounded bg-[#0d1117] overflow-hidden">
            <div className="px-1.5 py-0.5 border-b border-[#30363d] text-[#58a6ff] bg-[#161b22]">¹cpu  M3 Pro</div>
            <div className="px-1.5 py-0.5">
              <div className="text-[#7ee787]">CPU {bar(d.cpuTotal, 16)} <span className="text-[#f0883e]">{d.cpuTotal.toFixed(0)}%</span></div>
              {d.cores.slice(0, 4).map((pct, i) => (
                <div key={i} className="text-[#8b949e]">
                  <span className="text-[#58a6ff]">C{i}</span> {bar(pct, 12)} <span className="text-[#a371f7]">{pct.toFixed(0)}%</span>
                </div>
              ))}
              <div className="text-[#6e7681] mt-0.5">Load: <span className="text-[#f0883e]">{d.loadAvg.join(' ')}</span></div>
            </div>
          </div>
          {/* Mem panel */}
          <div className="flex-1 min-w-0 border border-[#388bfd] rounded bg-[#0d1117] overflow-hidden">
            <div className="px-1.5 py-0.5 border-b border-[#30363d] text-[#79c0ff] bg-[#161b22]">²mem</div>
            <div className="px-1.5 py-0.5 space-y-0.5">
              <div><span className="text-[#8b949e]">Total:</span> <span className="text-[#e6edf3]">{d.memTotal} GiB</span>  <span className="text-[#7ee787]">Used: {d.memUsed} GiB</span>  <span className="text-[#f0883e]">{memPct}%</span></div>
              <div className="text-[#79c0ff]">{bar(memPct, 24)}</div>
              <div><span className="text-[#8b949e]">Available:</span> <span className="text-[#7ee787]">{d.memAvailable} GiB</span>  <span className="text-[#a371f7]">Cached: {d.memCached} GiB</span></div>
              <div><span className="text-[#8b949e]">Swap:</span> <span className="text-[#f0883e]">{d.swapUsed}%</span> used</div>
            </div>
          </div>
        </div>

        {/* Processes — fixed height, scroll if needed */}
        <div className="flex-shrink-0 flex flex-col border border-[#a371f7] rounded bg-[#0d1117] overflow-hidden max-h-[120px]">
          <div className="px-1.5 py-0.5 border-b border-[#30363d] text-[#a371f7] bg-[#161b22] flex-shrink-0">
            ⁴proc  <span className="text-[#8b949e]">Pid  Program           Mem    Cpu%</span>
          </div>
          <div className="px-1.5 py-0.5 overflow-auto flex-1 min-h-0">
            {d.proc.map((p, i) => (
              <div key={i} className="flex gap-2 truncate">
                <span className="w-6 text-[#58a6ff]">{p.pid}</span>
                <span className="flex-1 min-w-0 truncate text-[#e6edf3]">{p.name}</span>
                <span className="text-[#79c0ff]">{p.mem}</span>
                <span className="text-[#7ee787]">{p.cpu}%</span>
              </div>
            ))}
          </div>
        </div>

        {/* Tools row: sparkline, calculator, timer, color picker */}
        <BtopToolsRow cpuHistory={d.cpuHistory} />

        {/* Bottom tab bar (btop style) */}
        <div className="mt-1 px-1.5 py-0.5 border border-[#30363d] rounded bg-[#161b22] text-[#6e7681] flex flex-wrap gap-x-2 gap-y-0 flex-shrink-0">
          <span className="text-[#7ee787]">¹cpu</span>
          <span className="text-[#79c0ff]">²mem</span>
          <span>³net</span>
          <span className="text-[#a371f7]">⁴proc</span>
          <span>filter</span>
          <span>per-core</span>
          <span>reverse</span>
          <span>tree</span>
        </div>
      </div>
    </div>
  );
}
