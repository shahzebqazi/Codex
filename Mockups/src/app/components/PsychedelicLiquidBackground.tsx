import { useEffect, useRef, useState } from 'react';

/**
 * Black metallic psychedelic rainbow liquid background.
 * Inspired by black/white moving abstraction, calm and flowing.
 * Falls back to solid black if canvas is unavailable or errors.
 */
export function PsychedelicLiquidBackground() {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [failed, setFailed] = useState(false);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas || failed) return;

    const ctx = canvas.getContext('2d', { alpha: false });
    if (!ctx) {
      setFailed(true);
      return;
    }

    let animationId: number;
    let t = 0;

    const resize = () => {
      const dpr = Math.min(window.devicePixelRatio ?? 1, 2);
      const w = window.innerWidth;
      const h = window.innerHeight;
      canvas.width = w * dpr;
      canvas.height = h * dpr;
      canvas.style.width = `${w}px`;
      canvas.style.height = `${h}px`;
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    };

    const draw = () => {
      try {
        const w = window.innerWidth;
        const h = window.innerHeight;
        const time = t * 0.00015;

        // Black base
        ctx.fillStyle = '#050508';
        ctx.fillRect(0, 0, w, h);

        // Dark gray base gradient for dynamic range (deep blacks to mid darks)
        const baseGrad = ctx.createLinearGradient(0, 0, w, h);
        baseGrad.addColorStop(0, '#0a0a0c');
        baseGrad.addColorStop(0.3, '#0f0f12');
        baseGrad.addColorStop(0.6, '#0c0c0e');
        baseGrad.addColorStop(1, '#08080a');
        ctx.fillStyle = baseGrad;
        ctx.fillRect(0, 0, w, h);

        // Dark gray ripples (concentric rings from multiple origins)
        const rippleOrigins = [
          [w * 0.25, h * 0.4],
          [w * 0.75, h * 0.55],
          [w * 0.5, h * 0.25],
        ];
        const grayRipples = ['#0d0d0f', '#141418', '#1a1a1f', '#222228', '#2a2a30'];
        rippleOrigins.forEach(([ox, oy], oi) => {
          const phase = (oi / 3) * Math.PI * 2 + time * 0.3;
          for (let ring = 0; ring < 12; ring++) {
            const r = 40 + ring * 55 + 25 * Math.sin(time * 0.4 + ring * 0.5 + phase);
            const gray = grayRipples[ring % grayRipples.length];
            const alpha = 0.08 * (1 - ring / 14) * (0.6 + 0.4 * Math.sin(time * 0.5 + ring * 0.3));
            ctx.beginPath();
            ctx.arc(ox, oy, r, 0, Math.PI * 2);
            ctx.strokeStyle = gray;
            ctx.globalAlpha = alpha;
            ctx.lineWidth = 1.5;
            ctx.stroke();
          }
        });
        ctx.globalAlpha = 1;

        // Dark gray waves (horizontal bands with sine displacement)
        const waveCount = 6;
        for (let i = 0; i < waveCount; i++) {
          const phase = (i / waveCount) * Math.PI * 2 + time * 0.25;
          const yBase = h * (0.2 + (i / waveCount) * 0.7) + 60 * Math.sin(time + phase);
          const bandHeight = 80 + 30 * Math.sin(time * 0.6 + i);
          const waveGrad = ctx.createLinearGradient(0, yBase - bandHeight / 2, 0, yBase + bandHeight / 2);
          const dark = `rgba(15,15,18,${0.12 + 0.06 * Math.sin(phase)})`;
          const mid = `rgba(28,28,32,${0.18 + 0.08 * Math.sin(phase + 0.5)})`;
          waveGrad.addColorStop(0, 'transparent');
          waveGrad.addColorStop(0.3, dark);
          waveGrad.addColorStop(0.5, mid);
          waveGrad.addColorStop(0.7, dark);
          waveGrad.addColorStop(1, 'transparent');
          ctx.fillStyle = waveGrad;
          ctx.fillRect(0, yBase - bandHeight / 2, w, bandHeight);
        }

        // Flowing blobs with wider gray range (dark to mid to light grays)
        const blobCount = 5;
        for (let i = 0; i < blobCount; i++) {
          const phase = (i / blobCount) * Math.PI * 2;
          const x = w * (0.3 + 0.4 * Math.sin(time + phase) + 0.1 * Math.sin(time * 0.7 + phase * 2));
          const y = h * (0.3 + 0.4 * Math.cos(time * 0.8 + phase * 1.3) + 0.1 * Math.cos(time * 0.5 + phase));
          const r = 120 + 80 * Math.sin(time * 0.6 + phase) + 60 * Math.cos(time * 0.4);

          const gradient = ctx.createRadialGradient(x, y, 0, x, y, r);
          const hue = (240 + i * 60 + t * 0.02) % 360;
          const sat = 8;
          const light = 12 + 6 * Math.sin(time + phase);
          gradient.addColorStop(0, `hsla(${hue}, ${sat}%, ${light}%, 0.4)`);
          gradient.addColorStop(0.35, `hsla(${hue}, ${sat}%, 8%, 0.2)`);
          gradient.addColorStop(0.6, `hsla(0, 0%, 5%, 0.08)`);
          gradient.addColorStop(1, 'transparent');

          ctx.beginPath();
          ctx.arc(x, y, r, 0, Math.PI * 2);
          ctx.fillStyle = gradient;
          ctx.fill();
        }

        // Mid to light gray blobs for dynamic range
        for (let i = 0; i < 3; i++) {
          const phase = (i / 3) * Math.PI * 2 + 1;
          const x = w * (0.5 + 0.45 * Math.cos(time * 0.5 + phase));
          const y = h * (0.5 + 0.45 * Math.sin(time * 0.6 + phase * 1.2));
          const r = 100 + 50 * Math.sin(time * 0.4 + phase);

          const g = ctx.createRadialGradient(x, y, 0, x, y, r);
          g.addColorStop(0, 'rgba(120,120,130,0.12)');
          g.addColorStop(0.4, 'rgba(60,60,70,0.06)');
          g.addColorStop(0.7, 'rgba(30,30,35,0.03)');
          g.addColorStop(1, 'transparent');
          ctx.beginPath();
          ctx.arc(x, y, r, 0, Math.PI * 2);
          ctx.fillStyle = g;
          ctx.fill();
        }

        // Burnt chrome: sharp light streaks (reflections)
        const streakCount = 4;
        for (let i = 0; i < streakCount; i++) {
          const phase = (i / streakCount) * Math.PI * 2 + time * 0.2;
          const cx = w * (0.3 + 0.4 * Math.sin(phase) + 0.2 * Math.sin(phase * 2));
          const cy = h * (0.35 + 0.35 * Math.cos(phase * 0.9));
          const angle = phase * 0.5;
          const rx = 180 + 60 * Math.sin(time * 0.3 + i);
          const ry = 12 + 4 * Math.sin(time * 0.4 + i);

          ctx.save();
          ctx.translate(cx, cy);
          ctx.rotate(angle);
          const streakGrad = ctx.createLinearGradient(-rx, 0, rx, 0);
          streakGrad.addColorStop(0, 'transparent');
          streakGrad.addColorStop(0.35, 'rgba(40,35,30,0.4)');
          streakGrad.addColorStop(0.5, 'rgba(220,200,180,0.25)');
          streakGrad.addColorStop(0.65, 'rgba(255,248,240,0.35)');
          streakGrad.addColorStop(0.8, 'rgba(200,180,160,0.2)');
          streakGrad.addColorStop(1, 'transparent');
          ctx.fillStyle = streakGrad;
          ctx.beginPath();
          ctx.ellipse(0, 0, rx, ry, 0, 0, Math.PI * 2);
          ctx.fill();
          ctx.restore();
        }

        // Burnt chrome: small hot spots (specular highlights)
        const spotCount = 5;
        for (let i = 0; i < spotCount; i++) {
          const phase = (i / spotCount) * Math.PI * 2 + time * 0.15;
          const sx = w * (0.2 + 0.6 * Math.sin(phase * 1.1));
          const sy = h * (0.25 + 0.55 * Math.cos(phase * 0.85));
          const spotR = 25 + 15 * Math.sin(time * 0.5 + i);
          const spotGrad = ctx.createRadialGradient(sx, sy, 0, sx, sy, spotR);
          spotGrad.addColorStop(0, 'rgba(255,245,235,0.5)');
          spotGrad.addColorStop(0.2, 'rgba(255,230,210,0.2)');
          spotGrad.addColorStop(0.5, 'rgba(180,160,140,0.06)');
          spotGrad.addColorStop(1, 'transparent');
          ctx.fillStyle = spotGrad;
          ctx.beginPath();
          ctx.arc(sx, sy, spotR, 0, Math.PI * 2);
          ctx.fill();
        }

        // Soft white/gray highlights (existing layer, slightly adjusted)
        for (let i = 0; i < 2; i++) {
          const phase = (i / 2) * Math.PI * 2 + 0.7;
          const x = w * (0.5 + 0.4 * Math.cos(time * 0.5 + phase));
          const y = h * (0.5 + 0.4 * Math.sin(time * 0.6 + phase * 1.2));
          const r = 80 + 40 * Math.sin(time * 0.4 + phase);
          const g = ctx.createRadialGradient(x, y, 0, x, y, r);
          g.addColorStop(0, 'rgba(255,255,255,0.07)');
          g.addColorStop(0.5, 'rgba(200,200,210,0.03)');
          g.addColorStop(1, 'transparent');
          ctx.beginPath();
          ctx.arc(x, y, r, 0, Math.PI * 2);
          ctx.fillStyle = g;
          ctx.fill();
        }

        t += 16;
      } catch {
        setFailed(true);
        return;
      }
      animationId = requestAnimationFrame(draw);
    };

    resize();
    window.addEventListener('resize', resize);
    draw();

    return () => {
      window.removeEventListener('resize', resize);
      cancelAnimationFrame(animationId);
    };
  }, [failed]);

  if (failed) {
    return <div className="absolute inset-0 bg-black" aria-hidden />;
  }

  return (
    <canvas
      ref={canvasRef}
      className="absolute inset-0 w-full h-full block"
      style={{ background: '#0a0a0a' }}
      aria-hidden
    />
  );
}
