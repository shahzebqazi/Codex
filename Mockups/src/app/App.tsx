import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ChevronLeft, ChevronRight, Menu, Settings, User, Paperclip, Mic, Target, ChevronDown, Circle, PanelLeft, PanelRight, PanelBottom, X, Terminal, FileCode, Wrench, Cpu, Zap, Shield, Database, Code2, Layers, Activity, Bug, GitCommit, FolderOpen, File, Files, Puzzle, GitBranch, Search, ChevronRight as ChevronRightIcon, Folder, MessageCircle, Network, Globe, Image as ImageIcon, LayoutList, Bot, MoreVertical } from 'lucide-react';
import { ThreadGraph3D, type ThreadNode, type ThreadLink } from './components/ThreadGraph3D';

// Koi pond background (Unsplash, free to use) — animated with light blur for mockup
const KOI_POND_BG = 'https://images.unsplash.com/photo-1582794543139-8ac9cb0f7b11?w=1920&q=80';

// 1440p laptop: 2560×1440 logical pixels (mimics QHD high-DPI laptop screen)
const MOCKUP_WIDTH = 2560;
const MOCKUP_HEIGHT = 1440;
const MOCKUP_PADDING = 128;

type AppTab = 'home' | 'settings' | 'mods';

export default function App() {
  const [activeTab, setActiveTab] = useState<AppTab>('home');
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [scale, setScale] = useState(1);

  useEffect(() => {
    const updateScale = () => {
      const s = Math.min(
        (window.innerWidth - MOCKUP_PADDING) / MOCKUP_WIDTH,
        (window.innerHeight - MOCKUP_PADDING) / MOCKUP_HEIGHT,
        1
      );
      setScale(Math.max(0.2, s));
    };
    updateScale();
    window.addEventListener('resize', updateScale);
    return () => window.removeEventListener('resize', updateScale);
  }, []);

  return (
    <div className="min-h-screen overflow-hidden relative">
      {/* Animated koi pond background (home tab only); black fallback */}
      <div className="absolute inset-0 overflow-hidden bg-black">
        {activeTab === 'home' && (
          <div
            className="absolute inset-0 bg-cover bg-center animate-koi-drift"
            style={{
              backgroundImage: `url(${KOI_POND_BG})`,
              filter: 'blur(4px) saturate(1.2) contrast(0.95) brightness(0.7)',
            }}
          />
        )}
      </div>

      {/* Floating mockup window — fixed 1440p size; scale only from resize, not from content */}
      <div className="relative min-h-screen flex items-center justify-center p-8">
        <div
          className="origin-center"
          style={{
            width: MOCKUP_WIDTH,
            height: MOCKUP_HEIGHT,
            minWidth: MOCKUP_WIDTH,
            minHeight: MOCKUP_HEIGHT,
            flex: '0 0 auto',
            transform: `scale(${scale})`,
          }}
        >
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="rounded-xl overflow-hidden shadow-2xl border border-[#333333]/80 bg-[#0a0a0a] flex flex-col"
            style={{ width: MOCKUP_WIDTH, height: MOCKUP_HEIGHT }}
          >
            {/* Single top bar: macOS window controls + tabs (Home | Settings | Mods) */}
            <div className="flex items-center gap-2 border-b border-[#1A1A1A] bg-[#0A0A0A] px-2 py-1.5 shrink-0 min-h-[36px]">
              <div className="flex items-center gap-1.5 shrink-0 pointer-events-none">
                <div className="w-3 h-3 rounded-full bg-[#FF5F57]" />
                <div className="w-3 h-3 rounded-full bg-[#FEBC2E]" />
                <div className="w-3 h-3 rounded-full bg-[#28C840]" />
              </div>
              <div className="flex items-center gap-0.5">
                <button onClick={() => setActiveTab('home')} className={`px-3 py-2 rounded-t text-sm font-mono ${activeTab === 'home' ? 'bg-[#0a0a0a] text-[#E5E5E5] border border-[#1A1A1A] border-b-transparent -mb-px' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Home</button>
                <button onClick={() => setActiveTab('settings')} className={`px-3 py-2 rounded-t text-sm font-mono ${activeTab === 'settings' ? 'bg-[#0a0a0a] text-[#E5E5E5] border border-[#1A1A1A] border-b-transparent -mb-px' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Settings</button>
                <button onClick={() => setActiveTab('mods')} className={`px-3 py-2 rounded-t text-sm font-mono ${activeTab === 'mods' ? 'bg-[#0a0a0a] text-[#E5E5E5] border border-[#1A1A1A] border-b-transparent -mb-px' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Mods</button>
              </div>
            </div>
            <div className="flex-1 min-h-0 overflow-hidden">
              {activeTab === 'home' && (
                <MainChatView
                  sidebarOpen={sidebarOpen}
                  setSidebarOpen={setSidebarOpen}
                  onOpenSettings={() => setActiveTab('settings')}
                />
              )}
              {activeTab === 'settings' && <SettingsView onBack={() => setActiveTab('home')} />}
              {activeTab === 'mods' && <ModsView />}
            </div>
          </motion.div>
        </div>
      </div>
    </div>
  );
}

// Project management mock data
const MOCK_TASKS = [
  { id: '1', title: 'Implement auth flow', status: 'in_progress' as const, assignee: 'Dev', due: 'Mar 14' },
  { id: '2', title: 'API rate limiting', status: 'todo' as const, assignee: 'Backend', due: 'Mar 18' },
  { id: '3', title: 'Dashboard redesign', status: 'done' as const, assignee: 'Design', due: 'Mar 10' },
  { id: '4', title: 'E2E tests for checkout', status: 'in_progress' as const, assignee: 'QA', due: 'Mar 16' },
  { id: '5', title: 'Document API v2', status: 'todo' as const, assignee: 'Dev', due: 'Mar 20' },
  { id: '6', title: 'Fix mobile nav', status: 'todo' as const, assignee: 'Frontend', due: 'Mar 15' },
];
const MOCK_CALENDAR_EVENTS = [
  { id: 'c1', title: 'Sprint planning', date: 'Mar 12', time: '10:00' },
  { id: 'c2', title: 'API review', date: 'Mar 12', time: '14:00' },
  { id: 'c3', title: 'Release cut-off', date: 'Mar 15', time: '17:00' },
  { id: 'c4', title: 'Retro', date: 'Mar 16', time: '11:00' },
];

type MainViewMode = 'list' | 'kanban' | 'calendar' | 'graph';

// Mock thread graph structures (nodes = messages/turns, links = reply flow) for 3D view
const MOCK_THREAD_GRAPHS: Record<string, { nodes: ThreadNode[]; links: ThreadLink[] }> = {
  general: {
    nodes: [
      { id: '1', name: 'User: Can you draft a jj commit?', role: 'user' },
      { id: '2', name: 'dotAi: Here is the commit...', role: 'assistant' },
      { id: '3', name: 'User: Add docs to scope', role: 'user' },
      { id: '4', name: 'dotAi: Updated commit...', role: 'assistant' },
      { id: '5', name: 'User: Confirm', role: 'user' },
      { id: '6', name: 'System: Commit created', role: 'system' },
    ],
    links: [
      { source: '1', target: '2' },
      { source: '2', target: '3' },
      { source: '3', target: '4' },
      { source: '4', target: '5' },
      { source: '5', target: '6' },
    ],
  },
  'MVP PRD': {
    nodes: [
      { id: 'a', name: 'User: Outline MVP PRD', role: 'user' },
      { id: 'b', name: 'dotAi: PRD structure...', role: 'assistant' },
      { id: 'c', name: 'User: Add auth section', role: 'user' },
      { id: 'd', name: 'dotAi: Auth requirements...', role: 'assistant' },
      { id: 'e', name: 'User: LGTM', role: 'user' },
    ],
    links: [
      { source: 'a', target: 'b' },
      { source: 'b', target: 'c' },
      { source: 'c', target: 'd' },
      { source: 'd', target: 'e' },
    ],
  },
  docs: {
    nodes: [
      { id: 'x', name: 'User: Generate docs', role: 'user' },
      { id: 'y', name: 'dotAi: Documentation plan...', role: 'assistant' },
      { id: 'z', name: 'User: Add API section', role: 'user' },
    ],
    links: [
      { source: 'x', target: 'y' },
      { source: 'y', target: 'z' },
    ],
  },
};

function MainChatView(
  { sidebarOpen, setSidebarOpen, onOpenSettings }: { sidebarOpen: boolean; setSidebarOpen: (open: boolean) => void; onOpenSettings: () => void }
) {
  const [leftSidebarOpen, setLeftSidebarOpen] = useState(true);
  const [rightSidebarOpen, setRightSidebarOpen] = useState(true);
  const [activeSidebarTab, setActiveSidebarTab] = useState<'files' | 'search' | 'git' | 'extensions'>('files');
  const [mainViewMode, setMainViewMode] = useState<MainViewMode>('list');
  const [selectedThreadId, setSelectedThreadId] = useState<string>('general');
  const [terminalOpen, setTerminalOpen] = useState(false);
  const [expandedFolders, setExpandedFolders] = useState<string[]>(['Orchestration']);

  const threadGraphData = MOCK_THREAD_GRAPHS[selectedThreadId] ?? MOCK_THREAD_GRAPHS.general;

  const toggleFolder = (folder: string) => {
    setExpandedFolders(prev =>
      prev.includes(folder) ? prev.filter(f => f !== folder) : [...prev, folder]
    );
  };

  return (
    <div className="bg-[#0a0a0a] overflow-hidden flex flex-col h-full min-h-0">
      {/* Main Content Area: left sidebar | center | right sidebar — fills 1440p frame (single top bar is in App) */}
      <div className="flex flex-col flex-1 min-h-0">
        <div className="flex flex-1 min-h-0">
          {/* Left Sidebar - Explorer, Search, Git, Extensions (toggle via status bar) */}
          <motion.div
            initial={false}
            animate={{ width: leftSidebarOpen ? 260 : 0 }}
            className="bg-[#000000] border-r border-[#1A1A1A] overflow-hidden flex flex-col shrink-0"
          >
            <div className="w-[260px] h-full flex flex-col min-w-0">
              <div className="flex items-center justify-between gap-2 p-2 border-b border-[#1A1A1A] bg-[#0A0A0A] shrink-0">
                <span className="text-xs font-mono text-[#666666] uppercase tracking-wide">Explorer</span>
                <button onClick={() => setLeftSidebarOpen(false)} className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]" title="Hide sidebar">
                  <PanelLeft className="w-4 h-4" />
                </button>
              </div>
              <div className="flex items-center gap-0.5 px-2 py-1 border-b border-[#1A1A1A] bg-[#0A0A0A]/50">
                <button onClick={() => setActiveSidebarTab('files')} className={`p-2 rounded transition-colors ${activeSidebarTab === 'files' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`} title="Files"><Folder className="w-4 h-4" /></button>
                <button onClick={() => setActiveSidebarTab('search')} className={`p-2 rounded transition-colors ${activeSidebarTab === 'search' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`} title="Search"><Search className="w-4 h-4" /></button>
                <button onClick={() => setActiveSidebarTab('git')} className={`p-2 rounded transition-colors ${activeSidebarTab === 'git' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`} title="Source Control"><GitBranch className="w-4 h-4" /></button>
                <button onClick={() => setActiveSidebarTab('extensions')} className={`p-2 rounded transition-colors ${activeSidebarTab === 'extensions' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`} title="Extensions"><Puzzle className="w-4 h-4" /></button>
              </div>
              <div className="flex-1 overflow-y-auto p-2">
              {activeSidebarTab === 'files' && (
                <div className="space-y-0.5">
                  <div>
                    <button
                      onClick={() => toggleFolder('Orchestration')}
                      className="w-full flex items-center gap-1 px-2 py-1 hover:bg-[#1A1A1A] rounded text-sm transition-colors"
                    >
                      <ChevronRightIcon className={`w-3 h-3 transition-transform ${expandedFolders.includes('Orchestration') ? 'rotate-90' : ''}`} />
                      <Folder className="w-4 h-4 text-[#E5E5E5]" />
                      <span className="text-[#E5E5E5]">Orchestration</span>
                    </button>
                    {expandedFolders.includes('Orchestration') && (
                      <div className="ml-4 space-y-0.5">
                        <div className="flex items-center gap-1 px-2 py-1 hover:bg-[#1A1A1A] rounded text-sm cursor-pointer transition-colors">
                          <Folder className="w-4 h-4 text-[#E5E5E5]" />
                          <span className="text-[#E5E5E5]">Agents</span>
                        </div>
                        <div className="flex items-center gap-1 px-2 py-1 hover:bg-[#1A1A1A] rounded text-sm cursor-pointer transition-colors">
                          <Folder className="w-4 h-4 text-[#E5E5E5]" />
                          <span className="text-[#E5E5E5]">Memories</span>
                        </div>
                        <div className="flex items-center gap-1 px-2 py-1 hover:bg-[#1A1A1A] rounded text-sm cursor-pointer bg-[#E5E5E5]/5 transition-colors">
                          <FileCode className="w-4 h-4 text-[#F59E0B]" />
                          <span className="text-[#E5E5E5]">MVP_PRD.md</span>
                        </div>
                        <div className="flex items-center gap-1 px-2 py-1 hover:bg-[#1A1A1A] rounded text-sm cursor-pointer transition-colors">
                          <FileCode className="w-4 h-4 text-[#F59E0B]" />
                          <span className="text-[#E5E5E5]">CHATBOT.md</span>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              )}

              {activeSidebarTab === 'git' && (
                <div className="space-y-4">
                  <div>
                    <div className="text-xs text-[#666666] mb-2 font-mono">CHANGES</div>
                    <div className="space-y-1 text-sm">
                      <div className="flex items-center gap-2 px-2 py-1 hover:bg-[#1A1A1A] rounded cursor-pointer transition-colors">
                        <span className="text-[#10B981]">M</span>
                        <span className="text-[#E5E5E5]">MVP_PRD.md</span>
                      </div>
                      <div className="flex items-center gap-2 px-2 py-1 hover:bg-[#1A1A1A] rounded cursor-pointer transition-colors">
                        <span className="text-[#F59E0B]">A</span>
                        <span className="text-[#E5E5E5]">FEATURES.md</span>
                      </div>
                    </div>
                  </div>
                </div>
              )}

              {activeSidebarTab === 'extensions' && (
                <div className="space-y-4 p-2">
                  <div>
                    <div className="flex items-center gap-1.5 px-2 py-1 text-xs font-mono text-[#0EA5E9] uppercase tracking-wide">
                      <LayoutList className="w-3.5 h-3.5" /> Planner
                    </div>
                    <div className="mt-1 space-y-1">
                      <div className="px-2 py-1.5 rounded text-sm text-[#E5E5E5] bg-[#1A1A1A]/50">Task planning</div>
                      <div className="px-2 py-1.5 rounded text-sm text-[#666666]">Roadmap view</div>
                    </div>
                  </div>
                  <div>
                    <div className="flex items-center gap-1.5 px-2 py-1 text-xs font-mono text-[#10B981] uppercase tracking-wide">
                      <Bot className="w-3.5 h-3.5" /> AI Agents
                    </div>
                    <div className="mt-1 space-y-1">
                      <div className="px-2 py-1.5 rounded text-sm flex items-center gap-2">
                        <Zap className="w-4 h-4 text-[#10B981]" />
                        <span className="text-[#E5E5E5]">SWE Developer</span>
                      </div>
                      <div className="px-2 py-1.5 rounded text-sm text-[#666666]">Orchestrator</div>
                    </div>
                  </div>
                  <div>
                    <div className="flex items-center gap-1.5 px-2 py-1 text-xs font-mono text-[#8B5CF6] uppercase tracking-wide">
                      <MessageCircle className="w-3.5 h-3.5" /> AI Chat bots
                    </div>
                    <div className="mt-1 space-y-1">
                      <div className="px-2 py-1.5 rounded text-sm flex items-center gap-2 bg-[#E5E5E5]/5">
                        <MessageCircle className="w-4 h-4 text-[#8B5CF6]" />
                        <span className="text-[#E5E5E5]">Standard Chatbot</span>
                      </div>
                      <div className="px-2 py-1.5 rounded text-sm text-[#666666]">Q&A assistant</div>
                    </div>
                  </div>
                  <div>
                    <div className="flex items-center gap-1.5 px-2 py-1 text-xs font-mono text-[#F59E0B] uppercase tracking-wide">
                      <Bug className="w-3.5 h-3.5" /> AI Debuggers
                    </div>
                    <div className="mt-1 space-y-1">
                      <div className="px-2 py-1.5 rounded text-sm flex items-center gap-2">
                        <Bug className="w-4 h-4 text-[#F59E0B]" />
                        <span className="text-[#E5E5E5]">Tech Lead / Reviewer</span>
                      </div>
                      <div className="px-2 py-1.5 rounded text-sm text-[#666666]">Trace analyzer</div>
                    </div>
                  </div>
                </div>
              )}
              </div>
            </div>
          </motion.div>

          {/* Center: half (main) + quarter/quarter (right column, two panes stacked) */}
          <div className="flex-1 flex min-w-0 bg-[#0a0a0a] relative">
            {/* Major section: 1/2 width — list / kanban / calendar / graph */}
            <div className="w-1/2 min-w-0 flex flex-col border-r border-[#1A1A1A] bg-[#0a0a0a] shrink-0">
              <div className="flex items-center gap-1 px-2 py-1.5 border-b border-[#1A1A1A] shrink-0 bg-[#0a0a0a]">
                <button onClick={() => setMainViewMode('list')} className={`px-2 py-1 rounded text-xs font-mono ${mainViewMode === 'list' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>List</button>
                <button onClick={() => setMainViewMode('kanban')} className={`px-2 py-1 rounded text-xs font-mono ${mainViewMode === 'kanban' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Kanban</button>
                <button onClick={() => setMainViewMode('calendar')} className={`px-2 py-1 rounded text-xs font-mono ${mainViewMode === 'calendar' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Calendar</button>
                <button onClick={() => setMainViewMode('graph')} className={`px-2 py-1 rounded text-xs font-mono flex items-center gap-1 ${mainViewMode === 'graph' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`} title="3D thread graph"><Network className="w-3.5 h-3.5" /> Graph</button>
              </div>
              <div className="flex-1 overflow-auto p-3 min-h-0 bg-[#0a0a0a]">
                {mainViewMode === 'list' && (
                  <ul className="space-y-1 font-mono text-sm">
                    {MOCK_TASKS.map(t => (
                      <li key={t.id} className="flex items-center gap-3 px-2 py-2 rounded bg-[#0A0A0A] border border-[#1A1A1A]">
                        <span className="text-[#666666] w-6">{t.id}</span>
                        <span className="text-[#E5E5E5] flex-1">{t.title}</span>
                        <span className="text-[#10B981] text-xs">{t.status}</span>
                        <span className="text-[#666666] text-xs">{t.assignee}</span>
                        <span className="text-[#666666] text-xs">{t.due}</span>
                      </li>
                    ))}
                  </ul>
                )}
                {mainViewMode === 'kanban' && (
                  <div className="flex gap-3 h-full min-h-[200px]">
                    {(['todo', 'in_progress', 'done'] as const).map(col => (
                      <div key={col} className="flex-1 min-w-[140px] rounded bg-[#0A0A0A] border border-[#1A1A1A] p-2">
                        <div className="text-xs font-mono text-[#666666] uppercase mb-2">{col.replace('_', ' ')}</div>
                        <div className="space-y-2">
                          {MOCK_TASKS.filter(t => t.status === col).map(t => (
                            <div key={t.id} className="p-2 rounded bg-[#161b22] border border-[#30363d] text-sm text-[#E5E5E5]">{t.title}</div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
                {mainViewMode === 'calendar' && (
                  <div className="grid grid-cols-7 gap-1 font-mono text-sm">
                    {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map(d => (
                      <div key={d} className="text-[#666666] text-xs p-1">{d}</div>
                    ))}
                    {Array.from({ length: 31 }, (_, i) => {
                      const day = i + 1;
                      const events = MOCK_CALENDAR_EVENTS.filter(e => e.date === `Mar ${day}`);
                      return (
                        <div key={i} className="min-h-[48px] p-1 rounded bg-[#0A0A0A] border border-[#1A1A1A]">
                          <span className="text-[#666666]">{day}</span>
                          {events.slice(0, 2).map(ev => (
                            <div key={ev.id} className="text-[10px] text-[#0EA5E9] truncate mt-0.5">{ev.title}</div>
                          ))}
                        </div>
                      );
                    })}
                  </div>
                )}
                {mainViewMode === 'graph' && (
                  <div className="flex flex-col h-full min-h-0 p-3 gap-2">
                    <div className="text-[10px] font-mono text-[#8b949e] shrink-0">Thread: #{selectedThreadId} — drag to rotate, scroll to zoom</div>
                    <div className="flex-1 min-h-[320px] flex items-center justify-center">
                      <ThreadGraph3D
                        nodes={threadGraphData.nodes}
                        links={threadGraphData.links}
                        width={620}
                        height={360}
                        backgroundColor="#0d1117"
                      />
                    </div>
                  </div>
                )}
              </div>
            </div>

            {/* Right column: AI chat pane (top) + btop diagnostics (bottom) */}
            <div className="w-[280px] flex flex-col shrink-0 border-l border-[#1A1A1A] bg-[#0a0a0a]">
              <div className="border-b border-[#1A1A1A] p-2 shrink-0 bg-[#0a0a0a]">
                <input type="text" placeholder="Ask dotAi..." className="w-full bg-[#0A0A0A] border border-[#1A1A1A] rounded px-2 py-2 text-xs text-[#E5E5E5] placeholder:text-[#666666] outline-none focus:border-[#333333]" />
              </div>
              <div className="flex-1 min-h-0 overflow-hidden flex flex-col bg-[#0d1117]">
                <div className="px-2 py-1 border-b border-[#30363d] text-[10px] font-mono text-[#8b949e]">btop — system</div>
                <div className="flex-1 overflow-auto p-2 font-mono text-[10px] text-[#7ee787] space-y-0.5">
                  <div className="flex justify-between text-[#8b949e]"><span>CPU</span><span>12%</span></div>
                  <div className="h-1.5 bg-[#21262d] rounded overflow-hidden"><div className="h-full bg-[#10B981] rounded" style={{ width: '12%' }} /></div>
                  <div className="flex justify-between text-[#8b949e]"><span>Mem</span><span>4.2G / 16G</span></div>
                  <div className="h-1.5 bg-[#21262d] rounded overflow-hidden"><div className="h-full bg-[#0EA5E9] rounded" style={{ width: '26%' }} /></div>
                  <div className="flex justify-between text-[#8b949e]"><span>Tasks</span><span>142</span></div>
                  <div className="text-[#e6edf3] mt-1">node (8) · vite (2) · zsh (1)</div>
                </div>
              </div>
            </div>
          </div>

          {/* Right Sidebar - Threads (toggle via status bar) */}
          <motion.div
            initial={false}
            animate={{ width: rightSidebarOpen ? 220 : 0 }}
            className="bg-[#000000] border-l border-[#1A1A1A] overflow-hidden flex flex-col shrink-0"
          >
            <div className="w-[220px] h-full flex flex-col min-w-0">
              <div className="flex items-center justify-between gap-2 p-2 border-b border-[#1A1A1A] bg-[#0A0A0A] shrink-0">
                <span className="text-xs font-mono text-[#666666] uppercase tracking-wide">Threads</span>
                <button onClick={() => setRightSidebarOpen(false)} className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]" title="Hide sidebar">
                  <PanelRight className="w-4 h-4 rotate-180" />
                </button>
              </div>
              <div className="flex-1 overflow-y-auto p-2">
                <div className="space-y-0.5">
                  {(['general', 'MVP PRD', 'docs'] as const).map((tid) => (
                    <button
                      key={tid}
                      onClick={() => setSelectedThreadId(tid)}
                      className={`w-full text-left px-2 py-1.5 rounded text-sm cursor-pointer transition-colors ${selectedThreadId === tid ? 'text-[#E5E5E5] bg-[#E5E5E5]/10' : 'text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`}
                    >
                      #{tid}
                    </button>
                  ))}
                </div>
              </div>
            </div>
          </motion.div>
        </div>

        {/* Status bar: sidebar icons, SSH/JJ status, terminal */}
        <div className="flex items-center gap-0.5 px-2 py-1 border-t border-[#1A1A1A] bg-[#0A0A0A] shrink-0 min-h-[28px] text-[#666666]">
          <button onClick={() => { setLeftSidebarOpen(true); setActiveSidebarTab('files'); }} className={`p-1.5 rounded ${leftSidebarOpen && activeSidebarTab === 'files' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`} title="Explorer"><Folder className="w-3.5 h-3.5" /></button>
          <button onClick={() => { setLeftSidebarOpen(true); setActiveSidebarTab('search'); }} className={`p-1.5 rounded ${!leftSidebarOpen ? 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]' : activeSidebarTab === 'search' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:text-[#E5E5E5]'}`} title="Search"><Search className="w-3.5 h-3.5" /></button>
          <button onClick={() => { setLeftSidebarOpen(true); setActiveSidebarTab('git'); }} className={`p-1.5 rounded ${!leftSidebarOpen ? 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]' : activeSidebarTab === 'git' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:text-[#E5E5E5]'}`} title="Git"><GitBranch className="w-3.5 h-3.5" /></button>
          <button onClick={() => { setLeftSidebarOpen(true); setActiveSidebarTab('extensions'); }} className={`p-1.5 rounded ${!leftSidebarOpen ? 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]' : activeSidebarTab === 'extensions' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:text-[#E5E5E5]'}`} title="Extensions"><Puzzle className="w-3.5 h-3.5" /></button>
          <div className="w-px h-4 bg-[#333333] mx-0.5" />
          <button onClick={() => setRightSidebarOpen(!rightSidebarOpen)} className={`p-1.5 rounded ${rightSidebarOpen ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`} title="Threads"><MessageCircle className="w-3.5 h-3.5" /></button>
          <div className="w-px h-4 bg-[#333333] mx-0.5" />
          <span className="text-[10px] font-mono text-[#8b949e] px-1.5 py-0.5 rounded bg-[#1A1A1A]/80" title="SSH">SSH: dev@host</span>
          <span className="text-[10px] font-mono text-[#8b949e] px-1.5 py-0.5 rounded bg-[#1A1A1A]/80" title="JJ">JJ: main 3↑</span>
          <div className="flex-1" />
          <button onClick={() => setTerminalOpen(true)} className="p-1.5 rounded hover:bg-[#1A1A1A] hover:text-[#10B981] text-[#10B981]" title="Terminal"><Terminal className="w-3.5 h-3.5" /></button>
        </div>
      </div>

      {/* Slide-up terminal (opens from bottom) */}
      <AnimatePresence>
        {terminalOpen && (
          <motion.div
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: '40%', opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            transition={{ type: 'tween', duration: 0.2 }}
            className="absolute bottom-0 left-0 right-0 border-t border-[#1A1A1A] bg-[#0d1117] flex flex-col overflow-hidden z-50"
            style={{ minHeight: 0 }}
          >
            <div className="flex items-center justify-between px-2 py-1.5 border-b border-[#30363d] shrink-0 bg-[#161b22]">
              <span className="text-xs font-mono text-[#8b949e]">xonsh — Terminal</span>
              <button onClick={() => setTerminalOpen(false)} className="p-1 text-[#8b949e] hover:text-[#e6edf3] rounded"><X className="w-4 h-4" /></button>
            </div>
            <div className="flex-1 overflow-auto p-3 font-mono text-sm min-h-0">
              <pre className="text-[#7ee787]">$ <span className="text-[#e6edf3]">_</span></pre>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}

function ChatPaneMock() {
  return (
    <div className="space-y-3">
      <div className="bg-[#0d1117] rounded border border-[#30363d] p-3 font-mono text-sm text-[#e6edf3]">
        Ask dotAi anything...
      </div>
      <div className="text-xs text-[#8b949e] font-mono">New chat thread (mock)</div>
    </div>
  );
}

function XonshPaneMock() {
  return (
    <div className="bg-[#0d1117] rounded border border-[#30363d] font-mono text-sm overflow-hidden">
      <div className="px-3 py-1.5 border-b border-[#30363d] text-[#8b949e]">xonsh</div>
      <pre className="p-3 text-[#7ee787]">$ <span className="text-[#e6edf3]">_</span></pre>
    </div>
  );
}

function FilePaneMock() {
  return (
    <div className="bg-[#0d1117] rounded border border-[#30363d] font-mono text-sm overflow-hidden">
      <div className="px-3 py-1.5 border-b border-[#30363d] text-[#8b949e]">file.md — NORMAL</div>
      <pre className="p-3 text-[#e6edf3] whitespace-pre">1  │</pre>
    </div>
  );
}

function BrowserPaneMock() {
  return (
    <div className="bg-[#0d1117] rounded border border-[#30363d] overflow-hidden">
      <div className="flex items-center gap-2 px-3 py-2 border-b border-[#30363d] bg-[#161b22]">
        <div className="flex gap-1.5">
          <div className="w-2.5 h-2.5 rounded-full bg-[#ff5f57]" /><div className="w-2.5 h-2.5 rounded-full bg-[#febc2e]" /><div className="w-2.5 h-2.5 rounded-full bg-[#28c840]" />
        </div>
        <div className="flex-1 rounded bg-[#0d1117] px-2 py-1 text-xs text-[#8b949e]">https://</div>
      </div>
      <div className="p-4 text-center text-[#8b949e] text-sm">Browser preview (mock)</div>
    </div>
  );
}

function ImagePaneMock() {
  return (
    <div className="bg-[#0d1117] rounded border border-[#30363d] overflow-hidden">
      <div className="aspect-video flex items-center justify-center border border-dashed border-[#30363d] text-[#8b949e] text-sm font-mono">
        <ImageIcon className="w-12 h-12 opacity-50" />
        <span className="ml-2">Image preview (mock)</span>
      </div>
    </div>
  );
}

function SettingsView({ onBack }: { onBack: () => void }) {
  return (
    <div className="bg-[#000000] overflow-hidden flex flex-col h-full min-h-0">
      {/* Header */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-6 py-4 flex items-center justify-between shrink-0">
        <button onClick={onBack} className="flex items-center gap-2 text-sm text-[#666666] hover:text-white transition-colors">
          <ChevronLeft className="w-4 h-4" />
          <span>Back to Chat</span>
        </button>
        <div className="text-lg font-medium flex items-center gap-2">
          <Settings className="w-5 h-5 text-[#0EA5E9]" />
          Settings
        </div>
        <div className="w-20" />
      </div>

      {/* Settings Content — fills remaining space so tab area does not shrink */}
      <div className="p-8 space-y-8 flex-1 min-h-0 overflow-y-auto">
        {/* Config Path */}
        <div>
          <div className="text-xs text-[#666666] mb-2 font-mono flex items-center gap-2">
            <FileCode className="w-3 h-3" />
            Configuration Path
          </div>
          <div className="bg-[#000000] rounded-md px-4 py-3 font-mono text-xs text-[#E5E5E5] border border-[#1A1A1A]">
            ~/Project/Orchestration/Memories/SETTINGS.json
          </div>
        </div>

        {/* Backend & Routing */}
        <div>
          <div className="text-sm font-medium mb-4 text-[#E5E5E5] flex items-center gap-2">
            <Database className="w-4 h-4 text-[#0EA5E9]" />
            Backend & Routing
          </div>
          <div className="space-y-3">
            <label className="flex items-center gap-3 p-3 rounded-md hover:bg-[#0A0A0A] cursor-pointer transition-colors border border-[#1A1A1A]">
              <input type="radio" name="backend" defaultChecked className="w-4 h-4 accent-[#0EA5E9]" />
              <div className="flex-1">
                <div className="text-sm text-[#E5E5E5]">Local Default</div>
                <div className="text-xs text-[#666666] font-mono">http://localhost:11434</div>
              </div>
            </label>
            
            <label className="flex items-center gap-3 p-3 rounded-md hover:bg-[#0A0A0A] cursor-pointer transition-colors border border-transparent">
              <input type="radio" name="backend" className="w-4 h-4 accent-[#0EA5E9]" />
              <div className="flex-1">
                <div className="text-sm text-[#E5E5E5]">Custom Llama-Server</div>
                <div className="text-xs text-[#666666] font-mono">http://localhost:8080</div>
              </div>
            </label>
            
            <label className="flex items-center gap-3 p-3 rounded-md bg-[#0EA5E9]/10 border border-[#0EA5E9]/30 cursor-pointer">
              <input type="checkbox" defaultChecked className="w-4 h-4 accent-[#0EA5E9]" />
              <div className="flex-1">
                <div className="text-sm text-[#E5E5E5] flex items-center gap-2">
                  <Shield className="w-3.5 h-3.5 text-[#0EA5E9]" />
                  Strictly Local-Only
                </div>
                <div className="text-xs text-[#666666]">Disable external API fallback</div>
              </div>
            </label>
          </div>
        </div>

        {/* Security & Execution */}
        <div>
          <div className="text-sm font-medium mb-4 text-[#E5E5E5] flex items-center gap-2">
            <Shield className="w-4 h-4 text-[#0EA5E9]" />
            Security & Execution
          </div>
          <div className="space-y-3">
            <div>
              <label className="text-xs text-[#666666] block mb-2 font-mono">Execution Level</label>
              <select className="w-full bg-[#000000] border border-[#1A1A1A] rounded-md px-4 py-2.5 text-sm text-[#E5E5E5] outline-none focus:border-[#0EA5E9]/50 transition-colors font-mono">
                <option>Ask for Confirmation</option>
                <option>Auto-execute Safe Commands</option>
                <option>Always Ask</option>
              </select>
              <div className="text-xs text-[#666666] mt-1.5 font-mono">Confirm all jj / destructive actions</div>
            </div>
          </div>
        </div>

        {/* Session Management */}
        <div>
          <div className="text-sm font-medium mb-4 text-[#E5E5E5] flex items-center gap-2">
            <Activity className="w-4 h-4 text-[#0EA5E9]" />
            Session Management
          </div>
          <div className="space-y-3">
            <label className="flex items-center gap-3 p-3 rounded-md hover:bg-[#0A0A0A] cursor-pointer transition-colors border border-transparent">
              <input type="checkbox" defaultChecked className="w-4 h-4 accent-[#0EA5E9]" />
              <div className="text-sm text-[#E5E5E5]">Restore last session on app start</div>
            </label>
            
            <button className="w-full px-4 py-2.5 bg-[#0A0A0A] hover:bg-[#1A1A1A] rounded-md text-sm text-[#E5E5E5] transition-colors border border-[#1A1A1A]">
              Start Fresh Session
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

function ModsView() {
  return (
    <div className="bg-[#000000] overflow-hidden flex flex-col h-full min-h-0">
      {/* Header */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-6 py-4 shrink-0">
        <div className="text-lg font-medium flex items-center gap-2">
          <Wrench className="w-5 h-5 text-[#0EA5E9]" />
          Select Active Mod / Persona
        </div>
      </div>

      {/* Mods List — fills remaining space so tab area does not shrink */}
      <div className="p-8 space-y-4 flex-1 min-h-0 overflow-y-auto">
        {/* Standard Chatbot */}
        <label className="block p-5 rounded-lg border-2 border-[#0EA5E9] bg-[#0EA5E9]/10 cursor-pointer transition-all">
          <div className="flex items-start gap-4">
            <input type="radio" name="persona" defaultChecked className="mt-1 w-5 h-5 accent-[#0EA5E9]" />
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-2">
                <Terminal className="w-5 h-5 text-[#0EA5E9]" />
                <div className="font-medium text-[#E5E5E5]">Standard Chatbot</div>
                <span className="px-2 py-0.5 bg-[#1A1A1A] rounded text-xs text-[#666666] font-mono border border-[#333333]">Default</span>
              </div>
              <div className="text-sm text-[#999999]">
                Lightweight, general assistance and project navigation.
              </div>
            </div>
          </div>
        </label>

        {/* SWE Developer */}
        <label className="block p-5 rounded-lg border-2 border-[#1A1A1A] hover:border-[#333333] bg-[#0A0A0A] hover:bg-[#0F0F0F] cursor-pointer transition-all">
          <div className="flex items-start gap-4">
            <input type="radio" name="persona" className="mt-1 w-5 h-5 accent-[#0EA5E9]" />
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-2">
                <Zap className="w-5 h-5 text-[#10B981]" />
                <div className="font-medium text-[#E5E5E5]">SWE Developer (Orchestrator)</div>
              </div>
              <div className="text-sm text-[#999999] mb-3">
                Full access to jj commits, Docker management, and file creation.
              </div>
              <div className="flex flex-wrap gap-2">
                <span className="px-2 py-1 bg-[#000000] rounded text-xs text-[#666666] font-mono border border-[#1A1A1A]">jj commits</span>
                <span className="px-2 py-1 bg-[#000000] rounded text-xs text-[#666666] font-mono border border-[#1A1A1A]">Docker</span>
                <span className="px-2 py-1 bg-[#000000] rounded text-xs text-[#666666] font-mono border border-[#1A1A1A]">File creation</span>
              </div>
            </div>
          </div>
        </label>

        {/* Tech Lead */}
        <label className="block p-5 rounded-lg border-2 border-[#1A1A1A] hover:border-[#333333] bg-[#0A0A0A] hover:bg-[#0F0F0F] cursor-pointer transition-all">
          <div className="flex items-start gap-4">
            <input type="radio" name="persona" className="mt-1 w-5 h-5 accent-[#0EA5E9]" />
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-2">
                <Bug className="w-5 h-5 text-[#F59E0B]" />
                <div className="font-medium text-[#E5E5E5]">Tech Lead / Reviewer</div>
                <span className="px-2 py-0.5 bg-[#F59E0B]/10 rounded text-xs text-[#F59E0B] font-mono border border-[#F59E0B]/30">Read-only</span>
              </div>
              <div className="text-sm text-[#999999]">
                Read-only mode. Criticizes code quality and checks against base repo guidelines.
              </div>
            </div>
          </div>
        </label>

        {/* Load Custom */}
        <button className="w-full p-5 rounded-lg border-2 border-dashed border-[#333333] hover:border-[#666666] bg-transparent hover:bg-[#0A0A0A] transition-all">
          <div className="flex items-center justify-center gap-2 text-[#666666] hover:text-[#E5E5E5] transition-colors">
            <span className="text-2xl">+</span>
            <div className="text-sm font-medium font-mono">Load custom persona from Orchestration/Agents/Personas/</div>
          </div>
        </button>
      </div>
    </div>
  );
}