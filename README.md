# Codex - AI Development Workspace

**Developer**: shahzebqazi  
**Email**: dev@shahzeb.me  
**Created**: August 18th, 2025  
**Last Updated**: August 23, 2025  

## What This Is

A simple workspace for AI-assisted development. It's just you, some AI tools, and a way to keep track of what you're working on.

## Current AI Configuration

This project includes a `.cursorrules` file that configures the AI to operate as a **Technical Writer Background Agent**. When you open this project in Cursor IDE, the AI will automatically:

- **Log all development activities** in the background
- **Track code changes, commits, and progress** automatically
- **Maintain development history** using structured logging
- **Provide logging commands** like `#log-status`, `#log-work`, `#log-change`

The AI operates silently in the background, only interrupting when clarification is needed. You can interact with the logging system using the provided commands or let it work automatically.

## Quick Start

1. **Map.md** - Style guide, patterns, and AI interaction rules
2. **Memories.md** - AI remembers your preferences and current context
3. **Mods/** - System mods for functionality (chat sounds, profiles, etc.)
4. **AI-ML-Topics-Guide.md** - Reference for AI/ML concepts and mods system

## Project Structure

```
Codex/
├── README.md                    # This file
├── .cursorrules                 # AI behavior configuration for Cursor IDE
├── Map/                         # Style guide, patterns, and AI rules
│   └── Map.md                   # Main style guide and patterns
├── Memories/                    # AI learning and current context
├── Mods/                        # System mods and functionality
│   ├── README.md               # Mods documentation
│   ├── chat-sound.md           # Chat sound controls
│   ├── cursor-profiles.md      # AI profile switching
│   ├── auto-execution.md       # Code execution controls
│   ├── agent-training.md       # AI learning configuration
│   ├── workflow-preferences.md # User workflow preferences
│   ├── ai-personality.md       # AI personality configuration
│   └── permissions.md          # Access control and permissions
├── AI-ML-Topics-Guide.md       # AI/ML reference and mods system
└── Tools/                       # Skill and task templates
```

## How to Use

### For You
- Check **Map/** folder for style guides and AI interaction rules
- Review **Memories/** folder to see what AI has learned about you
- Use **Mods/** folder to understand system functionality
- Reference **AI-ML-Topics-Guide.md** for AI/ML concepts and mods system

### For AI
- Read **Map/** folder first to understand style guidelines and interaction patterns
- Check **Memories/** folder for learned preferences and current context
- Review **Mods/** folder for system configuration options
- Update memories after working together

## iOS Notes Sync

- `ios` branch is checked out at `../Prompts-ios/` using git worktree
- Use iOS Shortcuts to export notes as HTML
- Commit changes from the worktree folder

---

## Cursor Rules (.cursorrules)

### Overview
`.cursorrules` files are **project-specific AI configuration files** that automatically execute when you open a project/workspace in Cursor. They transform the AI into a specialized assistant tailored to your project's needs.

### How It Works

#### Automatic Execution
- **On Workspace Open**: Cursor reads `.cursorrules` when you open a project
- **AI Configuration**: The AI immediately adopts the behavior defined in the file
- **Persistent Behavior**: The AI maintains this configuration for the entire session
- **Project-Specific**: Each project can have different `.cursorrules` for different AI behaviors

#### Behavior Transformation
When you open a project with `.cursorrules`:
- **Before**: AI behaves as default Cursor assistant
- **After**: AI immediately transforms into the specified role/personality
- **Context**: AI understands the project context and behaves accordingly
- **Memory**: AI maintains this behavior throughout the session

### Example Configurations

#### Web Development Project
```markdown
# .cursorrules for Web Project
- You are a frontend expert
- Focus on React and modern web practices
- Always suggest testing approaches
- Use TypeScript best practices
```

#### Data Science Project
```markdown
# .cursorrules for Data Project
- You are a data science specialist
- Focus on Python, pandas, scikit-learn
- Always suggest data validation
- Use Jupyter notebook best practices
```

#### Documentation Project
```markdown
# .cursorrules for Docs Project
- You are a documentation specialist
- Focus on clear, structured writing
- Always follow style guidelines
- Use consistent formatting patterns
```

### Key Benefits

#### Project-Specific AI
- **Web Projects**: Get frontend/backend expertise
- **Data Projects**: Get data science guidance
- **Documentation**: Get writing and organization help
- **Research**: Get academic and research methodology support

#### Consistent Behavior
- **Same AI Personality**: Every time you open the project
- **Project Context**: AI understands your specific project needs
- **Specialized Knowledge**: AI focuses on relevant expertise areas
- **Custom Commands**: Project-specific quick commands and shortcuts

### Advanced Usage

#### Dynamic Switching
You can change `.cursorrules` while Cursor is open:
1. **Edit .cursorrules** → Modify the file
2. **Restart Cursor** → Close and reopen the project
3. **New Behavior** → AI adopts the new configuration

#### Integration with Codex
Your Codex system provides ready-to-use `.cursorrules` configurations:
- **Default Profile**: General development assistance
- **Expert Profile**: Deep technical expertise
- **Mentor Profile**: Educational guidance
- **Rapid Profile**: Quick implementation focus

### Best Practices

#### Creating Effective Rules
1. **Be Specific**: Define clear roles and expertise areas
2. **Project Context**: Include project-specific requirements
3. **Behavior Guidelines**: Specify how AI should interact
4. **Custom Commands**: Define project-specific shortcuts
5. **Integration**: Reference your existing style guides and patterns

#### File Management
- **Version Control**: Include `.cursorrules` in your git repository
- **Project Isolation**: Each project maintains its own configuration
- **Consistency**: Use similar patterns across related projects
- **Documentation**: Document what each configuration does

### Technical Details

#### File Location
- **Project Root**: Place `.cursorrules` in your project's root directory
- **File Format**: Use Markdown syntax for easy editing
- **Naming**: Must be exactly `.cursorrules` (with the dot)

#### Execution Timing
- **Workspace Open**: Rules are read and applied immediately
- **Session Persistence**: Rules remain active until project is closed
- **No Reload**: Changes require restarting Cursor to take effect
- **Memory**: AI maintains context and behavior throughout session

---

## Topics Guide

### Overview

This guide explores three fundamental concepts in artificial intelligence and machine learning: Reinforcement Learning, Iterative Deepening, and AI Persistence. Each topic is presented with theoretical foundations, practical applications, and implementation considerations.

### 1. Reinforcement Learning (RL)

#### Core Concept
Reinforcement Learning is a type of machine learning where an agent learns to make decisions by interacting with an environment. The agent receives rewards or penalties for its actions and learns to maximize cumulative rewards over time.

#### Key Components
- **Agent**: The decision-making entity that learns from interactions
- **Environment**: The system the agent interacts with
- **State (s)**: Current situation of the environment
- **Action (a)**: Choice made by the agent
- **Reward (r)**: Feedback signal from the environment
- **Policy (π)**: Strategy mapping states to actions

#### Learning Process
1. **Observation**: Agent observes current state s_t
2. **Action Selection**: Agent chooses action a_t based on policy
3. **Environment Response**: Environment transitions to new state s_{t+1} and provides reward r_{t+1}
4. **Policy Update**: Agent updates policy to maximize expected future rewards

#### Applications in Gaming
- NPC behavior learning
- Strategy optimization
- Dynamic difficulty adjustment
- Procedural content generation

### 2. Iterative Deepening

#### Core Concept
Iterative Deepening is a search strategy that combines the benefits of breadth-first search (completeness) with the space efficiency of depth-first search. It works by performing a series of depth-limited searches with increasing depth limits.

#### Key Characteristics
- **Complete**: Will find a solution if one exists
- **Optimal**: Finds the shortest path solution
- **Space Efficient**: O(bd) space complexity where b is branching factor and d is depth
- **Time Complexity**: O(b^d) but with multiple searches

#### Applications
- Pathfinding in game worlds
- AI decision tree traversal
- Puzzle solving mechanics
- State space exploration

### 3. AI Persistence

#### Core Concept
AI Persistence refers to the ability of AI systems to maintain state, knowledge, and learned behaviors across sessions, interactions, or system restarts. This involves saving and loading model states, memories, and learned parameters.

#### Types of Persistence
- **Model Persistence**: Saving trained models and weights
- **Memory Persistence**: Short-term and long-term memory systems
- **State Persistence**: Session state and user preferences

#### Applications
- Chatbots & Virtual Assistants
- Game AI state management
- Recommendation systems
- Learning progress tracking

### 4. User Preferences and Mods System

#### Overview
This section documents the user preferences and mods system architecture for AI development environments. It covers personal working styles, configurable behaviors, and system-level functionality.

#### User Preferences Framework
- **Communication Style**: Concise, actionable responses with examples only when requested
- **Workflow Preferences**: JSON configuration, active git usage, comprehensive documentation
- **Development Approach**: Plan before code, incremental development, comprehensive testing

#### Core Mods
1. **Chat Sound Control**: Toggle chat completion sounds and adjust volume
2. **Cursor Profiles**: Switch between AI personalities (Default, Expert, Mentor, Rapid)
3. **Auto-Execution**: Control code execution permissions (Manual, Semi-auto, Full-auto)
4. **Agent Training**: Configure AI learning modes and skill development
5. **Workflow Preferences**: User-specific workflow automation and context management
6. **AI Personality**: Configure adaptive, consistent, or contextual AI behaviors
7. **Permissions**: Granular access control and security management
8. **AI Behavior Control**: Control AI capabilities and permissions for file operations

### 5. Cursor Integration and Configuration

#### Overview
This section covers the integration of Codex with Cursor IDE, including profile management, AI behavior configuration, and automated setup procedures.

### 6. AI Behavior Standards and Terminology

#### Universal AI Behavior Concepts
- **System Prompts** - The most common term across AI platforms
- **Context Instructions** - Instructions that define AI behavior
- **Behavior Configuration** - How AI should behave in a given context
- **AI Personas** - Different AI personalities/roles
- **Instruction Sets** - Collections of behavioral instructions

#### Technical Implementation Terms
- **Prompt Engineering** - The practice of designing these instructions
- **Context Injection** - Inserting behavior rules into AI conversations
- **Behavioral Conditioning** - Training AI to behave in specific ways
- **Role Definition** - Defining what role the AI should play

#### Platform-Specific Standards
- **OpenAI Function Calling** - Structured behavior definition
- **Claude Constitutional AI** - Built-in behavioral guidelines
- **Anthropic System Prompts** - Standardized instruction format
- **LangChain Agents** - Configurable AI behavior patterns

#### Cross-Platform Integration
- **Universal Standards** - Emerging industry-wide behavior definitions
- **AI Alignment** - Ensuring AI behaves as intended
- **Behavioral Consistency** - Same AI behavior across platforms
- **Contextual Intelligence** - AI adapting to different situations

#### Cursor Profile System
- **Default Profile**: Standard AI coding assistant (helpful, concise, practical)
- **Expert Profile**: Advanced developer with deep technical knowledge
- **Mentor Profile**: Educational approach with detailed explanations
- **Rapid Profile**: Quick implementation focus with minimal explanations

#### AI Behavior Configuration
- **Execution Control**: Manual, Semi-auto, Full-auto modes
- **Code Execution Safety**: Safe operations, approval requirements, confirmation rules
- **Personality Modes**: Adaptive, Consistent, Contextual behaviors
- **Quick Commands**: #verbose, #quick, #explain, #review

#### AI Personality Configuration
- **Style**: Concise and professional
- **Detail Level**: On-demand (provide detailed explanations when asked)
- **Emoji Usage**: Disabled
- **Formality**: Professional
- **Approach**: Analytical and systematic
- **Creativity Level**: Balanced
- **Risk Tolerance**: Calculated

#### Response Guidelines
- Always read relevant files before making suggestions
- Provide multiple solution approaches when appropriate
- Include code examples when relevant
- Explain reasoning behind recommendations
- Use symbolic language system for status tracking (!, @, #, $, %, etc.)

### Integration and Synergies

#### Combined Applications
- **Adaptive Game AI**: Use RL to train agents, apply iterative deepening for decision-making, implement persistence to maintain learned behaviors
- **Learning Systems**: RL agents explore environments, iterative deepening helps in planning and strategy, persistence enables continuous learning

#### Best Practices
- **Mod Development**: Single responsibility, clear naming, minimal dependencies
- **User Experience**: Consistency, discoverability, performance, accessibility
- **Security**: Permission models, audit logging, data protection, escalation control
