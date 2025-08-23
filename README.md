# Prompts - AI Development Workspace

**Developer**: sqazi  
**Email**: dev@shahzeb.me  
**Created**: August 18th, 2025  
**Last Updated**: December 19th, 2025  
**Version**: 3.0

## Overview

This is a comprehensive AI development workspace for managing prompts, memories, and development workflows. The workspace is designed to work seamlessly with Cursor IDE and provides a structured approach to AI-assisted development with automated memory management and customizable AI behavior.

## How we log

- Logs live under `/var/log/[project_name]` as plain `.log` files. Ordering: `worklog`/`buildlog`/`changelog`/`errors` are append-only (newest-last); `server.log` is newest-first per deployment.
- Work tracking uses `worklog` entries with priorities P1/P2/P3 and status tags `#planned`, `#in-development`, `#completed`.
- Change tracking uses `buildlog` entries with conventional commits (`feat|fix|refactor|docs|test|perf`), optionally including `files:[path1, path2]`.
- Release summaries use `changelog` one-liners. README release notes can be generated from logs when needed. No YAML change logs or in-repo `Buildlog.yaml`/`Changelog.yaml`.
- Quick view: `tail -n 50 /var/log/[project]/worklog/worklog.log` · `tail -n 50 /var/log/[project]/buildlog/build.log` · `tail -n 50 /var/log/[project]/changelog/change.log` · `head -n 200 /var/log/[project]/server.log`.

## Core Components

### Mods.md - AI Behavior Customization
**Mods** are configurable rules that customize how AI assistants behave across different contexts. They are:
- **Static by default** - Changes require manual editing
- **Context-agnostic** - Applicable to fields, tasks, and events
- **Commentable** - Can be enabled/disabled by commenting/uncommenting lines
- **General purpose** - Not user-specific, but user-configurable
- **Version controlled** - Shared across all clones of the repository

### Logging System (Prompt-based)
**Changelogging** is handled via plain-text logs under `/var/log/[project_name]`:
- **worklog**: Prioritized tasks with statuses (#planned, #in-development, #completed)
- **buildlog**: Conventional commit entries per change; optional files list
- **changelog**: One-line release-style summaries
- **errors** and **server.log**: Error entries and deployment transcripts

**Mods Usage Instructions:**
- **Enable a mod**: Uncomment the line (remove #)
- **Disable a mod**: Comment the line (add #)
- **Customize values**: Modify the text after the colon
- **Add new mods**: Follow the established pattern and document in README

**Mods Categories:**
1. **General Mods** - Communication, workflow, and environment preferences
2. **Coding Mods** - Code style, development practices, and technology stack choices

**Rules for Adding Mods:**
1. **One concept per mod** - Each mod should address a single, clear behavior
2. **Universal applicability** - Mods should work across different AI contexts
3. **Clear syntax** - Use consistent comment/uncomment patterns
4. **Documentation** - Each mod should be self-explanatory
5. **Testing** - New mods should be tested before committing
6. **Categorization** - Place new mods in appropriate category (General or Coding)

### Memories.md - AI Learning & Persistence
**Memories** are AI-generated insights that provide:
- **Persistence** - Long-term retention of learned patterns
- **Iterative Deepening** - Progressive refinement of understanding
- **Learning** - Adaptation to user's work patterns and preferences
- **Context Recovery** - Quick resumption of work after breaks

**Memory Management Rules:**
- **Automated Generation** - AI creates and removes memories automatically
- **Fresh Start** - Git clone begins with empty memories
- **Regular Updates** - Must be appended every few days
- **Decay Handling** - If memories are stale, AI provides summaries to resume work
- **User-Specific** - Contains information that must be generated for each user

## Key Differences: Mods vs Memories vs Changelogging

### Mods (Static Configuration)
- **Purpose**: Define how AI should behave and respond
- **Scope**: Universal rules that apply to all AI interactions
- **Persistence**: Shared across repository clones, version controlled
- **Customization**: Manual editing required, comment/uncomment pattern
- **Examples**: "Always provide concise responses", "Use functional programming style"
- **Lifecycle**: Changes only when manually edited

### Memories (Dynamic Learning)
- **Purpose**: Store what AI has learned about the user
- **Scope**: User-specific insights and preferences
- **Persistence**: Unique to each user, not version controlled
- **Customization**: AI automatically generates and manages content
- **Examples**: "User prefers Haskell for backend", "User works in focused 2-hour sessions"
- **Lifecycle**: Continuously updated through AI interactions

### Changelogging (Change Tracking)
- **Purpose**: Document changes via prompt-based logs (worklog/buildlog/changelog)
- **Scope**: Project-wide change history and audit trail
- **Persistence**: Version controlled where published; operational logs live under `/var/log/[project_name]`
- **Examples**: Code generation, bug fixes, structural changes, documentation updates
- **Lifecycle**: Updated alongside development and releases

### README.md Integration (Public Changelog)
- **Purpose**: Public-facing notes when you cut a release
- **Source**: Generate from `buildlog` and `changelog` on demand; no YAML templates

### When to Use Each

**Use Mods when you want to:**
- Set consistent AI behavior across all contexts
- Share preferences with other developers
- Control how AI responds to different types of requests
- Establish coding standards and practices

**Use Memories when you want to:**
- Let AI remember your personal preferences
- Enable context recovery after breaks
- Allow AI to adapt to your work patterns
- Maintain user-specific knowledge across sessions

**Use Changelogging when you want to:**
- Track all changes made through AI assistance
- Maintain an audit trail of project modifications
- Share change history with other developers
- Document the evolution of your codebase
- Teach other AI systems how to maintain changelogs

## Project Structure

```
Prompts/
├── README.md          # This file - project overview and rules
├── Memories.md        # AI-generated learning and persistence (auto-managed)
├── Mods.md           # Commentable AI behavior customizations
├── Skills/            # Skill development and learning
│   └── Code/         # Code snippets and examples
├── Docs/             # Documentation and whitepapers
└── Tasks/            # Task management and tracking
```

## Usage Guidelines

### For AI Assistants
1. **Read Mods.md first** - Understand configured behaviors
2. **Check Memories.md** - Review recent learning and context
3. **Provide summaries** - If memories are stale (>3 days old)
4. **Update memories** - Append new insights after significant interactions
5. **Update Changelog** - Add entries for all code generation and modifications

### Changelogging Workflow
1. **Before Making Changes**: Review recent `buildlog` and `changelog` entries
2. **During Code Generation**: Append conventional-commit entries to `buildlog`
3. **After Changes**: Add a one-line summary to `changelog` if it affects releases

### For Users
1. **Customize Mods.md** - Comment/uncomment behaviors as needed
2. **Review memories** - Check for outdated or incorrect information
3. **Maintain freshness** - Ensure memories are updated regularly
4. **Clone fresh** - Start new workspaces with clean memory state

## Version Control

- **Mods.md**: Version controlled, shared across clones
- **Memories.md**: Not version controlled, regenerated per workspace
- **Other files**: Standard version control practices apply

---

## Release Notes

Public release notes can be generated from `buildlog` and `changelog` when you cut a release.

## Future Features Roadmap

### High Priority Features

#### 1. **Automated Validation & Linting**
- **Required Fields**: Every entry must have version, date, and at least one category
- **Format Validation**: YAML syntax must be valid
- **Category Consistency**: Use only predefined categories
- **Entry Completeness**: Each change should have sufficient detail

#### 2. **Git Hook Integration**
- **Pre-commit Hook**: Automatically update README.md before commits
- **Auto-append**: Generate notes from `buildlog`/`changelog` when releasing
- **Timestamp Updates**: Automatic timestamp management
- **Format Validation**: Ensure consistency before commit

#### 3. **Change Impact Assessment**
- **Breaking Changes**: Major changes that break existing functionality
- **Feature Additions**: New capabilities added
- **Enhancements**: Improvements to existing features
- **Bug Fixes**: Corrections to issues
- **Documentation**: Updates to docs only

#### 4. **Cross-Reference System**
- **Issue Numbers**: #123 for GitHub/GitLab issues
- **Pull Requests**: PR #45 for merge requests
- **Related Changes**: Links to other changelog entries
- **Dependencies**: What other systems are affected

### Medium Priority Features

#### 5. **Rolling Release Management**
- **Semantic Versioning**: 1.2.3 for major.minor.patch
- **Release Candidates**: 2.0.0-rc.1
- **Beta Versions**: 1.5.0-beta.2
- **Hotfixes**: 1.2.4 for emergency fixes

#### 6. **AI Learning Feedback Loop**
- **AI Compliance**: Track how often AI follows the template correctly
- **Common Mistakes**: Identify what AI gets wrong most often
- **Template Improvements**: Suggestions for better AI instructions
- **Learning Progress**: Monitor AI improvement over time

#### 7. **Multi-Project Template System**
- **Frontend Projects**: React/Vue/Angular specific categories
- **Backend Projects**: API, database, security categories
- **Mobile Apps**: Platform-specific change types
- **DevOps Projects**: Infrastructure, deployment categories

#### 8. **Change Analytics & Reporting**
- **Change Frequency**: How often changes occur
- **Category Distribution**: What types of changes are most common
- **Team Activity**: Who/what is making changes
- **Quality Metrics**: Bug fix vs. feature ratio

### Nice to Have Features

#### 9. **Integration with Project Management Tools**
- **Jira Integration**: Auto-link to JIRA tickets
- **Slack Notifications**: Post changes to team channels
- **Email Digests**: Weekly change summaries
- **Dashboard Integration**: Real-time change visibility

#### 10. **Enhanced AI Instructions**
- **Context Awareness**: Different instructions for different file types
- **Learning Examples**: Show good vs. bad entries
- **Common Patterns**: Templates for typical changes
- **Error Prevention**: What NOT to do

#### 11. **Backup & Recovery System**
- **Auto-backup**: Before making changes
- **Version History**: Keep previous versions
- **Recovery Points**: Easy rollback if needed
- **Conflict Resolution**: Handle merge conflicts

#### 12. **Performance Optimization**
- **Pagination**: For very long changelogs
- **Search & Filter**: Find specific changes quickly
- **Compression**: Archive old entries
- **Caching**: Fast access to recent changes

#### 13. **Community & Sharing Features**
- **Template Marketplace**: Share templates with others
- **Best Practices**: Community-driven improvements
- **Integration Examples**: How others use the system
- **Feedback System**: Continuous improvement

#### 14. **Accessibility & Internationalization**
- **Multi-language Support**: Different language templates
- **Accessibility**: Screen reader friendly
- **Localization**: Date formats, terminology
- **Cultural Adaptation**: Different change categorization styles

---

*Future features will be implemented based on priority and community feedback*
