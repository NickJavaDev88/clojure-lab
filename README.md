# Clojure Lab 🧪

> 🌐 **Languages:** **English** · [繁體中文](https://github.com/NickJavaDev88/clojure-lab/blob/zh-tw/README.md) · [Español](https://github.com/NickJavaDev88/clojure-lab/blob/es/README.md)

A hands-on Clojure learning environment that runs **inside Docker**, built to
practice 4 core ideas of interactive development. The REPL runs in the
container; you edit from VS Code + Calva on your host machine and connect to the
container's REPL. No JDK or Clojure install needed on your computer — Docker
handles everything.

## Architecture

```
  VS Code + Calva  (your machine / macOS or Linux)
        │  connects to nREPL
        ▼
  ┌──────────────────────────┐
  │  Docker container         │
  │  - Clojure CLI + JDK 21   │
  │  - nREPL  :7888           │  ← Calva connects here
  │  - Portal :5678           │  ← data inspector UI
  └──────────────────────────┘
   src/ is mounted live (you edit on the host, the container sees it instantly)
```

## Prerequisites & installation

You only need **Docker**, **Git**, **VS Code**, and the **Calva** extension.
Clojure and the JDK live inside the container, so you don't install them.

### macOS

```bash
# 1. Docker runtime (OrbStack recommended — lightweight; Docker Desktop also works)
#    + VS Code, via Homebrew:
brew install --cask orbstack visual-studio-code

# 2. Git (skip if you already have it — comes with Xcode Command Line Tools)
xcode-select --install

# 3. Calva extension for VS Code
code --install-extension betterthantomorrow.calva
```

Then launch OrbStack (or Docker Desktop) once so the Docker engine is running.

### Linux

```bash
# 1. Docker Engine + the compose plugin
#    Follow the official guide for your distro:
#    https://docs.docker.com/engine/install/
#    (make sure the "docker compose" plugin is included)

# 2. Git + VS Code (Debian/Ubuntu example)
sudo apt update && sudo apt install -y git
#    VS Code: https://code.visualstudio.com/docs/setup/linux

# 3. Calva extension for VS Code
code --install-extension betterthantomorrow.calva
```

## Get the code

```bash
git clone https://github.com/NickJavaDev88/clojure-lab.git
cd clojure-lab
```

## Start the lab

```bash
docker compose up --build -d   # first time: downloads libs (~1–2 min)
# next times:  docker compose up -d
docker compose ps              # check status and ports
docker logs clojure-lab        # watch the startup logs
```

The environment is ready when the logs show:

```
nREPL server started on port 7888
```

## Connect Calva to the container's REPL

1. Open the `clojure-lab` folder in VS Code.
2. Command Palette (`Cmd+Shift+P` on macOS / `Ctrl+Shift+P` on Linux) →
   **"Calva: Connect to a Running REPL Server in the Project"**.
3. Project type: **deps.edn**.
4. Host:port → `localhost:7888`.
5. Open `src/learn/core.clj` and start evaluating forms with **`Ctrl+Enter`**.

> The warning `No nrepl port file found` is harmless — Calva connects via the
> explicit port anyway.

## The 4 concepts (all in `src/learn/core.clj`)

| # | Concept | How you practice it |
|---|---------|---------------------|
| 1 | **Interactive development** | Evaluate forms with `Ctrl+Enter` / `Alt+Enter`; redefine functions live without restarting. |
| 2 | **Debug via inline inspection** | `tap>` to spy on values + Calva's debugger (instrument a form). |
| 3 | **S-expression editing** | Paredit: slurp/barf, expand selection, edit the parenthesis tree structurally. |
| 4 | **Pretty-printing a large hashmap** | `clojure.pprint/pprint` (text) and **Portal** at `localhost:5678` (navigable UI). |

## Keyboard shortcuts (Calva defaults)

These are Calva's out-of-the-box shortcuts — no custom config needed.

| Action | macOS | Windows / Linux |
|--------|-------|-----------------|
| Evaluate current form (inline) | `Ctrl+Enter` | `Ctrl+Enter` |
| Evaluate top-level form | `Alt+Enter` | `Alt+Enter` |
| Evaluate enclosing form | `Ctrl+Shift+Enter` | `Ctrl+Shift+Enter` |
| Slurp forward (Paredit) | `Ctrl+Alt+→` | `Ctrl+Alt+→` *(Linux: `Ctrl+Alt+.`)* |
| Barf forward (Paredit) | `Ctrl+Alt+←` | `Ctrl+Alt+←` *(Linux: `Ctrl+Alt+,`)* |
| Expand selection | `Ctrl+W` | `Shift+Alt+→` |
| Instrument form for debugging | Command Palette → *"Calva: Instrument Current Form for Debugging"* | same |

> The output/results window is opened from the Command Palette
> (*"Calva: Show Output Window"*) — it has no default keybinding.

## Stop / clean up

```bash
docker compose down            # stop
docker compose down -v         # stop + delete the Maven cache volume
```
