# LINUX -- AI Agent System Operations

## Essential Commands

```bash
# Navigation
pwd, cd, ls, find, locate

# File ops
cp, mv, rm, mkdir, touch
cat, head, tail, less
grep, rg, sed, awk

# Process
ps, top, htop, kill, killall
nohup, bg, fg, jobs
```

## Filesystem Navigation

- `/` root; `/home` user dirs; `/tmp` ephemeral
- `~` = `$HOME`
- Use `realpath` for canonical paths; `readlink -f` for symlinks

## Package Management

| Distro | Install | Remove | Update |
|--------|---------|--------|--------|
| Debian/Ubuntu | `apt install pkg` | `apt remove pkg` | `apt update && apt upgrade` |
| Fedora/RHEL | `dnf install pkg` | `dnf remove pkg` | `dnf update` |
| Arch | `pacman -S pkg` | `pacman -Rs pkg` | `pacman -Syu` |

Use `sudo` when installing system-wide.

## Process Management

```bash
ps aux | grep name
kill -9 PID   # force
kill -15 PID  # graceful
pkill -f pattern
```

## systemd

```bash
systemctl start SERVICE
systemctl stop SERVICE
systemctl restart SERVICE
systemctl status SERVICE
systemctl enable SERVICE   # on boot
journalctl -u SERVICE -f   # logs
```

## Permissions

- `chmod +x file` (execute), `chmod 644 file` (rw-r--r--)
- `chown user:group file`
- `umask` controls default permissions

## XDG Compliance

- `$XDG_CONFIG_HOME` (~/.config), `$XDG_DATA_HOME` (~/.local/share), `$XDG_CACHE_HOME` (~/.cache)
- Prefer these over dotfiles in `$HOME` when tools support them

## GPU Drivers

### AMD (ROCm)

```bash
# Ubuntu
apt install rocm

# Verify
rocm-smi
```

### NVIDIA (CUDA)

```bash
# Use distro packages or nvidia driver/CUDA installer
nvidia-smi
nvcc --version  # if CUDA toolkit installed
```

LLM inference: set `GGML_CUDA=ON` when building llama.cpp.
