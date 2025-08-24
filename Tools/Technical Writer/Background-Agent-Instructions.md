# Technical Writer Background Agent - Deployment Instructions

**Version**: 1.0.2  
**Last Updated**: August 23, 2025  
**Tool**: Technical Writer (Agentic logger)

## Overview

The Technical Writer tool can be deployed as a **Cursor Background Agent** to provide automated development logging and documentation. This guide explains how to set up, configure, and run the Technical Writer as a background service.

## Prerequisites

### Required Components
- **Git Repository**: Must be a valid git repository (public or private)
- **Cursor IDE**: Version that supports background agents
- **GitHub/GitLab Access**: For repository deployment
- **File System Permissions**: Access to create and write log files

### Repository Structure
```
technical-writer-agent/
├── .git/
├── README.md
├── agent.json                    # Cursor background agent configuration
├── prompts/
│   └── technical-writer.md      # Technical Writer prompt
├── tools/
│   └── logging/
│       ├── worklog.py           # Work logging functionality
│       ├── buildlog.py          # Build logging functionality
│       └── changelog.py         # Change logging functionality
└── config/
    ├── logging.yaml             # Logging configuration
    └── permissions.yaml         # Permission settings
```

## Step 1: Create Agent Configuration

### Important: Configuration vs Log Files
- **Configuration Files** (`.json`, `.yaml`): Define how the agent behaves and where logs are stored
- **Log Files** (`.log`): Contain the actual logged data and are created automatically by the agent
- **All logs are plain text `.log` files** - never YAML or JSON format

### Create `agent.json`
```json
{
  "name": "Technical Writer Agent",
  "description": "Automated development logging and documentation agent",
  "version": "1.0.2",
  "author": "Your Name",
  "repository": "https://github.com/username/technical-writer-agent",
  "tools": [
    "logging",
    "git-integration",
    "file-monitoring",
    "cursor-api"
  ],
  "triggers": [
    "file-change",
    "git-commit",
    "git-branch",
    "error-occurrence",
    "build-completion"
  ],
  "permissions": {
    "read-workspace": true,
    "read-git": true,
    "write-logs": true,
    "read-file-content": true,
    "execute-commands": false
  },
  "logging": {
    "base-directory": "/var/log/{project_name}",
    "log-levels": ["worklog", "buildlog", "changelog", "errors", "server"],
    "retention": "30 days",
    "compression": true
  },
  "monitoring": {
    "file-types": [".py", ".js", ".ts", ".java", ".cpp", ".md", ".yaml", ".json"],
    "exclude-patterns": ["node_modules/", "venv/", ".git/", "*.log"],
    "scan-interval": "30 seconds"
  }
}
```

### Create `config/logging.yaml`
```yaml
# Logging Configuration for Technical Writer Agent
# Note: This file configures logging behavior - actual logs are .log files
logging:
  base_directory: "/var/log/{project_name}"
  directories:
    worklog: "worklog"
    buildlog: "buildlog"
    changelog: "changelog"
    errors: "errors"
    server: "server"
  
  file_permissions: "644"
  directory_permissions: "755"
  
  formats:
    timestamp: "[DD-MM-YYYY HH:MM:SS]"
    enriched_timestamp: "[author@workspace DD-MM-YYYY HH:MM:SS on git_branch]"
  
  retention:
    worklog: "90 days"
    buildlog: "60 days"
    changelog: "90 days"
    errors: "30 days"
    server: "180 days"
  
  compression:
    enabled: true
    threshold: "7 days"
    algorithm: "gzip"
```

## Step 2: Deploy as Background Agent

### Option A: Deploy from Repository
1. **Push to GitHub/GitLab**: Ensure your repository is accessible
2. **In Cursor**: Go to Settings → Background Agents
3. **Add Agent**: Click "Add Background Agent"
4. **Repository URL**: Enter your repository URL
5. **Install**: Cursor will install and configure the agent

### Option B: Local Development Setup
1. **Clone Repository**: `git clone <your-repo-url>`
2. **Install Dependencies**: `pip install -r requirements.txt`
3. **Configure Permissions**: Set up log directory permissions
4. **Test Locally**: Run agent in development mode

## Step 3: Initial Configuration

### First Run Setup
When the agent first runs, it will prompt for configuration:

```bash
# Agent will create and prompt for:
1. Project name and workspace label
2. Developer identifier (author)
3. Logging preferences and verbosity
4. File monitoring scope and exclusions
5. Git integration settings
6. Notification preferences
```

### Permission Setup
```bash
# Create log directories with proper permissions
sudo install -d -m 755 -o root -g root \
  "$BASE_DIR" "$WORKLOG_DIR" "$BUILDLOG_DIR" \
  "$CHANGELOG_DIR" "$ERRORS_DIR" "$PROMPT_HISTORY_DIR"

# Create log files
sudo touch "$WORKLOG" "$BUILD_LOG" "$CHANGE_LOG" \
  "$ERROR_LOG" "$SERVER_LOG" "$PROMPT_HISTORY_FILE"
```

## Step 4: Agent Operation

### Log File Structure
The agent creates **plain text `.log` files** (never YAML or JSON):

```
/var/log/{project_name}/
├── worklog/
│   └── worklog.log              # Plain text work entries
├── buildlog/
│   └── build.log                # Plain text build entries  
├── changelog/
│   └── change.log               # Plain text change entries
├── errors/
│   └── error.log                # Plain text error entries
└── server.log                    # Plain text server deployment entries
```

### Automatic Monitoring
The agent will automatically:

- **Monitor File Changes**: Track modifications to source files
- **Log Git Activities**: Record commits, branches, and merges
- **Track Build Processes**: Monitor compilation and build outputs
- **Capture Errors**: Log errors and warnings automatically
- **Generate Reports**: Create periodic activity summaries

### Log Entry Formats

#### Worklog Entries
```
[23-08-2025 14:30:15] [P1] Implement user authentication system #worklog #in-development
[23-08-2025 15:45:22] [P2] Fix login form validation #worklog #completed
```

#### Buildlog Entries
```
[23-08-2025 16:20:33] feat(auth): add JWT token validation files:[src/auth/jwt.py, tests/test_jwt.py]
[23-08-2025 16:25:18] fix(auth): resolve token expiration bug files:[src/auth/token.py]
```

#### Changelog Entries
```
[23-08-2025 16:30:00] Added JWT authentication system with token validation #changelog
[23-08-2025 16:35:00] Fixed token expiration handling in authentication flow #changelog
```

## Step 5: Integration Commands

### Agent Control Commands
```bash
# Check agent status
#agent-status

# View recent logs
#agent-logs --recent 50

# Generate activity report
#agent-report --period "last-week"

# Pause monitoring temporarily
#agent-pause

# Resume monitoring
#agent-resume

# Configure agent settings
#agent-configure
```

### Log Access Commands
```bash
# View worklog entries
tail -n 50 "$WORKLOG"

# View buildlog entries
tail -n 50 "$BUILD_LOG"

# View changelog entries
tail -n 50 "$CHANGE_LOG"

# View error log
tail -n 50 "$ERROR_LOG"

# View server deployment log
head -n 200 "$SERVER_LOG"
```

## Step 6: Customization

### Modify Monitoring Scope
Edit `config/logging.yaml`:
```yaml
monitoring:
  file_types: [".py", ".js", ".ts", ".java", ".cpp", ".md"]
  exclude_patterns: ["node_modules/", "venv/", ".git/", "*.log"]
  include_patterns: ["src/", "lib/", "tests/"]
  scan_interval: "30 seconds"
```

### Adjust Logging Levels
```yaml
logging:
  levels:
    worklog: "INFO"
    buildlog: "DEBUG"
    changelog: "INFO"
    errors: "WARNING"
    server: "INFO"
```

### Custom Triggers
```yaml
triggers:
  file_change:
    enabled: true
    threshold: "1 second"
  git_commit:
    enabled: true
    include_message: true
  build_completion:
    enabled: true
    success_only: false
```

## Step 7: Troubleshooting

### Common Issues

#### Permission Denied
```bash
# Check log directory permissions
ls -la /var/log/{project_name}

# Fix permissions if needed
sudo chown -R $USER:$USER /var/log/{project_name}
sudo chmod -R 755 /var/log/{project_name}
```

#### Agent Not Starting
```bash
# Check agent logs
tail -f /var/log/cursor/background-agents.log

# Verify configuration
python -m json.tool agent.json

# Check dependencies
pip list | grep -E "(cursor|logging|git)"
```

#### Logs Not Being Written
```bash
# Verify file paths
echo $BASE_DIR
ls -la $WORKLOG_DIR

# Check file permissions
ls -la $WORKLOG

# Test write access
echo "test" >> $WORKLOG
```

### Debug Mode
Enable debug logging in `config/logging.yaml`:
```yaml
logging:
  debug: true
  verbose: true
  log_to_console: true
```
```

## Step 8: Maintenance

### Regular Tasks
- **Log Rotation**: Monitor log file sizes and implement rotation
- **Permission Audits**: Regularly review file and directory permissions
- **Performance Monitoring**: Ensure agent doesn't impact development speed
- **Configuration Updates**: Keep agent configuration current with project needs

### Backup Strategy
```bash
# Backup log directories
tar -czf logs-backup-$(date +%Y%m%d).tar.gz /var/log/{project_name}

# Backup configuration
cp -r config/ config-backup-$(date +%Y%m%d)/
```

## Support and Updates

### Getting Help
- **Documentation**: Check the main Codex repository for updates
- **Issues**: Report problems through GitHub issues
- **Community**: Join Codex user discussions and forums

### Version Updates
```bash
# Pull latest changes
git pull origin main

# Update dependencies
pip install -r requirements.txt --upgrade

# Restart agent
#agent-restart
```

---

**Note**: These instructions assume you have administrative access to your system for creating log directories. Adjust permissions and paths according to your specific environment and security requirements.
