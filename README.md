# Clojure Lab 🧪

Entorno de aprendizaje de Clojure **dentro de Docker**, pensado para dominar
4 conceptos del desarrollo interactivo. El REPL corre en el contenedor; tú
editas desde VS Code + Calva en el host y te conectas al REPL del contenedor.

## Arquitectura

```
  VS Code + Calva  (host / macOS)
        │  conecta a nREPL
        ▼
  ┌─────────────────────────┐
  │  Contenedor Docker       │
  │  - Clojure CLI + JDK 21  │
  │  - nREPL  :7888          │  ← Calva
  │  - Portal :5678          │  ← inspector de datos
  └─────────────────────────┘
   src/ montado en vivo (editas en el Mac, el contenedor lo ve)
```

## Requisitos (ya verificados en tu Mac)

- Docker corriendo (OrbStack) ✅
- VS Code ✅
- **Calva** (extensión de VS Code) — instalar una vez:
  ```
  code --install-extension betterthantomorrow.calva
  ```

## Arrancar

```bash
cd ~/clojure-lab
docker compose up --build      # primera vez (baja libs, ~1-2 min)
# siguientes veces:  docker compose up
```

Cuando veas `nREPL server started on port 7888` el entorno está listo.

## Conectar Calva al REPL del contenedor

1. Abre la carpeta `~/clojure-lab` en VS Code.
2. Paleta de comandos (`Cmd+Shift+P`) → **"Calva: Connect to a Running REPL Server in the Project"**.
3. Tipo de proyecto: **deps.edn**.
4. Host:puerto → `localhost:7888`.
5. Abre `src/learn/core.clj` y empieza a evaluar formas con **Cmd+Enter**.

## Los 4 conceptos (todo en `src/learn/core.clj`)

| # | Concepto | Cómo se practica |
|---|----------|------------------|
| 1 | **Interactive development** | Evalúa formas con `Cmd+Enter` / `Cmd+Shift+Enter`; redefine funciones en caliente. |
| 2 | **Debug por inspección inline** | `tap>` para espiar valores + el debugger de Calva (instrumentar forma). |
| 3 | **S-expression editing** | Paredit: slurp/barf, expandir selección, edición del árbol de paréntesis. |
| 4 | **Pretty-print de hashmap grande** | `clojure.pprint/pprint` (texto) y **Portal** en `localhost:5678` (UI navegable). |

## Atajos de Calva más usados

> Atajos definidos en `~/Library/Application Support/Code/User/keybindings.json`
> (reasignados a `Cmd` por el swap Ctrl↔Cmd a nivel de macOS).

| Acción | Atajo (mac) |
|--------|-------------|
| Evaluar forma actual (inline) | `Cmd+Enter` |
| Evaluar top-level | `Cmd+Shift+Enter` |
| Mostrar ventana de output | `Cmd+Alt+Enter` |
| Slurp forward (Paredit) | `Cmd+Alt+.` |
| Barf forward (Paredit) | `Cmd+Alt+,` |
| Expandir selección semántica | paleta → "Calva: Expand Selection" |
| Instrumentar para debug | paleta → "Calva: Instrument Current Form for Debugging" |

## Parar / limpiar

```bash
docker compose down            # parar
docker compose down -v         # parar + borrar cache de libs
```
