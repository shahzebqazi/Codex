import { useState, useEffect } from 'react';
import { motion } from 'motion/react';
import { StarField, useStarField } from './components/StarField';

const TITLE_STORAGE_KEY = 'mystic-title-seen';

export function hasSeenTitle(): boolean {
  try {
    return sessionStorage.getItem(TITLE_STORAGE_KEY) === '1';
  } catch {
    return false;
  }
}

export function markTitleSeen(): void {
  try {
    sessionStorage.setItem(TITLE_STORAGE_KEY, '1');
  } catch {
    // ignore
  }
}

type TitlePageProps = {
  onStart: () => void;
};

export function TitlePage({ onStart }: TitlePageProps) {
  const { dots, sparkles } = useStarField();
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const t = setTimeout(() => setReady(true), 100);
    return () => clearTimeout(t);
  }, []);

  const basePath = (import.meta as Record<string, unknown>).env &&
    typeof (import.meta as Record<string, Record<string, string>>).env?.BASE_URL === 'string'
    ? (import.meta as Record<string, Record<string, string>>).env.BASE_URL
    : '/';

  if (!ready) return <div className="min-h-screen bg-[#0A0A0B]" />;

  return (
    <div className="min-h-screen overflow-hidden relative bg-[#0A0A0B] flex items-center justify-center p-6 sm:p-12">
      <StarField dots={dots} sparkles={sparkles} />

      <div
        className="absolute inset-0 opacity-20 pointer-events-none"
        style={{
          background:
            'radial-gradient(ellipse 60% 50% at 50% 45%, rgba(212,168,67,0.15) 0%, transparent 70%)',
        }}
      />

      <motion.main
        className="relative z-10 max-w-lg w-full flex flex-col items-center text-center"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.8, ease: [0.22, 1, 0.36, 1] }}
      >
        <motion.div
          initial={{ opacity: 0, scale: 0.85 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.2, duration: 0.7, ease: [0.22, 1, 0.36, 1] }}
          className="mb-6"
        >
          <img
            src={`${basePath}brand/pixel-icon-128.png`}
            alt="Mystic — crescent moon"
            width={96}
            height={96}
            style={{ imageRendering: 'pixelated' }}
          />
        </motion.div>

        <motion.h1
          className="text-3xl sm:text-4xl font-medium tracking-tight mb-2"
          style={{ color: '#FAFAFA', letterSpacing: '0.03em' }}
          initial={{ opacity: 0, y: 16 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.35, duration: 0.5, ease: [0.22, 1, 0.36, 1] }}
        >
          Mystic
        </motion.h1>

        <motion.p
          className="text-sm tracking-widest uppercase mb-10"
          style={{ color: '#D4A843' }}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.5, duration: 0.5 }}
        >
          Notes &middot; Code &middot; Design
        </motion.p>

        <motion.div
          className="space-y-4 text-sm sm:text-base leading-relaxed text-left max-w-md"
          style={{ color: '#A1A1AA' }}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.65, duration: 0.6 }}
        >
          <p>
            Major AI labs—Google, OpenAI, and Anthropic—are involved in military
            and mass surveillance contracts. Their systems are increasingly used
            for targeting, monitoring, and control at scale.
          </p>
          <p>
            We believe machines need human beings to know what is true and what
            is wrong. Please use AI responsibly and with a conscience.{' '}
            <span
              className="font-medium cursor-pointer"
              style={{ color: '#5EC4AB' }}
              onClick={() => {
                markTitleSeen();
                onStart();
              }}
            >
              Enter Mystic.
            </span>
          </p>
        </motion.div>
      </motion.main>
    </div>
  );
}
