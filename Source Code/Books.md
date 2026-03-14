# Books

## Style Guide
- Author folders: `Authors/Last, First`
- Book folders: `Authors/Last, First/<Book Title>`
- Notes files: `_Author.md`, `_Book.md`
- Use Obsidian links: [[README]], [[prompt]], [[Books]]
- Prefer plain Markdown; put media in `media/` (ignored)

### _Author.md template
---
name: Last, First
display: First Last
aka: []
links: []
summary: |
  Short evolving summary of author’s themes and significance.
bibliography: []
---

## Librarian Tasks
- Create author folder if missing; maintain `_Author.md` summary
- For each new book, create `<Book Title>/_Book.md` with metadata
- Keep Categories minimal; create only when used
- Ensure `.jjignore` excludes media; commit only Markdown and configs

### _Book.md template
---
title: <Book Title>
author: Last, First
publisher: <Publisher>
isbn: <ISBN>
edition: <Revision/Edition>
year: <Year>
source: <epub|pdf|torrent|webpage>
source_link: <URL or local media path>
notes_link: [[<relative link to notes>]]
status: not-started|reading|finished
---

## Summary
Key insights, arguments, and takeaways.

## Historical Context
Relevant context and timelines.

## Quotes
- "Quote" — page

## References
- citation keys, DOIs, etc.
