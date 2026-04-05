import { useMemo } from 'react';
import { motion } from 'motion/react';

export const STAR_PALETTE = [
  '#FAD075',
  '#FDE8B0',
  '#FFFFFF',
  '#CCDDFF',
  '#FFEECC',
  '#E8D0FF',
];

function mulberry32(seed: number) {
  let s = seed | 0;
  return () => {
    s = (s + 0x6d2b79f5) | 0;
    let t = Math.imul(s ^ (s >>> 15), 1 | s);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}

type StarDot = {
  nx: number;
  ny: number;
  size: number;
  alpha: number;
  colorIdx: number;
  twinkleDelay: number;
  twinkleDuration: number;
};

type Sparkle = {
  nx: number;
  ny: number;
  colorIdx: number;
  rayLength: number;
};

export function generateStarLayout(seed: number, density: number, sparkleCount: number) {
  const rng = mulberry32(seed);
  const totalDots = Math.round(density * 1.2);

  const dots: StarDot[] = [];
  for (let i = 0; i < totalDots; i++) {
    dots.push({
      nx: rng(),
      ny: rng(),
      size: 0.5 + rng() * 1.5,
      alpha: 0.15 + rng() * 0.55,
      colorIdx: Math.floor(rng() * STAR_PALETTE.length),
      twinkleDelay: rng() * 6,
      twinkleDuration: 2 + rng() * 4,
    });
  }

  const sparkles: Sparkle[] = [];
  for (let i = 0; i < sparkleCount; i++) {
    sparkles.push({
      nx: rng(),
      ny: rng(),
      colorIdx: Math.floor(rng() * STAR_PALETTE.length),
      rayLength: 2 + Math.floor(rng() * 3),
    });
  }

  return { dots, sparkles };
}

export function useStarField(seed = 42, density = 120, sparkleCount = 8) {
  return useMemo(() => generateStarLayout(seed, density, sparkleCount), [seed, density, sparkleCount]);
}

function SparkleIcon({ sparkle }: { sparkle: Sparkle }) {
  const color = STAR_PALETTE[sparkle.colorIdx];
  const ps = 3;
  const rl = sparkle.rayLength;
  const size = (rl * 2 + 1) * ps;
  const center = rl * ps;

  const rects: { x: number; y: number; opacity: number }[] = [];

  rects.push({ x: center, y: center, opacity: 0.85 });

  for (let r = 1; r <= rl; r++) {
    const fade = (1 - r / (rl + 1)) * 0.85;
    rects.push({ x: center, y: center - r * ps, opacity: fade });
    rects.push({ x: center, y: center + r * ps, opacity: fade });
    rects.push({ x: center - r * ps, y: center, opacity: fade });
    rects.push({ x: center + r * ps, y: center, opacity: fade });
  }

  const diagLen = Math.max(1, Math.floor(rl / 2));
  for (let r = 1; r <= diagLen; r++) {
    const fade = (1 - r / (diagLen + 1)) * 0.85 * 0.5;
    rects.push({ x: center - r * ps, y: center - r * ps, opacity: fade });
    rects.push({ x: center + r * ps, y: center - r * ps, opacity: fade });
    rects.push({ x: center - r * ps, y: center + r * ps, opacity: fade });
    rects.push({ x: center + r * ps, y: center + r * ps, opacity: fade });
  }

  return (
    <motion.svg
      className="absolute"
      style={{
        left: `calc(${sparkle.nx * 100}% - ${size / 2}px)`,
        top: `calc(${sparkle.ny * 100}% - ${size / 2}px)`,
      }}
      width={size}
      height={size}
      initial={{ opacity: 0 }}
      animate={{ opacity: [0, 1, 0] }}
      transition={{
        delay: 1 + sparkle.nx * 3,
        duration: 3 + sparkle.ny * 2,
        repeat: Infinity,
        ease: 'easeInOut',
      }}
    >
      {rects.map((r, j) => (
        <rect
          key={j}
          x={r.x}
          y={r.y}
          width={ps}
          height={ps}
          fill={color}
          opacity={r.opacity}
        />
      ))}
    </motion.svg>
  );
}

export function StarField({ dots, sparkles }: { dots: ReturnType<typeof generateStarLayout>['dots']; sparkles: ReturnType<typeof generateStarLayout>['sparkles'] }) {
  return (
    <div className="absolute inset-0 overflow-hidden" aria-hidden>
      {dots.map((star, i) => (
        <motion.div
          key={`d${i}`}
          className="absolute rounded-full"
          style={{
            left: `${star.nx * 100}%`,
            top: `${star.ny * 100}%`,
            width: star.size,
            height: star.size,
            backgroundColor: STAR_PALETTE[star.colorIdx],
          }}
          initial={{ opacity: 0 }}
          animate={{ opacity: [0, star.alpha, 0] }}
          transition={{
            delay: star.twinkleDelay,
            duration: star.twinkleDuration,
            repeat: Infinity,
            ease: 'easeInOut',
          }}
        />
      ))}
      {sparkles.map((sp, i) => (
        <SparkleIcon key={`s${i}`} sparkle={sp} />
      ))}
    </div>
  );
}
