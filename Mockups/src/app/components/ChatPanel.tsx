import { useState, useRef, useEffect } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import {
  Plus,
  X,
  Square,
  Pencil,
  ChevronRight,
  CheckSquare,
  LayoutList,
  Files,
  FileCode,
  Image as ImageIcon,
  Mic,
  SlidersHorizontal,
  Bot,
  Percent,
  DollarSign,
} from 'lucide-react';

export type ChatTab = {
  id: string;
  title: string;
  isRunning: boolean;
  runningPrompt?: string;
  todos: { id: string; label: string; done: boolean }[];
  plans: { id: string; title: string; summary: string }[];
  files: { name: string; reviewed: boolean }[];
  messages: { role: 'user' | 'assistant'; content: string; codeBlock?: string }[];
};

const MOCK_TABS: ChatTab[] = [
  {
    id: 'arch-zfs',
    title: 'Arch ZFS Install',
    isRunning: true,
    runningPrompt: 'Yes, single NVMe, LUKS-style encryption. And use systemd-boot.',
    todos: [
      { id: 't1', label: 'Set up ZFS pool', done: true },
      { id: 't2', label: 'Install base system', done: true },
      { id: 't3', label: 'Configure bootloader', done: false },
      { id: 't4', label: 'Test reboot', done: false },
    ],
    plans: [
      { id: 'p1', title: 'ZFS Encryption Setup Plan', summary: 'Pool creation, datasets, encryption keys' },
      { id: 'p2', title: 'Bootloader Config Plan', summary: 'systemd-boot, mkinitcpio, kernel hooks' },
    ],
    files: [
      { name: 'zfs-setup.sh', reviewed: true },
      { name: 'mkinitcpio.conf', reviewed: false },
      { name: 'loader.conf', reviewed: false },
    ],
    messages: [
      {
        role: 'user',
        content: 'How do I install Arch Linux with ZFS as the root filesystem? I have a single NVMe and want encryption.',
      },
      {
        role: 'assistant',
        content:
          'High-level steps:\n\n1. Boot the Arch ISO and load ZFS modules: zfs, zfs_arc_max if needed.\n2. Create a ZFS pool (e.g. zpool create -f -O encryption=aes-256-gcm -O keyformat=passphrase rpool /dev/nvme0n1).\n3. Create datasets: root, root/var, maybe home.\n4. Install base system into the pool, chroot, install zfs-dkms + hooks, set up mkinitcpio and bootloader (e.g. systemd-boot) to load ZFS and unlock the pool.\n\nI can give you exact commands for your layout (single disk, encryption) if you want.',
      },
      {
        role: 'user',
        content: 'Yes, single NVMe, LUKS-style encryption. And use systemd-boot.',
      },
      {
        role: 'assistant',
        content:
          "Here's a minimal flow (single NVMe, ZFS native encryption, systemd-boot):\n\nThen pacstrap, chroot, install zfs-dkms linux linux-headers, add zfs to mkinitcpio hooks and to the bootloader cmdline. I can spell out the chroot + systemd-boot steps next.",
        codeBlock: `# Load ZFS
modprobe zfs

# Create encrypted pool (replace nvme0n1 with your block device)
zpool create -f -o ashift=12 -O encryption=aes-256-gcm \\
  -O keyformat=passphrase -O keylocation=prompt -O mountpoint=none rpool /dev/nvme0n1

# Datasets
zfs create -o mountpoint=/ -o canmount=noauto rpool/root
zfs create rpool/root/arch
zfs create -o mountpoint=/home rpool/home
zfs mount rpool/root/arch`,
      },
    ],
  },
  {
    id: 'mvp-prd',
    title: 'MVP PRD Review',
    isRunning: false,
    todos: [],
    plans: [],
    files: [],
    messages: [
      { role: 'user', content: 'Outline MVP PRD for the project.' },
      { role: 'assistant', content: 'PRD structure and sections (mock).' },
    ],
  },
  {
    id: 'new',
    title: 'New Chat',
    isRunning: false,
    todos: [],
    plans: [],
    files: [],
    messages: [],
  },
];

function CollapsibleSection({
  label,
  icon: Icon,
  count,
  defaultOpen = false,
  children,
}: {
  label: string;
  icon: React.ComponentType<{ className?: string; size?: number }>;
  count: number;
  defaultOpen?: boolean;
  children: React.ReactNode;
}) {
  const [open, setOpen] = useState(defaultOpen);

  return (
    <div className="border-b border-[#1A1A1A]">
      <button
        type="button"
        onClick={() => setOpen((o) => !o)}
        className="w-full flex items-center gap-2 px-2 py-1.5 text-xs font-mono text-[#8b949e] hover:bg-[#161b22] hover:text-[#e6edf3] transition-colors"
      >
        <ChevronRight
          className={`w-3.5 h-3.5 shrink-0 transition-transform ${open ? 'rotate-90' : ''}`}
        />
        <Icon className="w-3.5 h-3.5 shrink-0" />
        <span className="flex-1 text-left">{label}</span>
        {!open && count > 0 && (
          <span className="text-[#666666] font-mono">{count}</span>
        )}
      </button>
      <AnimatePresence initial={false}>
        {open && (
          <motion.div
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: 'auto', opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            transition={{ duration: 0.15 }}
            className="overflow-hidden"
          >
            <div className="px-2 pb-2">{children}</div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}

function getFileColor(name: string): string {
  const ext = name.includes('.') ? name.slice(name.lastIndexOf('.') + 1) : '';
  if (ext === 'sh') return '#7ee787';
  if (ext === 'conf') return '#79c0ff';
  return '#e6edf3';
}

type ChatPanelProps = { narrow?: boolean };

export function ChatPanel({ narrow = false }: ChatPanelProps) {
  const [tabs, setTabs] = useState<ChatTab[]>(MOCK_TABS);
  const [activeTabId, setActiveTabId] = useState<string>(MOCK_TABS[0].id);
  const [highlightedFile, setHighlightedFile] = useState<string | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const activeTab = tabs.find((t) => t.id === activeTabId) ?? tabs[0];

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [activeTab?.messages.length]);

  const addChat = () => {
    const id = `tab-${Date.now()}`;
    setTabs((prev) => [
      ...prev,
      {
        id,
        title: 'New Chat',
        isRunning: false,
        todos: [],
        plans: [],
        files: [],
        messages: [],
      },
    ]);
    setActiveTabId(id);
  };

  const containerClass = narrow
    ? 'flex flex-col border-b border-[#1A1A1A] bg-[#0a0a0a]'
    : 'flex-1 min-h-0 flex flex-col border-b border-[#1A1A1A] bg-[#0a0a0a] overflow-hidden';

  return (
    <div className={containerClass}>
      {/* Chat tab bar */}
      <div className="flex items-center gap-0.5 border-b border-[#1A1A1A] bg-[#0A0A0A] px-2 py-1 shrink-0 overflow-x-auto scrollbar-hide">
        {tabs.map((tab) => {
          const isActive = tab.id === activeTabId;
          return (
            <button
              key={tab.id}
              type="button"
              onClick={() => setActiveTabId(tab.id)}
              className={`shrink-0 px-2.5 py-1.5 rounded-t text-xs font-mono flex items-center gap-1.5 transition-colors ${
                isActive
                  ? 'bg-[#0a0a0a] text-[#E5E5E5] border border-[#1A1A1A] border-b-transparent -mb-px'
                  : 'text-[#666666] hover:text-[#E5E5E5] hover:bg-[#1A1A1A]'
              }`}
            >
              {tab.isRunning && (
                <span
                  className="w-1.5 h-1.5 rounded-full bg-[#D4A843] animate-pulse shrink-0"
                  aria-hidden
                />
              )}
              <span className="truncate max-w-[120px] sm:max-w-[160px]">{tab.title}</span>
            </button>
          );
        })}
        <button
          type="button"
          onClick={addChat}
          className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5] shrink-0"
          title="New chat"
          aria-label="New chat"
        >
          <Plus className="w-3.5 h-3.5" />
        </button>
      </div>

      {/* Running prompt pill */}
      <AnimatePresence>
        {activeTab.isRunning && activeTab.runningPrompt && (
          <motion.div
            initial={{ opacity: 0, y: -8 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -8 }}
            className="mx-2 mt-2 shrink-0 flex items-center gap-2 rounded-lg border border-[#D4A843] bg-[#161b22] px-2.5 py-2"
          >
            <p className="flex-1 min-w-0 text-xs font-mono text-[#e6edf3] truncate">
              {activeTab.runningPrompt}
            </p>
            <button
              type="button"
              className="p-1 rounded text-[#8b949e] hover:text-[#e6edf3] hover:bg-[#30363d] shrink-0"
              title="Edit"
              aria-label="Edit prompt"
            >
              <Pencil className="w-3 h-3" />
            </button>
            <button
              type="button"
              className="p-1 rounded text-[#8b949e] hover:text-[#E8728A] hover:bg-[#30363d] shrink-0"
              title="Stop"
              aria-label="Stop"
            >
              <Square className="w-3 h-3" />
            </button>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Collapsible sections: To-Do, Plans, Files */}
      <div className="shrink-0 border-b border-[#1A1A1A] bg-[#0A0A0A]/80">
        <CollapsibleSection
          label="To-Do"
          icon={CheckSquare}
          count={activeTab.todos.length}
          defaultOpen={false}
        >
          <div className="space-y-1">
            {activeTab.todos.map((todo) => (
              <label
                key={todo.id}
                className="flex items-center gap-2 py-0.5 text-xs font-mono text-[#e6edf3] cursor-pointer hover:text-[#5EC4AB]"
              >
                <input
                  type="checkbox"
                  checked={todo.done}
                  readOnly
                  className="rounded border-[#30363d] bg-[#0d1117] text-[#5EC4AB]"
                />
                <span className={todo.done ? 'text-[#8b949e] line-through' : ''}>
                  {todo.label}
                </span>
              </label>
            ))}
          </div>
        </CollapsibleSection>
        <CollapsibleSection
          label="Plans"
          icon={LayoutList}
          count={activeTab.plans.length}
          defaultOpen={false}
        >
          <div className="space-y-2">
            {activeTab.plans.map((plan) => (
              <div
                key={plan.id}
                className="rounded border border-[#30363d] bg-[#161b22] px-2 py-1.5 text-xs font-mono"
              >
                <div className="text-[#e6edf3]">{plan.title}</div>
                <div className="text-[#8b949e] mt-0.5">{plan.summary}</div>
              </div>
            ))}
          </div>
        </CollapsibleSection>
        <CollapsibleSection
          label="Files for Review"
          icon={Files}
          count={activeTab.files.length}
          defaultOpen={false}
        >
          <div className="space-y-1">
            {activeTab.files.map((file) => (
              <div
                key={file.name}
                className={`flex items-center gap-2 py-1.5 px-2 rounded text-xs font-mono transition-colors ${
                  highlightedFile === file.name ? 'bg-[#30363d]' : 'hover:bg-[#161b22]'
                }`}
              >
                <FileCode
                  className="w-3.5 h-3.5 shrink-0"
                  style={{ color: getFileColor(file.name) }}
                />
                <span className="flex-1 min-w-0 truncate text-[#e6edf3]">{file.name}</span>
                <button
                  type="button"
                  onClick={() => setHighlightedFile(file.name)}
                  className="shrink-0 text-[10px] text-[#5EC4AB] hover:underline"
                >
                  Review
                </button>
              </div>
            ))}
          </div>
        </CollapsibleSection>
      </div>

      {/* Message area */}
      <div
        className={`flex-1 min-h-0 overflow-auto p-2 space-y-3 font-mono text-xs ${
          narrow ? 'max-h-[280px]' : ''
        }`}
      >
        {activeTab.messages.length === 0 ? (
          <div className="text-[#666666] text-center py-6">No messages yet. Ask Mystic below.</div>
        ) : (
          activeTab.messages.map((msg, i) => (
            <div key={i} className="flex flex-col gap-1">
              <span className="text-[#8b949e]">
                {msg.role === 'user' ? 'User' : 'Mystic'}
              </span>
              <div
                className={`rounded border border-[#30363d] px-2.5 py-2 text-[#e6edf3] space-y-1.5 ${
                  msg.role === 'user' ? 'bg-[#161b22]' : 'bg-[#0d1117]'
                }`}
              >
                <div className="whitespace-pre-wrap">{msg.content}</div>
                {msg.codeBlock && (
                  <pre className="text-[#7ee787] overflow-x-auto text-[10px] whitespace-pre mt-1.5 pt-1.5 border-t border-[#30363d]">
                    {msg.codeBlock}
                  </pre>
                )}
              </div>
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Command input bubble */}
      <div className="shrink-0 border-t border-[#1A1A1A] p-2 bg-[#0a0a0a]">
        {!narrow && (
          <div className="flex items-center gap-1.5 mb-1.5">
            <button
              type="button"
              className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]"
              title="Image upload"
            >
              <ImageIcon className="w-3.5 h-3.5" />
            </button>
            <button
              type="button"
              className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5]"
              title="Voice input"
            >
              <Mic className="w-3.5 h-3.5" />
            </button>
            <button
              type="button"
              className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5] flex items-center gap-1"
              title="Mode"
            >
              <SlidersHorizontal className="w-3.5 h-3.5" />
              <span className="text-[10px]">mode</span>
            </button>
            <button
              type="button"
              className="p-1.5 rounded text-[#666666] hover:bg-[#1A1A1A] hover:text-[#E5E5E5] flex items-center gap-1"
              title="Model"
            >
              <Bot className="w-3.5 h-3.5" />
              <span className="text-[10px]">model</span>
              <span className="text-[10px] text-[#8b949e] font-mono ml-0.5" title="Llama 3.2">
                L3.2
              </span>
            </button>
            <span
              className="flex items-center gap-0.5 text-[10px] text-[#666666] ml-auto"
              title="Context rotation"
            >
              <Percent className="w-3 h-3" /> rot 0%
            </span>
            <span className="flex items-center gap-0.5 text-[10px] text-[#666666]" title="Cost">
              <DollarSign className="w-3 h-3" /> $0.00
            </span>
          </div>
        )}
        <div
          className={`w-full bg-[#0A0A0A] border border-[#1A1A1A] rounded-xl flex items-center gap-0.5 font-mono text-xs text-white ${
            narrow ? 'px-3 py-2.5 touch-manipulation' : 'px-2.5 py-2'
          }`}
        >
          <span className="text-white">Ask Mystic...</span>
          <span
            className="inline-block w-2 h-3.5 bg-[#7ee787] animate-cursor-blink flex-shrink-0"
            aria-hidden
          />
        </div>
      </div>
    </div>
  );
}
