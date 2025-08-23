# iOS Notes ⇄ Repo (HTML) Shortcuts

Goal: preserve iOS Notes rich formatting by round-tripping HTML exports in the `ios` worktree.

## Prerequisites
- iOS Shortcuts app
- Files app access to the `Prompts-ios` folder (via iCloud Drive/Working Copy)
- Optional: Working Copy app for Git push/pull on iOS

## Shortcut 1 — Notes → HTML (Export)
1. Find Notes → Filter by Title contains `Codex Notes` (or select manually)
2. Get Note → Get Rich Text
3. Make HTML from Rich Text
4. Save File → `Prompts-ios/sync/ios/Codex_Notes.html` (Ask Where: Off, Overwrite: On)
5. Optional: Commit via Working Copy (automation) with message `chore(ios): export Codex Notes HTML`

## Shortcut 2 — HTML → Notes (Import)
1. Get File → `Prompts-ios/sync/ios/Codex_Notes.html`
2. Get Contents of File
3. Make Rich Text from HTML
4. Create Note → In desired folder, Title `Codex Notes`

## Tips
- Keep `IOS.md` updated with the shared iCloud Notes link
- Treat Notes as the authoring source; commit HTML exports for history
- On desktop: edit HTML in `Prompts-ios` worktree if necessary, then use Import to restore formatting to Notes

## Troubleshooting
- If HTML loses some styles, ensure “Make HTML from Rich Text” is used (not Markdown)
- Large images: enable “Save to Files” with Overwrite Off to avoid failures
- Working Copy: grant Shortcuts access under Automations
