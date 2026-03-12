import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ChevronLeft, ChevronRight, Menu, Settings, User, Paperclip, Mic, Target, ChevronDown, Circle, PanelLeft, PanelRight, PanelBottom, X, Terminal, FileCode, Wrench, Cpu, Zap, Shield, Database, Code2, Layers, Activity, Bug, GitCommit, FolderOpen, File, Files, Puzzle, GitBranch, Search, ChevronRight as ChevronRightIcon, Folder, MessageCircle, Network, Globe, Image as ImageIcon, LayoutList, Bot } from 'lucide-react';

// Koi pond / water background (Unsplash, verified 200)
const KOI_POND_BG = 'https://images.unsplash.com/photo-1582794543139-8ac9cb0f7b11?w=1920&q=80';

export default function App() {
  const [showSettings, setShowSettings] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(true);

  return (
    <div className="min-h-screen overflow-hidden relative">
      {/* Koi pond background with vaporwave + blur */}
      <div className="absolute inset-0 overflow-hidden">
        <div
          className="absolute inset-0 bg-cover bg-center"
          style={{
            backgroundImage: `url(${KOI_POND_BG})`,
            transform: 'scale(1.15)',
            filter: 'blur(14px) saturate(1.5) hue-rotate(-20deg) contrast(0.9) brightness(0.65)',
          }}
        />
        <div
          className="absolute inset-0 opacity-80"
          style={{
            background: 'linear-gradient(135deg, rgba(255,0,128,0.2) 0%, transparent 45%, transparent 55%, rgba(0,255,255,0.15) 100%)',
            mixBlendMode: 'overlay',
          }}
        />
      </div>

      {/* Floating mockup window */}
      <div className="relative min-h-screen flex items-center justify-center p-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="w-full max-w-6xl rounded-xl overflow-hidden shadow-2xl border border-[#333333]/80 bg-[#0a0a0a]/95 backdrop-blur-sm"
        >
          {showSettings ? (
            <>
              <SettingsView onBack={() => setShowSettings(false)} />
            </>
          ) : (
            <MainChatView
              sidebarOpen={sidebarOpen}
              setSidebarOpen={setSidebarOpen}
              onOpenSettings={() => setShowSettings(true)}
            />
          )}
        </motion.div>
      </div>
    </div>
  );
}

type MainCenterTab = 'chat' | 'graph';
type NewPaneType = 'chat' | 'xonsh' | 'file' | 'browser' | 'image' | null;

// Obsidian-style graph mock data (nodes + edges)
const GRAPH_NODES = [
  { id: '1', label: 'README', x: 120, y: 80 },
  { id: '2', label: 'MVP_PRD', x: 320, y: 60 },
  { id: '3', label: 'CHATBOT', x: 200, y: 180 },
  { id: '4', label: 'SETTINGS', x: 80, y: 220 },
  { id: '5', label: 'RULES', x: 280, y: 200 },
  { id: '6', label: 'Orchestration', x: 180, y: 280 },
];
const GRAPH_EDGES = [
  { from: '1', to: '2' }, { from: '1', to: '3' }, { from: '2', to: '3' },
  { from: '3', to: '4' }, { from: '3', to: '5' }, { from: '4', to: '6' }, { from: '5', to: '6' },
];

function MainChatView(
  { sidebarOpen, setSidebarOpen, onOpenSettings }: { sidebarOpen: boolean; setSidebarOpen: (open: boolean) => void; onOpenSettings: () => void }
) {
  const [activeSidebarTab, setActiveSidebarTab] = useState<'files' | 'search' | 'git' | 'extensions'>('files');
  const [mainCenterTab, setMainCenterTab] = useState<MainCenterTab>('chat');
  const [newPane, setNewPane] = useState<NewPaneType>(null);
  const [expandedFolders, setExpandedFolders] = useState<string[]>(['Orchestration']);
  const [menuOpen, setMenuOpen] = useState<string | null>(null);

  const toggleFolder = (folder: string) => {
    setExpandedFolders(prev =>
      prev.includes(folder) ? prev.filter(f => f !== folder) : [...prev, folder]
    );
  };

  const menuItems = [
    { name: 'File', sub: [{ label: 'Settings', onClick: onOpenSettings }] },
    { name: 'Edit', sub: [] },
    { name: 'Selection', sub: [] },
    { name: 'Window', sub: [] },
    { name: 'Go', sub: [] },
    { name: 'Run', sub: [] },
    { name: 'Help', sub: [] },
  ];

  useEffect(() => {
    if (!menuOpen) return;
    const close = () => setMenuOpen(null);
    const t = setTimeout(() => document.addEventListener('click', close), 0);
    return () => {
      clearTimeout(t);
      document.removeEventListener('click', close);
    };
  }, [menuOpen]);

  return (
    <div className="bg-[#000000] overflow-hidden">
      {/* Top menu bar */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-2 py-1 flex items-center gap-0.5 text-sm">
        {menuItems.map((m) => (
          <div key={m.name} className="relative">
            <button
              onClick={() => setMenuOpen(menuOpen === m.name ? null : m.name)}
              className="px-3 py-1.5 rounded text-[#E5E5E5] hover:bg-[#1A1A1A]"
            >
              {m.name}
            </button>
            {menuOpen === m.name && m.sub.length > 0 && (
              <div className="absolute left-0 top-full mt-0.5 bg-[#1A1A1A] border border-[#333333] rounded-md shadow-xl py-1 z-50 min-w-[160px]">
                {m.sub.map((s) => (
                  <button key={s.label} onClick={() => { s.onClick?.(); setMenuOpen(null); }} className="w-full text-left px-3 py-1.5 text-sm text-[#E5E5E5] hover:bg-[#333333]">
                    {s.label}
                  </button>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Window Chrome */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-4 py-2 flex items-center justify-between">
        <div className="flex gap-1.5">
          <div className="w-3 h-3 rounded-full bg-[#FF5F57]" />
          <div className="w-3 h-3 rounded-full bg-[#FEBC2E]" />
          <div className="w-3 h-3 rounded-full bg-[#28C840]" />
        </div>
        <div className="text-xs text-[#666666] font-mono">dotAi — Local Desktop Chat</div>
        <div className="w-12" />
      </div>

      {/* Main Content Area */}
      <div className="flex h-[580px]">
        {/* Collapse tab when sidebar is hidden (Threads) */}
        {!sidebarOpen && (
          <button
            onClick={() => setSidebarOpen(true)}
            className="flex flex-col items-center justify-center w-9 bg-[#0A0A0A] border-r border-[#1A1A1A] hover:bg-[#1A1A1A] text-[#666666] hover:text-[#E5E5E5] transition-colors shrink-0 py-3"
            title="Show sidebar (Threads)"
          >
            <PanelRight className="w-4 h-4 shrink-0" />
            <span className="text-[10px] font-mono uppercase tracking-wider mt-2" style={{ writingMode: 'vertical-rl' }}>Threads</span>
          </button>
        )}

        {/* Left Sidebar - Threads, collapsible file explorer */}
        <motion.div
          initial={false}
          animate={{ width: sidebarOpen ? 280 : 0 }}
          className="bg-[#000000] border-r border-[#1A1A1A] overflow-hidden flex flex-col shrink-0"
        >
          <div className="w-[280px] h-full flex flex-col min-w-0">
            {/* Sidebar Header: Threads label + collapse + Explorer icons */}
            <div className="flex items-center justify-between gap-2 p-2 border-b border-[#1A1A1A] bg-[#0A0A0A] shrink-0">
              <div className="flex items-center gap-1.5 min-w-0">
                <span className="text-xs font-mono text-[#666666] uppercase tracking-wide truncate">Threads</span>
                <button
                  onClick={() => setSidebarOpen(false)}
                  className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5] transition-colors shrink-0"
                  title="Hide sidebar"
                >
                  <PanelLeft className="w-4 h-4" />
                </button>
              </div>
              <div className="flex items-center gap-0.5 shrink-0">
                <button
                  onClick={() => setActiveSidebarTab('files')}
                  className={`p-2 rounded transition-colors ${activeSidebarTab === 'files' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
                  title="Explorer"
                >
                  <Folder className="w-4 h-4" />
                </button>
                <button
                  onClick={() => setActiveSidebarTab('search')}
                  className={`p-2 rounded transition-colors ${activeSidebarTab === 'search' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
                  title="Search"
                >
                  <Search className="w-4 h-4" />
                </button>
                <button
                  onClick={() => setActiveSidebarTab('git')}
                  className={`p-2 rounded transition-colors ${activeSidebarTab === 'git' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
                  title="Source Control"
                >
                  <GitBranch className="w-4 h-4" />
                </button>
                <button
                  onClick={() => setActiveSidebarTab('extensions')}
                  className={`p-2 rounded transition-colors ${activeSidebarTab === 'extensions' ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
                  title="Extensions"
                >
                  <Puzzle className="w-4 h-4" />
                </button>
              </div>
            </div>

            {/* Sidebar Content */}
            <div className="flex-1 overflow-y-auto p-2 flex flex-col gap-4">
              {/* Threads section - always visible at top */}
              <div>
                <div className="text-xs text-[#666666] font-mono uppercase tracking-wide mb-1.5 px-2">Threads</div>
                <div className="space-y-0.5">
                  <div className="px-2 py-1.5 rounded text-sm text-[#E5E5E5] bg-[#E5E5E5]/5 cursor-pointer"># general</div>
                  <div className="px-2 py-1.5 rounded text-sm text-[#666666] hover:bg-[#1A1A1A] cursor-pointer"># MVP PRD</div>
                  <div className="px-2 py-1.5 rounded text-sm text-[#666666] hover:bg-[#1A1A1A] cursor-pointer"># docs</div>
                </div>
              </div>

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

        {/* Center Area - Icon tabs + Content */}
        <div className="flex-1 flex flex-col min-w-0 bg-[#000000] relative">
          {/* Icon tab bar + New pane buttons */}
          <div className="flex items-center border-b border-[#1A1A1A] px-2 py-1.5 gap-1">
            <button
              onClick={() => setMainCenterTab('chat')}
              className={`p-2 rounded transition-colors ${mainCenterTab === 'chat' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
              title="Chat"
            >
              <MessageCircle className="w-4 h-4" />
            </button>
            <button
              onClick={() => setMainCenterTab('graph')}
              className={`p-2 rounded transition-colors ${mainCenterTab === 'graph' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
              title="Graph View"
            >
              <Network className="w-4 h-4" />
            </button>
            <div className="w-px h-5 bg-[#333333] mx-1" />
            <button
              onClick={() => setNewPane(newPane === 'chat' ? null : 'chat')}
              className={`p-2 rounded transition-colors ${newPane === 'chat' ? 'bg-[#0EA5E9]/20 text-[#0EA5E9]' : 'text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`}
              title="New chat"
            >
              <MessageCircle className="w-4 h-4" />
            </button>
            <button
              onClick={() => setNewPane(newPane === 'xonsh' ? null : 'xonsh')}
              className={`p-2 rounded transition-colors ${newPane === 'xonsh' ? 'bg-[#0EA5E9]/20 text-[#0EA5E9]' : 'text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`}
              title="New xonsh"
            >
              <Terminal className="w-4 h-4" />
            </button>
            <button
              onClick={() => setNewPane(newPane === 'file' ? null : 'file')}
              className={`p-2 rounded transition-colors ${newPane === 'file' ? 'bg-[#0EA5E9]/20 text-[#0EA5E9]' : 'text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`}
              title="New file"
            >
              <FileCode className="w-4 h-4" />
            </button>
            <button
              onClick={() => setNewPane(newPane === 'browser' ? null : 'browser')}
              className={`p-2 rounded transition-colors ${newPane === 'browser' ? 'bg-[#0EA5E9]/20 text-[#0EA5E9]' : 'text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`}
              title="New browser"
            >
              <Globe className="w-4 h-4" />
            </button>
            <button
              onClick={() => setNewPane(newPane === 'image' ? null : 'image')}
              className={`p-2 rounded transition-colors ${newPane === 'image' ? 'bg-[#0EA5E9]/20 text-[#0EA5E9]' : 'text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`}
              title="New image"
            >
              <ImageIcon className="w-4 h-4" />
            </button>
          </div>

          {mainCenterTab === 'graph' ? (
            <div className="flex-1 min-h-0 relative bg-[#0d1117] overflow-hidden" aria-label="Graph View canvas">
              {/* Obsidian-style graph */}
              <svg className="w-full h-full" viewBox="0 0 400 320" preserveAspectRatio="xMidYMid meet">
                <defs>
                  <linearGradient id="edgeGrad" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stopColor="#3B82F6" stopOpacity="0.6" />
                    <stop offset="100%" stopColor="#8B5CF6" stopOpacity="0.4" />
                  </linearGradient>
                </defs>
                {GRAPH_EDGES.map((e, i) => {
                  const a = GRAPH_NODES.find(n => n.id === e.from)!;
                  const b = GRAPH_NODES.find(n => n.id === e.to)!;
                  return (
                    <line key={i} x1={a.x} y1={a.y} x2={b.x} y2={b.y} stroke="url(#edgeGrad)" strokeWidth="1.2" strokeOpacity="0.7" />
                  );
                })}
                {GRAPH_NODES.map(node => (
                  <g key={node.id}>
                    <circle cx={node.x} cy={node.y} r="24" fill="#1a1f2e" stroke="#30363d" strokeWidth="1.5" className="hover:stroke-[#58a6ff]" />
                    <text x={node.x} y={node.y + 5} textAnchor="middle" fill="#e6edf3" fontSize="11" fontFamily="ui-monospace, monospace">{node.label}</text>
                  </g>
                ))}
              </svg>
              {/* + New pane overlays */}
            </div>
          ) : (
          <div className="flex-1 flex items-center justify-center p-8">
            <div className="max-w-2xl w-full space-y-8">
              {/* Greeting */}
              <div className="text-center space-y-4">
                <div className="text-4xl font-light text-[#E5E5E5]">
                  Good evening.
                </div>
                <div className="text-lg text-[#666666]">
                  What would you like to work on today?
                </div>
              </div>

              {/* Zen Input */}
              <div className="space-y-4">
                <div className="relative">
                  <input
                    type="text"
                    placeholder="Ask dotAi anything..."
                    className="w-full bg-[#0A0A0A] border border-[#1A1A1A] rounded-lg px-6 py-4 text-base text-[#E5E5E5] placeholder:text-[#666666] outline-none focus:border-[#E5E5E5]/30 transition-colors"
                  />
                  <div className="absolute right-4 top-1/2 -translate-y-1/2 flex items-center gap-2">
                    <button className="p-2 hover:bg-[#1A1A1A] rounded transition-colors" title="Attach files">
                      <Paperclip className="w-4 h-4 text-[#666666]" />
                    </button>
                    <button className="p-2 hover:bg-[#1A1A1A] rounded transition-colors" title="Voice input">
                      <Mic className="w-4 h-4 text-[#666666]" />
                    </button>
                  </div>
                </div>

                {/* Quick Actions */}
                <div className="flex items-center justify-center gap-3 text-xs">
                  <button className="px-3 py-1.5 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-md text-[#E5E5E5] transition-colors">
                    <span className="flex items-center gap-1.5">
                      <Cpu className="w-3 h-3" />
                      Llama-3-8B
                    </span>
                  </button>
                  <button className="px-3 py-1.5 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-md text-[#E5E5E5] transition-colors">
                    <span className="flex items-center gap-1.5">
                      <Zap className="w-3 h-3" />
                      SWE Developer
                    </span>
                  </button>
                  <button className="px-3 py-1.5 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-md text-[#E5E5E5] transition-colors">
                    <span className="flex items-center gap-1.5">
                      <Shield className="w-3 h-3" />
                      Local Only
                    </span>
                  </button>
                </div>
              </div>

              {/* Suggestions */}
              <div className="space-y-3">
                <div className="text-xs text-[#666666] uppercase tracking-wide font-mono">Suggestions</div>
                <div className="grid grid-cols-2 gap-3">
                  <button className="text-left p-4 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-lg transition-colors group">
                    <div className="flex items-start gap-2 mb-2">
                      <GitCommit className="w-4 h-4 text-[#666666] group-hover:text-[#E5E5E5] transition-colors" />
                      <span className="text-sm text-[#E5E5E5]">Review recent commits</span>
                    </div>
                    <div className="text-xs text-[#666666]">Analyze jj log and summarize changes</div>
                  </button>
                  <button className="text-left p-4 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-lg transition-colors group">
                    <div className="flex items-start gap-2 mb-2">
                      <FileCode className="w-4 h-4 text-[#666666] group-hover:text-[#E5E5E5] transition-colors" />
                      <span className="text-sm text-[#E5E5E5]">Update documentation</span>
                    </div>
                    <div className="text-xs text-[#666666]">Draft updates for project docs</div>
                  </button>
                  <button className="text-left p-4 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-lg transition-colors group">
                    <div className="flex items-start gap-2 mb-2">
                      <Terminal className="w-4 h-4 text-[#666666] group-hover:text-[#E5E5E5] transition-colors" />
                      <span className="text-sm text-[#E5E5E5]">Run diagnostic check</span>
                    </div>
                    <div className="text-xs text-[#666666]">System health and dependencies</div>
                  </button>
                  <button className="text-left p-4 bg-[#0A0A0A] hover:bg-[#1A1A1A] border border-[#1A1A1A] rounded-lg transition-colors group">
                    <div className="flex items-start gap-2 mb-2">
                      <Code2 className="w-4 h-4 text-[#666666] group-hover:text-[#E5E5E5] transition-colors" />
                      <span className="text-sm text-[#E5E5E5]">Code review</span>
                    </div>
                    <div className="text-xs text-[#666666]">Check quality and patterns</div>
                  </button>
                </div>
              </div>
            </div>
          </div>
          )}
          {/* + New pane overlay (shared for Chat and Graph) */}
          {newPane && (
            <div className="absolute inset-0 flex items-center justify-center bg-black/60 z-10 p-8">
              <div className="bg-[#161b22] border border-[#30363d] rounded-lg shadow-xl max-w-2xl w-full max-h-[80%] overflow-hidden flex flex-col">
                <div className="flex items-center justify-between px-4 py-2 border-b border-[#30363d]">
                  <span className="text-sm font-mono text-[#e6edf3]">
                    {newPane === 'chat' && 'Chat'}
                    {newPane === 'xonsh' && 'xonsh'}
                    {newPane === 'file' && 'file'}
                    {newPane === 'browser' && 'Browser'}
                    {newPane === 'image' && 'Image'}
                  </span>
                  <button onClick={() => setNewPane(null)} className="p-1 text-[#8b949e] hover:text-[#e6edf3] rounded"><X className="w-4 h-4" /></button>
                </div>
                <div className="flex-1 overflow-auto p-4 min-h-[200px]">
                  {newPane === 'chat' && <ChatPaneMock />}
                  {newPane === 'xonsh' && <XonshPaneMock />}
                  {newPane === 'file' && <FilePaneMock />}
                  {newPane === 'browser' && <BrowserPaneMock />}
                  {newPane === 'image' && <ImagePaneMock />}
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
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
    <div className="bg-[#000000] overflow-hidden">
      {/* Header */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-6 py-4 flex items-center justify-between">
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

      {/* Settings Content */}
      <div className="p-8 space-y-8 max-h-[600px] overflow-y-auto">
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
    <div className="bg-[#000000] rounded-xl overflow-hidden shadow-2xl border border-[#1A1A1A]">
      {/* Window Chrome */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="flex gap-1.5">
            <div className="w-3 h-3 rounded-full bg-[#FF5F57]" />
            <div className="w-3 h-3 rounded-full bg-[#FEBC2E]" />
            <div className="w-3 h-3 rounded-full bg-[#28C840]" />
          </div>
        </div>
        <div className="text-xs text-[#666666] font-mono">dotAi — Mods & Personas</div>
        <div className="w-12" />
      </div>

      {/* Header */}
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-6 py-4">
        <div className="text-lg font-medium flex items-center gap-2">
          <Wrench className="w-5 h-5 text-[#0EA5E9]" />
          Select Active Mod / Persona
        </div>
      </div>

      {/* Mods List */}
      <div className="p-8 space-y-4 max-h-[600px] overflow-y-auto">
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