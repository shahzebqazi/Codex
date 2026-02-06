#!/usr/bin/env python3
"""
Pure-function router simulation for Signal <-> Codex thread bridging.

Run to see how group-chat delegation routes to agents and back.
"""

from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable, List, Mapping, Sequence, Tuple


@dataclass(frozen=True)
class RouterState:
    thread_by_group: Mapping[str, str]
    agents: Sequence[str]
    default_agent: str


@dataclass(frozen=True)
class SignalMessage:
    group_id: str
    sender: str
    text: str
    timestamp: int
    message_id: str


@dataclass(frozen=True)
class CodexEvent:
    thread_id: str
    agent: str
    text: str
    timestamp: int
    message_id: str


@dataclass(frozen=True)
class Action:
    kind: str  # "to_codex" | "to_signal" | "log"
    target: str
    payload: Mapping[str, str]


def select_agent(agents: Sequence[str], default_agent: str, text: str) -> str:
    tokens = [token.strip() for token in text.split() if token.strip()]
    for token in tokens:
        if token.startswith("@"): 
            candidate = token[1:]
            if candidate in agents:
                return candidate
    return default_agent


def strip_agent_mentions(text: str, agents: Sequence[str]) -> str:
    tokens = text.split()
    kept: List[str] = []
    for token in tokens:
        if token.startswith("@") and token[1:] in agents:
            continue
        kept.append(token)
    return " ".join(kept).strip()


def group_for_thread(mapping: Mapping[str, str], thread_id: str) -> str | None:
    for group_id, mapped_thread in mapping.items():
        if mapped_thread == thread_id:
            return group_id
    return None


def route_inbound(state: RouterState, message: SignalMessage) -> Tuple[RouterState, List[Action]]:
    thread_id = state.thread_by_group.get(message.group_id)
    if not thread_id:
        return state, [
            Action(
                kind="log",
                target="router",
                payload={
                    "level": "warn",
                    "message": f"Unmapped group {message.group_id}",
                },
            )
        ]

    agent = select_agent(state.agents, state.default_agent, message.text)
    clean_text = strip_agent_mentions(message.text, state.agents)

    return state, [
        Action(
            kind="to_codex",
            target=thread_id,
            payload={
                "sender": message.sender,
                "agent": agent,
                "text": clean_text,
                "message_id": message.message_id,
            },
        )
    ]


def route_outbound(state: RouterState, event: CodexEvent) -> Tuple[RouterState, List[Action]]:
    group_id = group_for_thread(state.thread_by_group, event.thread_id)
    if not group_id:
        return state, [
            Action(
                kind="log",
                target="router",
                payload={
                    "level": "warn",
                    "message": f"Unmapped thread {event.thread_id}",
                },
            )
        ]

    decorated_text = f"[{event.agent}] {event.text}"
    return state, [
        Action(
            kind="to_signal",
            target=group_id,
            payload={
                "text": decorated_text,
                "message_id": event.message_id,
            },
        )
    ]


def run_demo() -> None:
    state = RouterState(
        thread_by_group={"group-1": "thread-abc"},
        agents=("planner", "builder", "qa"),
        default_agent="planner",
    )

    inbound = SignalMessage(
        group_id="group-1",
        sender="alice",
        text="@builder can you implement the router?",
        timestamp=1700000000,
        message_id="sig-001",
    )

    state, actions = route_inbound(state, inbound)
    print("Inbound actions:")
    for action in actions:
        print(action)

    outbound = CodexEvent(
        thread_id="thread-abc",
        agent="builder",
        text="Router implemented. Want tests next?",
        timestamp=1700000005,
        message_id="codex-001",
    )

    state, actions = route_outbound(state, outbound)
    print("\nOutbound actions:")
    for action in actions:
        print(action)


if __name__ == "__main__":
    run_demo()
