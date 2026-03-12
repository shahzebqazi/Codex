import { useState, useEffect, useRef, useMemo } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ChevronLeft, ChevronRight, Menu, User, Paperclip, Mic, Target, ChevronDown, Circle, PanelLeft, PanelRight, PanelBottom, X, Terminal, FileCode, Cpu, Zap, Shield, Database, Code2, Layers, Activity, Bug, GitCommit, FolderOpen, File, Files, Puzzle, GitBranch, Search, ChevronRight as ChevronRightIcon, Folder, MessageCircle, Network, Globe, Image as ImageIcon, LayoutList, Bot, MoreVertical, SlidersHorizontal, Percent, DollarSign, ZoomIn, ZoomOut, RotateCcw, Filter, Plus, HandMetal, Home, Settings, FileText, Scale } from 'lucide-react';
import { ThreadGraph3D, type ThreadNode, type ThreadLink } from './components/ThreadGraph3D';
import { MiniBtop } from './components/MiniBtop';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from './components/ui/dropdown-menu';
import { useIsNarrowViewport } from './components/ui/use-mobile';

// File-type tagging: derive tag from name (and isDir) for consistent icon + color
export type FileTag = 'dir' | 'md' | 'txt' | 'js' | 'ts' | 'tsx' | 'json' | 'license' | 'default';

export function getFileTag(name: string, isDir: boolean): FileTag {
  if (isDir) return 'dir';
  const lower = name.toLowerCase();
  if (/^license(\.[a-z]+)?$/i.test(lower) || lower === 'copying') return 'license';
  const ext = lower.includes('.') ? lower.slice(lower.lastIndexOf('.') + 1) : '';
  switch (ext) {
    case 'md': return 'md';
    case 'txt': return 'txt';
    case 'js': case 'mjs': case 'cjs': return 'js';
    case 'ts': return 'ts';
    case 'tsx': case 'jsx': return 'tsx';
    case 'json': case 'jsonc': return 'json';
    default: return 'default';
  }
}

const FILE_TAG_STYLES: Record<FileTag, { color: string; Icon: React.ComponentType<{ className?: string; style?: React.CSSProperties }> }> = {
  dir:    { color: '#9ca3af', Icon: Folder },
  md:     { color: '#79c0ff', Icon: FileText },
  txt:    { color: '#e6edf3', Icon: FileText },
  js:     { color: '#7ee787', Icon: FileCode },
  ts:     { color: '#79c0ff', Icon: FileCode },
  tsx:    { color: '#79c0ff', Icon: FileCode },
  json:   { color: '#f0d77e', Icon: FileCode },
  license: { color: '#f472b6', Icon: Scale },
  default: { color: '#e5e5e5', Icon: File },
};

// Flowers background (Unsplash, free to use) — static for mockup
const FLOWERS_BG = 'https://images.unsplash.com/photo-1490750967868-88aa4486c946?w=1920&q=80';

// 1440p laptop: 2560×1440 logical pixels (mimics QHD high-DPI laptop screen)
const MOCKUP_WIDTH = 2560;
const MOCKUP_HEIGHT = 1440;
const MOCKUP_PADDING = 128;
const SCALE_MIN = 0.2;
const SCALE_MAX = 2;
const SCALE_STEP = 0.15;

type TabKind = 'home' | 'settings' | 'editor' | 'terminal' | 'agent' | 'planner' | 'chat' | 'assistant';
type Tab = { id: string; kind: TabKind };

const TAB_LABELS: Record<TabKind, string> = {
  home: 'Home',
  settings: '.config',
  editor: 'Editor',
  terminal: 'Terminal',
  agent: 'Agent',
  planner: 'Planner',
  chat: 'Chat',
  assistant: 'Assistant',
};

function nextTabId() {
  return `tab-${Date.now()}-${Math.random().toString(36).slice(2, 6)}`;
}

export default function App() {
  const isNarrow = useIsNarrowViewport();
  const [tabs, setTabs] = useState<Tab[]>([
    { id: 'tab-home', kind: 'home' },
    { id: 'tab-settings', kind: 'settings' },
  ]);
  const [activeTabId, setActiveTabId] = useState<string>('tab-home');
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [scale, setScale] = useState(1);

  const activeTab = tabs.find((t) => t.id === activeTabId) ?? tabs[0];

  const addTab = (kind: TabKind) => {
    const id = nextTabId();
    setTabs((prev) => [...prev, { id, kind }]);
    setActiveTabId(id);
  };

  const closeTab = (id: string) => {
    setTabs((prev) => {
      const next = prev.filter((t) => t.id !== id);
      if (activeTabId === id && next.length) {
        const idx = Math.max(0, prev.findIndex((t) => t.id === id) - 1);
        setActiveTabId(next[idx].id);
      }
      return next;
    });
  };

  // Fit mockup to viewport on load and resize (desktop only; narrow uses full-width layout)
  useEffect(() => {
    if (isNarrow) return;
    const updateScaleToFit = () => {
      const s = Math.min(
        (window.innerWidth - MOCKUP_PADDING) / MOCKUP_WIDTH,
        (window.innerHeight - MOCKUP_PADDING) / MOCKUP_HEIGHT,
        1
      );
      setScale(Math.max(SCALE_MIN, s));
    };
    updateScaleToFit();
    window.addEventListener('resize', updateScaleToFit);
    return () => window.removeEventListener('resize', updateScaleToFit);
  }, [isNarrow]);

  // Cmd/Ctrl +/- zoom the mockup (app), not the page — prevent browser zoom (desktop only)
  useEffect(() => {
    if (isNarrow) return;
    const onKeyDown = (e: KeyboardEvent) => {
      if (!e.metaKey && !e.ctrlKey) return;
      const zoomIn = e.key === '=' || e.key === '+';
      const zoomOut = e.key === '-';
      if (!zoomIn && !zoomOut) return;
      e.preventDefault();
      setScale((s) => {
        const next = zoomIn ? s + SCALE_STEP : s - SCALE_STEP;
        return Math.max(SCALE_MIN, Math.min(SCALE_MAX, next));
      });
    };
    window.addEventListener('keydown', onKeyDown, { capture: true });
    return () => window.removeEventListener('keydown', onKeyDown, { capture: true });
  }, [isNarrow]);

  const tabBar = (
    <div className="flex items-center gap-2 border-b border-[#1A1A1A] bg-[#0A0A0A] px-2 py-1.5 shrink-0 min-h-[36px]">
      {!isNarrow && (
        <div className="flex items-center gap-1.5 shrink-0 pointer-events-none">
          <div className="w-3 h-3 rounded-full bg-[#FF5F57]" />
          <div className="w-3 h-3 rounded-full bg-[#FEBC2E]" />
          <div className="w-3 h-3 rounded-full bg-[#28C840]" />
        </div>
      )}
      <div className="flex items-center gap-0.5 flex-1 min-w-0 overflow-x-auto">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setActiveTabId(tab.id)}
            className={`px-3 py-2 rounded-t text-sm font-mono flex items-center gap-1.5 shrink-0 ${activeTabId === tab.id ? 'bg-[#0a0a0a] text-[#E5E5E5] border border-[#1A1A1A] border-b-transparent -mb-px' : 'text-[#666666] hover:text-[#E5E5E5]'}`}
          >
            {TAB_LABELS[tab.kind]}
            {tabs.length > 1 && (
              <span
                className="opacity-70 hover:opacity-100 rounded p-0.5 hover:bg-[#333]"
                onClick={(e) => { e.stopPropagation(); closeTab(tab.id); }}
                aria-label="Close tab"
              >
                <X className="w-3 h-3" />
              </span>
            )}
          </button>
        ))}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button
              type="button"
              className="p-2 rounded-t text-sm font-mono text-[#666666] hover:text-[#E5E5E5] hover:bg-[#1A1A1A] shrink-0"
              title="New tab"
              aria-label="New tab"
            >
              <Plus className="w-4 h-4" />
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            align="end"
            sideOffset={4}
            className="min-w-[11rem] bg-[#161b22] border-[#30363d] text-[#e6edf3] p-1 rounded-md shadow-lg"
          >
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('home')}>
              <Home className="w-4 h-4" /> Home
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('settings')}>
              <Settings className="w-4 h-4" /> .config
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('editor')}>
              <FileCode className="w-4 h-4" /> Editor
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('terminal')}>
              <Terminal className="w-4 h-4" /> Terminal
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('agent')}>
              <Bot className="w-4 h-4" /> Agent
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('planner')}>
              <LayoutList className="w-4 h-4" /> Planner
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('chat')}>
              <MessageCircle className="w-4 h-4" /> Chat
            </DropdownMenuItem>
            <DropdownMenuItem className="focus:bg-[#30363d] focus:text-[#e6edf3] cursor-pointer rounded px-2 py-1.5 text-sm font-mono flex items-center gap-2" onSelect={() => addTab('assistant')}>
              <Zap className="w-4 h-4" /> Assistant
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  );

  const paneContent = (
    <>
      {activeTab.kind === 'home' && (
        <MainChatView
          narrow={isNarrow}
          sidebarOpen={sidebarOpen}
          setSidebarOpen={setSidebarOpen}
          onOpenSettings={() => addTab('settings')}
        />
      )}
      {activeTab.kind === 'settings' && <ConfigFileView onBack={() => setActiveTabId(tabs[0]?.id ?? activeTabId)} />}
      {activeTab.kind === 'editor' && <EditorPaneMock />}
      {activeTab.kind === 'terminal' && <TerminalPaneMock />}
      {activeTab.kind === 'agent' && <AgentPaneMock />}
      {activeTab.kind === 'planner' && <PlannerPaneMock />}
      {activeTab.kind === 'chat' && <ChatPaneMock />}
      {activeTab.kind === 'assistant' && <AssistantPaneMock />}
    </>
  );

  // Narrow viewport: full-width responsive layout (no scaled mockup)
  if (isNarrow) {
    return (
      <div className="min-h-screen h-dvh overflow-hidden relative flex flex-col bg-black">
        {activeTab.kind === 'home' && (
          <div
            className="absolute inset-0 bg-cover bg-center pointer-events-none"
            style={{
              backgroundImage: `url(${FLOWERS_BG})`,
              filter: 'blur(4px) saturate(1.2) contrast(0.95) brightness(0.5)',
            }}
          />
        )}
        <div className="relative z-10 flex flex-col flex-1 min-h-0 bg-[#0a0a0a]/95 rounded-t-xl overflow-hidden shadow-2xl">
          {tabBar}
          <div className="flex-1 min-h-0 overflow-hidden">{paneContent}</div>
        </div>
      </div>
    );
  }

  // Desktop: floating scaled mockup window
  return (
    <div className="min-h-screen overflow-hidden relative">
      <div className="absolute inset-0 overflow-hidden bg-black">
        {activeTab.kind === 'home' && (
          <div
            className="absolute inset-0 bg-cover bg-center"
            style={{
              backgroundImage: `url(${FLOWERS_BG})`,
              filter: 'blur(4px) saturate(1.2) contrast(0.95) brightness(0.7)',
            }}
          />
        )}
      </div>
      <div className="absolute inset-0 flex items-center justify-center p-4 sm:p-6">
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
            {tabBar}
            <div className="flex-1 min-h-0 overflow-hidden">{paneContent}</div>
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

type GraphFilterRole = 'all' | 'user' | 'assistant' | 'system';

function GraphView({
  selectedThreadId,
  threadGraphData,
}: {
  selectedThreadId: string;
  threadGraphData: { nodes: ThreadNode[]; links: ThreadLink[] };
  threadIds: string[];
}) {
  const graphRef = useRef<{ zoom: (level: number, ms: number) => void; zoomToFit: (ms: number, padding: number) => void } | null>(null);
  const [graphBg, setGraphBg] = useState<'white' | 'black'>('black');
  const [graphFilterRole, setGraphFilterRole] = useState<GraphFilterRole>('all');
  const [filterMenuOpen, setFilterMenuOpen] = useState(false);

  const { nodes, links } = useMemo(() => {
    if (graphFilterRole === 'all') return threadGraphData;
    const visibleIds = new Set(
      threadGraphData.nodes.filter((n) => n.role === graphFilterRole).map((n) => n.id)
    );
    const filteredNodes = threadGraphData.nodes.filter((n) => visibleIds.has(n.id));
    const filteredLinks = threadGraphData.links.filter(
      (l) => visibleIds.has(String(l.source)) && visibleIds.has(String(l.target))
    );
    return { nodes: filteredNodes, links: filteredLinks };
  }, [threadGraphData, graphFilterRole]);

  const backgroundColor = graphBg === 'white' ? '#f6f8fa' : '#0d1117';

  return (
    <div className="flex flex-col h-full min-h-0 p-4 gap-3">
      <div className="flex items-center gap-2 flex-wrap shrink-0">
        <span className="text-[10px] font-mono text-[#8b949e] mr-2">Thread: #{selectedThreadId}</span>
        <div className="flex items-center gap-1 border border-[#30363d] rounded overflow-hidden bg-[#161b22]">
          <button
            type="button"
            onClick={() => graphRef.current?.zoom(1.5, 200)}
            className="p-1.5 text-[#e6edf3] hover:bg-[#30363d] transition-colors"
            title="Zoom in"
          >
            <ZoomIn className="w-3.5 h-3.5" />
          </button>
          <button
            type="button"
            onClick={() => graphRef.current?.zoom(0.66, 200)}
            className="p-1.5 text-[#e6edf3] hover:bg-[#30363d] transition-colors"
            title="Zoom out"
          >
            <ZoomOut className="w-3.5 h-3.5" />
          </button>
          <button
            type="button"
            onClick={() => graphRef.current?.zoomToFit(400, 50)}
            className="p-1.5 text-[#e6edf3] hover:bg-[#30363d] transition-colors"
            title="Reset view / fit"
          >
            <RotateCcw className="w-3.5 h-3.5" />
          </button>
        </div>
        <div className="flex items-center gap-1 border border-[#30363d] rounded overflow-hidden bg-[#161b22]">
          <button
            type="button"
            onClick={() => setGraphBg('white')}
            className={`px-2 py-1.5 text-xs font-mono transition-colors ${graphBg === 'white' ? 'bg-[#e6edf3] text-[#0d1117]' : 'text-[#8b949e] hover:bg-[#30363d]'}`}
            title="White background"
          >
            White
          </button>
          <button
            type="button"
            onClick={() => setGraphBg('black')}
            className={`px-2 py-1.5 text-xs font-mono transition-colors ${graphBg === 'black' ? 'bg-[#30363d] text-[#e6edf3]' : 'text-[#8b949e] hover:bg-[#30363d]'}`}
            title="Black background"
          >
            Black
          </button>
        </div>
        <div className="relative">
          <button
            type="button"
            onClick={() => setFilterMenuOpen((o) => !o)}
            className="flex items-center gap-1.5 px-2 py-1.5 border border-[#30363d] rounded bg-[#161b22] text-[#e6edf3] hover:bg-[#30363d] transition-colors text-xs font-mono"
            title="Filter by role"
          >
            <Filter className="w-3.5 h-3.5" />
            Filter: {graphFilterRole === 'all' ? 'All' : graphFilterRole}
          </button>
          {filterMenuOpen && (
            <>
              <div className="fixed inset-0 z-10" aria-hidden onClick={() => setFilterMenuOpen(false)} />
              <div className="absolute left-0 top-full mt-1 py-1 min-w-[120px] border border-[#30363d] rounded bg-[#161b22] shadow-lg z-20">
                {(['all', 'user', 'assistant', 'system'] as const).map((role) => (
                  <button
                    key={role}
                    type="button"
                    onClick={() => {
                      setGraphFilterRole(role);
                      setFilterMenuOpen(false);
                    }}
                    className={`w-full text-left px-3 py-1.5 text-xs font-mono capitalize ${graphFilterRole === role ? 'bg-[#30363d] text-[#e6edf3]' : 'text-[#8b949e] hover:bg-[#30363d] hover:text-[#e6edf3]'}`}
                  >
                    {role}
                  </button>
                ))}
              </div>
            </>
          )}
        </div>
      </div>
      <div className="flex-1 min-h-[280px] flex items-center justify-center rounded border border-[#30363d] overflow-hidden">
        <ThreadGraph3D
          ref={graphRef}
          nodes={nodes}
          links={links}
          width={620}
          height={360}
          backgroundColor={backgroundColor}
        />
      </div>
    </div>
  );
}

function MainChatView(
  { narrow = false, sidebarOpen, setSidebarOpen, onOpenSettings }: { narrow?: boolean; sidebarOpen: boolean; setSidebarOpen: (open: boolean) => void; onOpenSettings: () => void }
) {
  const [leftSidebarOpen, setLeftSidebarOpen] = useState(!narrow);
  const [rightSidebarOpen, setRightSidebarOpen] = useState(!narrow);
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

  const leftSidebarContent = (
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
              {(() => {
                const { color, Icon } = FILE_TAG_STYLES.dir;
                return <Icon className="w-4 h-4 shrink-0" style={{ color }} />;
              })()}
              <span style={{ color: FILE_TAG_STYLES.dir.color }}>Orchestration</span>
            </button>
            {expandedFolders.includes('Orchestration') && (
              <div className="ml-4 space-y-0.5">
                {[
                  { name: 'Agents', isDir: true },
                  { name: 'Memories', isDir: true },
                  { name: 'MVP_PRD.md', isDir: false },
                  { name: 'CHATBOT.md', isDir: false },
                  { name: 'README.md', isDir: false },
                  { name: 'notes.txt', isDir: false },
                  { name: 'index.js', isDir: false },
                  { name: 'LICENSE', isDir: false },
                ].map(({ name, isDir }) => {
                  const tag = getFileTag(name, isDir);
                  const { color, Icon } = FILE_TAG_STYLES[tag];
                  const isSelected = name === 'MVP_PRD.md';
                  return (
                    <div
                      key={name}
                      className={`flex items-center gap-1 px-2 py-1 rounded text-sm cursor-pointer transition-colors ${isSelected ? 'bg-[#E5E5E5]/5' : 'hover:bg-[#1A1A1A]'}`}
                    >
                      <Icon className="w-4 h-4 shrink-0" style={{ color }} />
                      <span style={{ color }}>{name}</span>
                    </div>
                  );
                })}
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
                <span style={{ color: FILE_TAG_STYLES[getFileTag('MVP_PRD.md', false)].color }}>MVP_PRD.md</span>
              </div>
              <div className="flex items-center gap-2 px-2 py-1 hover:bg-[#1A1A1A] rounded cursor-pointer transition-colors">
                <span className="text-[#F59E0B]">A</span>
                <span style={{ color: FILE_TAG_STYLES[getFileTag('FEATURES.md', false)].color }}>FEATURES.md</span>
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
  );

  return (
    <div className="bg-[#0a0a0a] overflow-hidden flex flex-col h-full min-h-0">
      <div className="flex flex-col flex-1 min-h-0">
        <div className={`flex flex-1 min-h-0 ${narrow ? 'flex-col' : ''}`}>
          {/* Left Sidebar — inline on desktop, overlay drawer on narrow */}
          {narrow ? (
            <AnimatePresence>
              {leftSidebarOpen && (
                <>
                  <div className="fixed inset-0 bg-black/60 z-40" aria-hidden onClick={() => setLeftSidebarOpen(false)} />
                  <motion.div
                    initial={{ x: '-100%' }}
                    animate={{ x: 0 }}
                    exit={{ x: '-100%' }}
                    transition={{ type: 'tween', duration: 0.2 }}
                    className="fixed left-0 top-0 bottom-0 w-[min(280px,85vw)] bg-[#000000] border-r border-[#1A1A1A] z-50 flex flex-col shadow-xl"
                  >
                    {leftSidebarContent}
                  </motion.div>
                </>
              )}
            </AnimatePresence>
          ) : (
          <motion.div
            initial={false}
            animate={{ width: leftSidebarOpen ? 260 : 0 }}
            className="bg-[#000000] border-r border-[#1A1A1A] overflow-hidden flex flex-col shrink-0"
          >
            {leftSidebarContent}
          </motion.div>
          )}

          {/* Center: half (main) + right column (chat + btop); on narrow they stack vertically */}
          <div className={`flex-1 flex min-w-0 bg-[#0a0a0a] relative ${narrow ? 'flex-col' : ''}`}>
            {/* Major section — list / kanban / calendar / graph */}
            <div className={`min-w-0 flex flex-col border-[#1A1A1A] bg-[#0a0a0a] shrink-0 ${narrow ? 'w-full border-b min-h-[200px]' : 'w-1/2 border-r'}`}>
              <div className="flex items-center gap-1 px-2 py-1.5 border-b border-[#1A1A1A] shrink-0 bg-[#0a0a0a]">
                <button onClick={() => setMainViewMode('list')} className={`px-2 py-1 rounded text-xs font-mono ${mainViewMode === 'list' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>List</button>
                <button onClick={() => setMainViewMode('kanban')} className={`px-2 py-1 rounded text-xs font-mono ${mainViewMode === 'kanban' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Kanban</button>
                <button onClick={() => setMainViewMode('calendar')} className={`px-2 py-1 rounded text-xs font-mono ${mainViewMode === 'calendar' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`}>Calendar</button>
                <button onClick={() => setMainViewMode('graph')} className={`px-2 py-1 rounded text-xs font-mono flex items-center gap-1 ${mainViewMode === 'graph' ? 'bg-[#E5E5E5]/15 text-[#E5E5E5]' : 'text-[#666666] hover:text-[#E5E5E5]'}`} title="3D thread graph"><Network className="w-3.5 h-3.5" /> Graph</button>
              </div>
              <div className="flex-1 overflow-auto min-h-0 bg-[#0a0a0a] flex flex-col">
                {mainViewMode === 'list' && (
                  <div className="p-4">
                    <ul className="space-y-2 font-mono text-sm">
                      {MOCK_TASKS.map(t => (
                        <li key={t.id} className="flex items-center gap-3 px-4 py-3 rounded bg-[#0A0A0A] border border-[#1A1A1A]">
                          <span className="text-[#666666] w-6">{t.id}</span>
                          <span className="text-[#E5E5E5] flex-1">{t.title}</span>
                          <span className="text-[#10B981] text-xs">{t.status}</span>
                          <span className="text-[#666666] text-xs">{t.assignee}</span>
                          <span className="text-[#666666] text-xs">{t.due}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                )}
                {mainViewMode === 'kanban' && (
                  <div className="flex gap-4 h-full min-h-[200px] p-4">
                    {(['todo', 'in_progress', 'done'] as const).map(col => (
                      <div key={col} className="flex-1 min-w-[140px] rounded-lg bg-[#0A0A0A] border border-[#1A1A1A] p-4">
                        <div className="text-xs font-mono text-[#666666] uppercase mb-3">{col.replace('_', ' ')}</div>
                        <div className="space-y-3">
                          {MOCK_TASKS.filter(t => t.status === col).map(t => (
                            <div
                              key={t.id}
                              className={`p-3 rounded-lg border text-sm text-[#0a0a0a] font-medium ${
                                col === 'todo'
                                  ? 'bg-[#bfdbfe] border-[#93c5fd]'
                                  : col === 'in_progress'
                                    ? 'bg-[#fde68a] border-[#fcd34d]'
                                    : 'bg-[#bbf7d0] border-[#86efac]'
                              }`}
                            >
                              {t.title}
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
                {mainViewMode === 'calendar' && (
                  <div className="flex-1 min-h-0 flex flex-col p-4">
                    <div className="grid grid-cols-7 grid-rows-[auto_1fr_1fr_1fr_1fr_1fr] gap-1.5 flex-1 min-h-0 font-mono text-sm">
                      {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map(d => (
                        <div key={d} className="text-[#666666] text-xs py-2 px-1 text-center flex items-center justify-center">{d}</div>
                      ))}
                      {Array.from({ length: 31 }, (_, i) => {
                        const day = i + 1;
                        const events = MOCK_CALENDAR_EVENTS.filter(e => e.date === `Mar ${day}`);
                        return (
                          <div key={i} className="min-h-0 p-2 rounded bg-[#0A0A0A] border border-[#1A1A1A] flex flex-col overflow-hidden">
                            <span className="text-[#666666] text-xs shrink-0">{day}</span>
                            <div className="flex-1 min-h-0 overflow-hidden space-y-0.5">
                              {events.slice(0, 3).map(ev => (
                                <div key={ev.id} className="text-[10px] text-[#0EA5E9] truncate">{ev.title}</div>
                              ))}
                            </div>
                          </div>
                        );
                      })}
                      {Array.from({ length: 4 }, (_, i) => (
                        <div key={`empty-${i}`} className="min-h-0 p-2 rounded bg-[#0A0A0A]/50 border border-[#1A1A1A]/50" aria-hidden />
                      ))}
                    </div>
                  </div>
                )}
                {mainViewMode === 'graph' && (
                  <GraphView
                    selectedThreadId={selectedThreadId}
                    threadGraphData={threadGraphData}
                    threadIds={['general', 'MVP PRD', 'docs']}
                  />
                )}
              </div>
            </div>

            {/* Right column — AI chat + btop; full width when narrow */}
            <div className={`min-w-0 flex flex-col shrink-0 border-[#1A1A1A] bg-[#0a0a0a] ${narrow ? 'w-full flex-1 min-h-0 border-t' : 'w-1/2 border-l'}`}>
              {/* Chat: messages area + input at bottom with icons */}
              <div className="flex-1 min-h-0 flex flex-col border-b border-[#1A1A1A] bg-[#0a0a0a] overflow-hidden">
                <div className="flex-1 min-h-0 overflow-auto p-2 space-y-3 font-mono text-xs">
                  <div className="flex flex-col gap-1">
                    <span className="text-[#8b949e]">User</span>
                    <div className="rounded bg-[#161b22] border border-[#30363d] px-2.5 py-2 text-[#e6edf3]">
                      How do I install Arch Linux with ZFS as the root filesystem? I have a single NVMe and want encryption.
                    </div>
                  </div>
                  <div className="flex flex-col gap-1">
                    <span className="text-[#8b949e]">dotAi</span>
                    <div className="rounded bg-[#0d1117] border border-[#30363d] px-2.5 py-2 text-[#e6edf3] space-y-1.5">
                      <p>High-level steps:</p>
                      <ol className="list-decimal list-inside space-y-0.5 text-[#8b949e]">
                        <li>Boot the Arch ISO and load ZFS modules: <code className="text-[#7ee787]">zfs</code>, <code className="text-[#7ee787]">zfs_arc_max</code> if needed.</li>
                        <li>Create a ZFS pool (e.g. <code className="text-[#7ee787]">zpool create -f -O encryption=aes-256-gcm -O keyformat=passphrase rpool /dev/nvme0n1</code>).</li>
                        <li>Create datasets: <code className="text-[#7ee787]">root</code>, <code className="text-[#7ee787]">root/var</code>, maybe <code className="text-[#7ee787]">home</code>.</li>
                        <li>Install base system into the pool, chroot, install <code className="text-[#7ee787]">zfs-dkms</code> + hooks, set up mkinitcpio and bootloader (e.g. systemd-boot) to load ZFS and unlock the pool.</li>
                      </ol>
                      <p className="text-[#8b949e]">I can give you exact commands for your layout (single disk, encryption) if you want.</p>
                    </div>
                  </div>
                  <div className="flex flex-col gap-1">
                    <span className="text-[#8b949e]">User</span>
                    <div className="rounded bg-[#161b22] border border-[#30363d] px-2.5 py-2 text-[#e6edf3]">
                      Yes, single NVMe, LUKS-style encryption. And use systemd-boot.
                    </div>
                  </div>
                  <div className="flex flex-col gap-1">
                    <span className="text-[#8b949e]">dotAi</span>
                    <div className="rounded bg-[#0d1117] border border-[#30363d] px-2.5 py-2 text-[#e6edf3] space-y-1.5">
                      <p>Here’s a minimal flow (single NVMe, ZFS native encryption, systemd-boot):</p>
                      <pre className="text-[#7ee787] overflow-x-auto text-[10px] whitespace-pre">{`# Load ZFS
modprobe zfs

# Create encrypted pool (replace nvme0n1 with your block device)
zpool create -f -o ashift=12 -O encryption=aes-256-gcm \\
  -O keyformat=passphrase -O keylocation=prompt -O mountpoint=none rpool /dev/nvme0n1

# Datasets
zfs create -o mountpoint=/ -o canmount=noauto rpool/root
zfs create rpool/root/arch
zfs create -o mountpoint=/home rpool/home
zfs mount rpool/root/arch`}</pre>
                      <p className="text-[#8b949e]">Then <code className="text-[#7ee787]">pacstrap</code>, chroot, install <code className="text-[#7ee787]">zfs-dkms linux linux-headers</code>, add <code className="text-[#7ee787]">zfs</code> to mkinitcpio hooks and to the bootloader cmdline. I can spell out the chroot + systemd-boot steps next.</p>
                    </div>
                  </div>
                </div>
                <div className="shrink-0 border-t border-[#1A1A1A] p-2 bg-[#0a0a0a]">
                  <div className="flex items-center gap-1.5 mb-1.5">
                    <button type="button" className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]" title="Image upload"><ImageIcon className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]" title="Voice input"><Mic className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5] flex items-center gap-1" title="Mode"><SlidersHorizontal className="w-3.5 h-3.5" /><span className="text-[10px]">mode</span></button>
                    <button type="button" className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5] flex items-center gap-1" title="Model"><Bot className="w-3.5 h-3.5" /><span className="text-[10px]">model</span><span className="text-[10px] text-[#8b949e] font-mono ml-0.5" title="Llama 3.2">L3.2</span></button>
                    <span className="flex items-center gap-0.5 text-[10px] text-[#666666] ml-auto" title="Context rotation"><Percent className="w-3 h-3" /> rot 0%</span>
                    <span className="flex items-center gap-0.5 text-[10px] text-[#666666]" title="Cost"><DollarSign className="w-3 h-3" /> $0.00</span>
                  </div>
                  <div className="w-full bg-[#0A0A0A] border border-[#1A1A1A] rounded-xl px-2.5 py-2 flex items-center gap-0.5 font-mono text-xs text-white">
                    <span className="text-white">Ask dotAi...</span>
                    <span className="inline-block w-2 h-3.5 bg-[#7ee787] animate-cursor-blink flex-shrink-0" aria-hidden />
                  </div>
                </div>
              </div>
              {/* Diagnostics: btop fills entire section */}
              <div className="flex-1 min-h-0 overflow-hidden flex flex-col">
                <MiniBtop />
              </div>
            </div>
          </div>

          {/* Right Sidebar - Threads: inline on desktop, overlay drawer on narrow */}
          {narrow ? (
            <AnimatePresence>
              {rightSidebarOpen && (
                <>
                  <div className="fixed inset-0 bg-black/60 z-40" aria-hidden onClick={() => setRightSidebarOpen(false)} />
                  <motion.div
                    initial={{ x: '100%' }}
                    animate={{ x: 0 }}
                    exit={{ x: '100%' }}
                    transition={{ type: 'tween', duration: 0.2 }}
                    className="fixed right-0 top-0 bottom-0 w-[min(260px,85vw)] bg-[#000000] border-l border-[#1A1A1A] z-50 flex flex-col shadow-xl"
                  >
                    <div className="w-full h-full flex flex-col min-w-0">
                      <div className="flex items-center justify-between gap-2 p-2 border-b border-[#1A1A1A] bg-[#0A0A0A] shrink-0">
                        <span className="flex items-center gap-1.5 text-xs font-mono text-[#666666] uppercase tracking-wide">
                          <HandMetal className="w-3.5 h-3.5 text-[#8b949e]" aria-hidden />
                          Threads
                        </span>
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
                </>
              )}
            </AnimatePresence>
          ) : (
          <motion.div
            initial={false}
            animate={{ width: rightSidebarOpen ? 220 : 0 }}
            className="bg-[#000000] border-l border-[#1A1A1A] overflow-hidden flex flex-col shrink-0"
          >
            <div className="w-[220px] h-full flex flex-col min-w-0">
              <div className="flex items-center justify-between gap-2 p-2 border-b border-[#1A1A1A] bg-[#0A0A0A] shrink-0">
                <span className="flex items-center gap-1.5 text-xs font-mono text-[#666666] uppercase tracking-wide">
                  <HandMetal className="w-3.5 h-3.5 text-[#8b949e]" aria-hidden />
                  Threads
                </span>
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
          )}
        </div>

        {/* Status bar: only show/hide left, bottom, and right sidebar */}
        <div className="flex items-center gap-0.5 px-2 py-1 border-t border-[#1A1A1A] bg-[#0A0A0A] shrink-0 min-h-[28px] text-[#666666]">
          <button onClick={() => setLeftSidebarOpen(!leftSidebarOpen)} className={`p-1.5 rounded ${leftSidebarOpen ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`} title="Toggle left sidebar"><PanelLeft className="w-3.5 h-3.5" /></button>
          <button onClick={() => setTerminalOpen(!terminalOpen)} className={`p-1.5 rounded ${terminalOpen ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`} title="Toggle bottom panel"><PanelBottom className="w-3.5 h-3.5" /></button>
          <button onClick={() => setRightSidebarOpen(!rightSidebarOpen)} className={`p-1.5 rounded ${rightSidebarOpen ? 'bg-[#E5E5E5]/10 text-[#E5E5E5]' : 'hover:bg-[#1A1A1A] hover:text-[#E5E5E5]'}`} title="Toggle right sidebar"><PanelRight className="w-3.5 h-3.5" /></button>
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

function EditorPaneMock() {
  return (
    <div className="h-full flex flex-col bg-[#0d1117] border border-[#30363d] font-mono text-sm overflow-hidden">
      <div className="px-3 py-1.5 border-b border-[#30363d] text-[#8b949e] shrink-0">file.md — NORMAL</div>
      <pre className="p-3 text-[#e6edf3] whitespace-pre flex-1 min-h-0 overflow-auto">1  │</pre>
    </div>
  );
}

function TerminalPaneMock() {
  return (
    <div className="h-full flex flex-col bg-[#0d1117] border border-[#30363d] font-mono text-sm overflow-hidden">
      <div className="px-3 py-1.5 border-b border-[#30363d] text-[#8b949e] shrink-0">xonsh — Terminal</div>
      <pre className="p-3 text-[#7ee787] flex-1 min-h-0 overflow-auto">$ <span className="text-[#e6edf3]">_</span></pre>
    </div>
  );
}

function AgentPaneMock() {
  return (
    <div className="h-full flex flex-col bg-[#0d1117] border border-[#30363d] overflow-hidden p-4">
      <div className="flex items-center gap-2 text-[#8b949e] font-mono text-sm mb-4">
        <Bot className="w-5 h-5 text-[#10B981]" /> Agent
      </div>
      <div className="flex-1 rounded border border-[#30363d] bg-[#161b22] p-4 text-[#e6edf3] text-sm">
        Agent pane (mock) — run tasks, tools, commits.
      </div>
    </div>
  );
}

function PlannerPaneMock() {
  return (
    <div className="h-full flex flex-col bg-[#0d1117] border border-[#30363d] overflow-hidden p-4">
      <div className="flex items-center gap-2 text-[#8b949e] font-mono text-sm mb-4">
        <LayoutList className="w-5 h-5 text-[#0EA5E9]" /> Planner
      </div>
      <div className="flex-1 rounded border border-[#30363d] bg-[#161b22] p-4 text-[#e6edf3] text-sm">
        Planner pane (mock) — task planning, roadmap view.
      </div>
    </div>
  );
}

function ChatPaneMock() {
  return (
    <div className="h-full flex flex-col bg-[#0d1117] border border-[#30363d] overflow-hidden p-4">
      <div className="flex-1 rounded border border-[#30363d] bg-[#161b22] p-3 font-mono text-sm text-[#e6edf3]">
        Ask dotAi anything...
      </div>
      <div className="text-xs text-[#8b949e] font-mono mt-2 shrink-0">New chat thread (mock)</div>
    </div>
  );
}

function AssistantPaneMock() {
  return (
    <div className="h-full flex flex-col bg-[#0d1117] border border-[#30363d] overflow-hidden p-4">
      <div className="flex items-center gap-2 text-[#8b949e] font-mono text-sm mb-4">
        <Zap className="w-5 h-5 text-[#F59E0B]" /> Assistant
      </div>
      <div className="flex-1 rounded border border-[#30363d] bg-[#161b22] p-4 text-[#e6edf3] text-sm">
        Assistant pane (mock) — Q&A, lightweight help.
      </div>
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

/** NixOS-style config file: edit this file to configure the application (no GUI settings). */
const MOCK_CONFIG_PATH = '~/.config/dotai/config.nix';

const MOCK_CONFIG_CONTENT = `# dotAi application configuration (NixOS-style)
# Edit this file to change behaviour. Restart or run :reload to apply.

{ config, pkgs, ... }:

{
  # Backend & model
  dotai.backend = "llama-server";
  dotai.endpoint = "http://localhost:11434";
  dotai.strictlyLocal = true;

  # Security & execution
  dotai.executionLevel = "ask";  # ask | auto-safe | always-ask

  # Session
  dotai.restoreSession = true;
}
`;

function ConfigFileView({ onBack }: { onBack: () => void }) {
  return (
    <div className="bg-[#000000] overflow-hidden flex flex-col h-full min-h-0">
      <div className="bg-[#0A0A0A] border-b border-[#1A1A1A] px-4 py-2 flex items-center justify-between shrink-0">
        <button onClick={onBack} className="flex items-center gap-2 text-sm text-[#666666] hover:text-[#E5E5E5] transition-colors">
          <ChevronLeft className="w-4 h-4" />
          <span>Back</span>
        </button>
        <span className="text-xs font-mono text-[#8b949e]">Edit config file to change settings</span>
      </div>
      <div className="px-4 py-2 border-b border-[#1A1A1A] bg-[#0A0A0A]/50 shrink-0">
        <div className="font-mono text-xs text-[#79c0ff]">{MOCK_CONFIG_PATH}</div>
      </div>
      <div className="flex-1 min-h-0 overflow-auto p-4">
        <pre className="font-mono text-xs text-[#e6edf3] whitespace-pre leading-relaxed bg-[#0d1117] border border-[#30363d] rounded-lg p-4">
          <code>{MOCK_CONFIG_CONTENT}</code>
        </pre>
      </div>
    </div>
  );
}
