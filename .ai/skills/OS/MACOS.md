# MACOS -- AI Agent System Operations

## Essential Commands

```bash
# Navigation (same as Linux)
pwd, cd, ls, find

# File ops
cp, mv, rm, mkdir
cat, head, tail
grep, rg

# Process
ps aux, top, kill, killall
launchctl  # for services
```

## Homebrew

```bash
brew install pkg
brew uninstall pkg
brew update && brew upgrade
brew list
brew doctor
```

Primary package manager. Prefer it over system Python/ruby.

## launchd

macOS replacement for systemd/cron:

```bash
launchctl load ~/Library/LaunchAgents/com.example.service.plist
launchctl unload ~/Library/LaunchAgents/com.example.service.plist
launchctl list
launchctl start SERVICE
launchctl stop SERVICE
```

Plist location: `~/Library/LaunchAgents/` (user) or `/Library/LaunchDaemons/` (system).

## Metal GPU Framework

- Default GPU API on Apple Silicon and Intel Macs
- llama.cpp: `-DGGML_METAL=ON` for Metal acceleration
- `system_profiler SPDisplaysDataType` for GPU info

## XDG on macOS

- Many tools respect XDG; set if unset:
  ```bash
  export XDG_CONFIG_HOME="${XDG_CONFIG_HOME:-$HOME/.config}"
  export XDG_DATA_HOME="${XDG_DATA_HOME:-$HOME/.local/share}"
  export XDG_CACHE_HOME="${XDG_CACHE_HOME:-$HOME/.cache}"
  ```

## Xcode Command Line Tools

Required for compilation (Clang, make, git):

```bash
xcode-select --install
```

## Security & Privacy

- **Gatekeeper**: blocks unsigned apps; `xattr -d com.apple.quarantine file` to allow
- **SIP** (System Integrity Protection): do not disable; avoid modifying `/System`
- **Full Disk Access**: needed for some automation; grant in System Settings → Privacy

## Disk & Volume Management

```bash
diskutil list
diskutil mount /dev/diskNsM
diskutil unmount /Volumes/Name
df -h
```
