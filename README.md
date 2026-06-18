# Clojure Lab 🧪

> 🌐 **語言：** [English](https://github.com/NickJavaDev88/clojure-lab/blob/master/README.md) · **繁體中文** · [Español](https://github.com/NickJavaDev88/clojure-lab/blob/es/README.md)

一個在 **Docker 容器內執行**的 Clojure 實作學習環境，用來練習互動式開發的
4 個核心概念。REPL 跑在容器裡；你在自己電腦上用 VS Code + Calva 編輯，
並連線到容器裡的 REPL。你的電腦**不需要安裝 JDK 或 Clojure**——一切交給
Docker 處理。

## 架構

```
  VS Code + Calva  (你的電腦 / macOS 或 Linux)
        │  連線到 nREPL
        ▼
  ┌──────────────────────────┐
  │  Docker 容器              │
  │  - Clojure CLI + JDK 21   │
  │  - nREPL  :7888           │  ← Calva 連到這裡
  │  - Portal :5678           │  ← 資料檢視器 UI
  └──────────────────────────┘
   src/ 以即時掛載方式共享（你在電腦上編輯，容器立刻看得到）
```

## 事前準備與安裝

你只需要 **Docker**、**Git**、**VS Code** 以及 **Calva** 擴充套件。
Clojure 與 JDK 都在容器裡，所以你不必自行安裝。

### macOS

```bash
# 1. Docker 執行環境（推薦 OrbStack——輕量；Docker Desktop 也可以）
#    以及 VS Code，透過 Homebrew 安裝：
brew install --cask orbstack visual-studio-code

# 2. Git（已安裝可略過——隨 Xcode Command Line Tools 一起提供）
xcode-select --install

# 3. VS Code 的 Calva 擴充套件
code --install-extension betterthantomorrow.calva
```

接著啟動一次 OrbStack（或 Docker Desktop），確保 Docker 引擎正在執行。

### Linux

```bash
# 1. Docker Engine 與 compose 外掛
#    依你的發行版參考官方指南：
#    https://docs.docker.com/engine/install/
#    （請確認含有 "docker compose" 外掛）

# 2. Git 與 VS Code（以 Debian/Ubuntu 為例）
sudo apt update && sudo apt install -y git
#    VS Code: https://code.visualstudio.com/docs/setup/linux

# 3. VS Code 的 Calva 擴充套件
code --install-extension betterthantomorrow.calva
```

## 取得程式碼

```bash
git clone https://github.com/NickJavaDev88/clojure-lab.git
cd clojure-lab
```

## 啟動實驗環境

```bash
docker compose up --build -d   # 第一次：會下載相依套件（約 1–2 分鐘）
# 之後：       docker compose up -d
docker compose ps              # 查看狀態與連接埠
docker logs clojure-lab        # 觀察啟動日誌
```

當日誌出現以下訊息時，環境就準備好了：

```
nREPL server started on port 7888
```

## 將 Calva 連線到容器的 REPL

1. 在 VS Code 開啟 `clojure-lab` 資料夾。
2. 命令面板（macOS 按 `Cmd+Shift+P` / Linux 按 `Ctrl+Shift+P`）→
   **「Calva: Connect to a Running REPL Server in the Project」**。
3. 專案類型：**deps.edn**。
4. 主機:連接埠 → `localhost:7888`。
5. 開啟 `src/learn/core.clj`，用 **`Ctrl+Enter`** 開始求值各個 form。

> 出現 `No nrepl port file found` 警告是無害的——Calva 仍會透過明確的連接埠連線。

## 4 個核心概念（全部都在 `src/learn/core.clj`）

| # | 概念 | 怎麼練習 |
|---|------|----------|
| 1 | **互動式開發** | 用 `Ctrl+Enter` / `Alt+Enter` 求值 form；不必重啟即可即時重新定義函式。 |
| 2 | **以行內檢視進行除錯** | 用 `tap>` 窺看數值 + Calva 的除錯器（instrument 某個 form）。 |
| 3 | **S-expression 編輯** | Paredit：slurp/barf、擴展選取範圍、以結構方式編輯括號樹。 |
| 4 | **大型 hashmap 的美化輸出** | `clojure.pprint/pprint`（文字）與 **Portal**（`localhost:5678` 的可瀏覽 UI）。 |

## 鍵盤快捷鍵（Calva 預設值）

以下是 Calva 的預設快捷鍵——不需要任何自訂設定。

| 動作 | macOS | Windows / Linux |
|------|-------|-----------------|
| 求值目前的 form（行內） | `Ctrl+Enter` | `Ctrl+Enter` |
| 求值 top-level form | `Alt+Enter` | `Alt+Enter` |
| 求值外層 form | `Ctrl+Shift+Enter` | `Ctrl+Shift+Enter` |
| Slurp forward（Paredit） | `Ctrl+Alt+→` | `Ctrl+Alt+→` *(Linux: `Ctrl+Alt+.`)* |
| Barf forward（Paredit） | `Ctrl+Alt+←` | `Ctrl+Alt+←` *(Linux: `Ctrl+Alt+,`)* |
| 擴展選取範圍 | `Ctrl+W` | `Shift+Alt+→` |
| Instrument form 以便除錯 | 命令面板 → *「Calva: Instrument Current Form for Debugging」* | 同左 |

> 輸出/結果視窗從命令面板開啟（*「Calva: Show Output Window」*）——沒有預設快捷鍵。

## 停止 / 清理

```bash
docker compose down            # 停止
docker compose down -v         # 停止 + 刪除 Maven 快取磁碟區
```
