import { PsychedelicLiquidBackground } from './components/PsychedelicLiquidBackground';

/** Full-screen background only — use ?background=1 or #background to inspect. */
export function BackgroundOnlyPage() {
  return (
    <div className="fixed inset-0 overflow-hidden bg-black">
      <PsychedelicLiquidBackground />
    </div>
  );
}
