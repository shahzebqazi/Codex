import { useCallback, useMemo, lazy, Suspense } from 'react';

const ForceGraph3D = lazy(() =>
  import('react-force-graph-3d').then((mod) => ({ default: mod.default }))
);

export type ThreadNode = { id: string; name?: string; role?: 'user' | 'assistant' | 'system' };
export type ThreadLink = { source: string; target: string };

type ThreadGraph3DProps = {
  nodes: ThreadNode[];
  links: ThreadLink[];
  width?: number;
  height?: number;
  backgroundColor?: string;
};

function GraphPlaceholder() {
  return (
    <div className="w-full h-full flex items-center justify-center bg-[#0d1117] border border-[#30363d] rounded text-[#8b949e] font-mono text-sm">
      Loading 3D graph…
    </div>
  );
}

export function ThreadGraph3D({
  nodes,
  links,
  width = 400,
  height = 360,
  backgroundColor = '#0d1117',
}: ThreadGraph3DProps) {
  const graphData = useMemo(
    () => ({
      nodes: nodes.map((n) => ({ id: n.id, name: n.name ?? n.id, role: n.role })),
      links: links.map((l) => ({ source: l.source, target: l.target })),
    }),
    [nodes, links]
  );

  const nodeColor = useCallback((node: { id: string; name?: string; role?: string }) => {
    if (node.role === 'user') return '#0EA5E9';
    if (node.role === 'assistant') return '#10B981';
    return '#8b949e';
  }, []);

  if (!nodes.length) {
    return (
      <div
        className="w-full flex items-center justify-center rounded border border-[#30363d] font-mono text-sm text-[#8b949e]"
        style={{ width, height, backgroundColor }}
      >
        No messages in this thread
      </div>
    );
  }

  return (
    <Suspense fallback={<GraphPlaceholder />}>
      <div className="w-full rounded border border-[#30363d] overflow-hidden bg-[#0d1117]" style={{ width, height }}>
        <ForceGraph3D
          graphData={graphData}
          nodeLabel={(node: { id: string; name?: string }) => node.name ?? node.id}
          nodeColor={nodeColor as (node: unknown) => string}
          linkColor="#333333"
          backgroundColor={backgroundColor}
          width={width}
          height={height}
          enableNodeDrag={true}
          showNavInfo={false}
        />
      </div>
    </Suspense>
  );
}
