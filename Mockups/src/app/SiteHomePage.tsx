import { useState, useEffect } from 'react';
import { Link } from 'react-router';
import { motion } from 'motion/react';
import { ArrowRight, Sparkles } from 'lucide-react';
import { StarField, useStarField } from './components/StarField';

const basePath =
  typeof import.meta !== 'undefined' && import.meta.env?.BASE_URL != null
    ? import.meta.env.BASE_URL
    : '/';

export function SiteHomePage() {
  const { dots, sparkles } = useStarField();
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const t = setTimeout(() => setReady(true), 80);
    return () => clearTimeout(t);
  }, []);

  if (!ready) {
    return <div className="min-h-dvh bg-[#0A0A0B]" />;
  }

  return (
    <div className="min-h-dvh overflow-x-hidden relative bg-[#0A0A0B] text-[#FAFAFA]">
      <StarField dots={dots} sparkles={sparkles} />

      <div
        className="absolute inset-0 opacity-[0.22] pointer-events-none"
        style={{
          background:
            'radial-gradient(ellipse 70% 55% at 50% 20%, rgba(212,168,67,0.12) 0%, transparent 55%), radial-gradient(ellipse 50% 40% at 80% 80%, rgba(94,196,171,0.06) 0%, transparent 50%)',
        }}
      />

      <header className="relative z-20 border-b border-[#27272A]/80 backdrop-blur-md bg-[#0A0A0B]/75">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 h-14 flex items-center justify-between gap-4">
          <Link
            to="/"
            className="flex items-center gap-2 text-sm font-medium tracking-[0.03em] text-[#FAFAFA] hover:text-[#D4A843] transition-colors"
          >
            <img
              src={`${basePath}brand/pixel-icon-64.png`}
              alt=""
              width={28}
              height={28}
              className="shrink-0"
              style={{ imageRendering: 'pixelated' }}
            />
            Mystic
          </Link>
          <nav className="flex items-center gap-1 sm:gap-3 text-sm">
            <a
              href="#values"
              className="hidden sm:inline text-[#A1A1AA] hover:text-[#D4A843] px-2 py-1 rounded transition-colors"
            >
              Values
            </a>
            <a
              href="#product"
              className="hidden sm:inline text-[#A1A1AA] hover:text-[#D4A843] px-2 py-1 rounded transition-colors"
            >
              Product
            </a>
            <Link
              to="/app"
              className="inline-flex items-center gap-1.5 rounded-lg bg-[#D4A843]/15 text-[#E8C76A] border border-[#D4A843]/35 px-3 py-1.5 font-medium hover:bg-[#D4A843]/25 transition-colors"
            >
              Open app
              <ArrowRight className="w-4 h-4" aria-hidden />
            </Link>
          </nav>
        </div>
      </header>

      <main className="relative z-10">
        <section className="max-w-6xl mx-auto px-4 sm:px-6 pt-16 sm:pt-24 pb-20 sm:pb-28">
          <motion.div
            initial={{ opacity: 0, y: 24 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.65, ease: [0.22, 1, 0.36, 1] }}
            className="max-w-3xl"
          >
            <p className="text-xs sm:text-sm tracking-[0.2em] uppercase text-[#D4A843] mb-4 flex items-center gap-2">
              <Sparkles className="w-4 h-4 opacity-80" aria-hidden />
              Notes · Code · Design
            </p>
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-medium tracking-tight leading-[1.1] mb-6 text-[#FAFAFA]">
              A calmer place to think with your tools and your conscience.
            </h1>
            <p className="text-lg sm:text-xl text-[#A1A1AA] leading-relaxed mb-10 max-w-2xl">
              Mystic is an AI workspace mockup for people who want clarity—not noise. Plan, chat, and ship
              without surrendering judgment to black boxes.
            </p>
            <div className="flex flex-col sm:flex-row gap-3 sm:items-center">
              <Link
                to="/app"
                className="inline-flex items-center justify-center gap-2 rounded-xl bg-[#D4A843] text-[#0A0A0B] px-6 py-3.5 font-medium hover:bg-[#E8C76A] transition-colors shadow-lg shadow-[#D4A843]/15"
              >
                Enter the workspace
                <ArrowRight className="w-4 h-4" aria-hidden />
              </Link>
              <a
                href="#values"
                className="inline-flex items-center justify-center rounded-xl border border-[#27272A] text-[#E4E4E7] px-6 py-3.5 font-medium hover:border-[#D4A843]/50 hover:text-[#FAFAFA] transition-colors"
              >
                Read our stance
              </a>
            </div>
          </motion.div>
        </section>

        <section id="values" className="border-t border-[#27272A]/60 bg-[#0A0A0B]/80">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 py-16 sm:py-20">
            <h2 className="text-2xl sm:text-3xl font-medium text-[#FAFAFA] mb-3">Responsibility first</h2>
            <p className="text-[#A1A1AA] max-w-2xl mb-10 leading-relaxed">
              Major AI labs are entangled with military and mass surveillance contracts. Their systems are
              increasingly used for targeting, monitoring, and control at scale.
            </p>
            <p className="text-[#A1A1AA] max-w-2xl mb-12 leading-relaxed">
              We believe machines need human beings to know what is true and what is wrong. Please use AI
              responsibly and with a conscience.
            </p>
            <div className="grid sm:grid-cols-3 gap-6">
              {[
                {
                  title: 'Human judgment',
                  body: 'Tools amplify intent—they do not replace ethics. You stay in the loop.',
                },
                {
                  title: 'Transparent workflows',
                  body: 'Threads, files, and plans in one surface so you can see what changed and why.',
                },
                {
                  title: 'Built for focus',
                  body: 'A dark, minimal shell so your attention stays on the work—not the chrome.',
                },
              ].map((card, i) => (
                <motion.article
                  key={card.title}
                  initial={{ opacity: 0, y: 16 }}
                  whileInView={{ opacity: 1, y: 0 }}
                  viewport={{ once: true, margin: '-40px' }}
                  transition={{ delay: i * 0.08, duration: 0.45, ease: [0.22, 1, 0.36, 1] }}
                  className="rounded-2xl border border-[#27272A] bg-[#111113]/90 p-6 hover:border-[#D4A843]/25 transition-colors"
                >
                  <h3 className="text-[#E4E4E7] font-medium mb-2">{card.title}</h3>
                  <p className="text-sm text-[#A1A1AA] leading-relaxed">{card.body}</p>
                </motion.article>
              ))}
            </div>
          </div>
        </section>

        <section id="product" className="border-t border-[#27272A]/60">
          <div className="max-w-6xl mx-auto px-4 sm:px-6 py-16 sm:py-20">
            <h2 className="text-2xl sm:text-3xl font-medium text-[#FAFAFA] mb-4">What you&apos;ll find inside</h2>
            <p className="text-[#A1A1AA] max-w-2xl mb-10 leading-relaxed">
              The interactive mockup includes chat, explorer, threads, widgets, and more—laid out like a
              serious desktop app. It&apos;s a prototype, not a production backend.
            </p>
            <ul className="grid sm:grid-cols-2 gap-4 max-w-3xl font-mono text-sm text-[#E4E4E7]">
              <li className="flex gap-2">
                <span className="text-[#5EC4AB] shrink-0">—</span>
                Multi-tab workspace with editor, terminal, graph, and planner panes
              </li>
              <li className="flex gap-2">
                <span className="text-[#5EC4AB] shrink-0">—</span>
                Resizable terminal panel and optional home-screen widgets
              </li>
              <li className="flex gap-2">
                <span className="text-[#5EC4AB] shrink-0">—</span>
                Thread list and mock conversations for UX review
              </li>
              <li className="flex gap-2">
                <span className="text-[#5EC4AB] shrink-0">—</span>
                Responsive narrow layout for phones and tablets
              </li>
            </ul>
            <div className="mt-12">
              <Link
                to="/app"
                className="inline-flex items-center gap-2 text-[#D4A843] font-medium hover:text-[#E8C76A] transition-colors"
              >
                Launch the mockup
                <ArrowRight className="w-4 h-4" aria-hidden />
              </Link>
            </div>
          </div>
        </section>

        <footer className="border-t border-[#27272A]/60 py-10 text-center text-xs text-[#71717A] font-mono">
          Mystic — mockups for design and narrative. Not affiliated with any of the labs named above.
        </footer>
      </main>
    </div>
  );
}
