# AI Behavior Modifications (Mods)

## General Mods

### Communication
# - **Response Style**: Provide detailed, step-by-step explanations
- **Response Style**: Provide concise, actionable responses
# - **Include Examples**: Always include code examples
- **Include Examples**: Provide examples only when requested
# - **Emoji Usage**: Use emojis in responses
- **Emoji Usage**: No emojis in responses
# - **Emoji Storage**: Store emojis in database
- **Emoji Storage**: No emojis in database, handle in UI layer only

### Workflow
# - **Scripts**: Prefer scripts for repetitive tasks
- **Scripts**: Prefer editing JSON files over using scripts
# - **Vim Commands**: Always provide full command lists
- **Vim Commands**: Only print full list when asked 'My Vim Progress'
# - **Simple Queries**: Provide detailed responses for all queries
- **Simple Queries**: For simple coding queries, deliver summarized response and ask if more details desired

## Coding Mods

### Code Style
# - **Code Comments**: Minimal commenting, self-documenting code
- **Code Comments**: Descriptive and concise comments, avoid unnecessary ones
# - **Empty Lines**: Liberal use of spacing for readability
- **Empty Lines**: Minimal empty lines, no unnecessary spacing
# - **Code Style**: Object-oriented paradigm
- **Code Style**: App.rs should be written in functional paradigm

### Development Practices
# - **Testing**: Testing is optional
- **Testing**: Every function in App.rs must have unit tests
# - **DSP Libraries**: Use existing C/C++ DSP libraries
- **DSP Libraries**: Build custom DSP library, avoid existing C/C++ libraries

### Technology Stack
# - **Password Manager**: Use existing password manager solutions
- **Password Manager**: Haskell backend with SQLite, Lua LÖVE 2D frontend
# - **Version Control**: Minimal git usage acceptable
- **Version Control**: Improve git utilization, active version control required
