import * as React from "react";

export const MOBILE_BREAKPOINT = 768;

/** Breakpoint below which we show the responsive (non-scaled-mockup) layout. */
export const NARROW_VIEWPORT_BREAKPOINT = 1024;

export function useIsMobile() {
  const [isMobile, setIsMobile] = React.useState<boolean | undefined>(
    undefined,
  );

  React.useEffect(() => {
    const mql = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT - 1}px)`);
    const onChange = () => {
      setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);
    };
    mql.addEventListener("change", onChange);
    setIsMobile(window.innerWidth < MOBILE_BREAKPOINT);
    return () => mql.removeEventListener("change", onChange);
  }, []);

  return !!isMobile;
}

/** True when viewport is narrow (mobile/tablet): use full-width responsive layout instead of scaled desktop mockup. */
export function useIsNarrowViewport() {
  const [isNarrow, setIsNarrow] = React.useState<boolean>(() =>
    typeof window !== "undefined"
      ? window.innerWidth < NARROW_VIEWPORT_BREAKPOINT
      : false,
  );

  React.useEffect(() => {
    const mql = window.matchMedia(
      `(max-width: ${NARROW_VIEWPORT_BREAKPOINT - 1}px)`,
    );
    const onChange = () => setIsNarrow(window.innerWidth < NARROW_VIEWPORT_BREAKPOINT);
    mql.addEventListener("change", onChange);
    setIsNarrow(window.innerWidth < NARROW_VIEWPORT_BREAKPOINT);
    return () => mql.removeEventListener("change", onChange);
  }, []);

  return isNarrow;
}
