(ns learn.core
  "A lab to learn the 4 core concepts of interactive Clojure development.

   HOW TO USE THIS FILE:
   1. Connect Calva to the container's REPL (see README).
   2. Place the cursor ON or AFTER each form (parens) and press
      Ctrl+Enter (evaluate current form) or Alt+Enter (evaluate top-level).
   3. The result shows up INLINE, next to the code. That right there is
      concept #1: interactive development."
  (:require [clojure.pprint :as pp]
            [portal.api :as p]))

;; ============================================================
;; CONCEPT 1 — INTERACTIVE DEVELOPMENT
;; ============================================================
;; The idea: you don't restart the program. You keep a live process (the REPL)
;; alive and send it chunks of code that change its state on the fly.

;; Evaluate these forms one by one (cursor on it + Ctrl+Enter):
(+ 1 2 3)                       ; => 6, shows up inline


(def saludo "hola mundo")       ; defines a var in the live REPL

(clojure.string/upper-case saludo)  ; use what you just defined

;; Define a function, evaluate it, try it, MODIFY it and re-evaluate:
;; change the body, press Alt+Enter, and the new version is active
;; instantly. That cycle (edit -> evaluate -> see) is EVERYTHING.
(defn doblar [x]
  (* 2 x))

(doblar 21)                     ; => 42  (change the 2 to a 3 and re-evaluate)

;; A "comment" is your scratchpad: the code inside is NOT run when the file
;; loads, but you CAN evaluate it by hand.
(comment
  (doblar 10)
  (map doblar [1 2 3 4 5])
  )


;; ============================================================
;; CONCEPT 2 — DEBUG USING INLINE INSPECTION
;; ============================================================
;; Two techniques you'll use every day:

;; (a) tap> : sends a value to an "inspector" without disturbing the flow.
;;     tap> returns the same value it receives, so you can drop it
;;     anywhere in a computation to spy on it.
(defn procesar [xs]
  (->> xs
       (filter even?)
       (tap> )            ; <- spy here: see "the even numbers" in Portal
       (map doblar)
       (reduce +)))

;; (b) Calva's DEBUGGER: put the cursor inside `sumar-debug`,
;;     run the command "Calva: Instrument Current Form for Debugging"
;;     (from the Command Palette: Cmd+Shift+P / Ctrl+Shift+P),
;;     then call the function. Calva PAUSES execution and shows you the
;;     value of each symbol INLINE, step by step (Enter = next step).
(defn sumar-debug [a b]
  (let [doble-a (* 2 a)
        doble-b (* 2 b)
        total   (+ doble-a doble-b)]
    total))

(comment
  (procesar [1 2 3 4 5 6])      ; => 24
  (sumar-debug 3 4)             ; instrument it and watch each step
  ;;=> 14
  )


;; ============================================================
;; CONCEPT 3 — S-EXPRESSION EDITING (structural editing / Paredit)
;; ============================================================
;; In Clojure you DON'T edit text, you edit the parenthesis TREE. Calva ships
;; Paredit. Practice with this form (don't delete it, play with it):
;;
;;   - Slurp  forward  (Ctrl+Alt+→): "swallows" the element to
;;                     the right INTO the current parens.
;;   - Barf   forward  (Ctrl+Alt+←): "spits" the last element
;;                     OUT of the parens.
;;   - Expand selection: Ctrl+W (macOS) / Shift+Alt+→ (Win/Linux).
;;   - Raise, splice, etc.
;;
;; Exercise: put the cursor right after (inc 1) and Slurp so it "eats"
;; the  2 , then Barf to release it. Notice how the parens always stay
;; balanced.
(comment
  (+ (inc 1) 2 3)
  )


;; ============================================================
;; CONCEPT 4 — PRETTY-PRINTING A LARGE HASHMAP
;; ============================================================
;; We build a nested map that's "ugly" to read on a single line:
(def datos-grandes
  {:usuario {:id 42
             :nombre "Ada Lovelace"
             :roles #{:admin :editor :revisor}
             :preferencias {:tema "oscuro" :idioma "es" :notificaciones true}}
   :pedidos (vec (for [i (range 1 11)]
                   {:pedido-id i
                    :items (vec (for [j (range 1 4)]
                                  {:sku (str "SKU-" i "-" j)
                                   :cantidad (* i j)
                                   :precio (+ 9.99 (* i 1.5))}))
                    :estado (rand-nth [:pendiente :enviado :entregado])}))
   :metadata {:generado "2026-06-17"
              :version "1.0.0"
              :tags ["demo" "aprendizaje" "clojure"]}})

;; --- Option A: clojure.pprint (always available, goes to the REPL) ---
;; Evaluate this and look at the REPL console: the map comes out indented and
;; readable.
(comment
  (pp/pprint datos-grandes)

  ;; Limit depth/width for HUGE maps:
  (binding [*print-level* 3
            *print-length* 5]
    (pp/pprint datos-grandes))
  )

;; --- Option B: PORTAL (the "systematic" and recommended way) ---
;; Portal opens a navigable UI (tree, table, expand/collapse nodes).
;; It's MUCH better than reading text for large maps.
;;
;; Steps:
;;   1. Evaluate (abrir-portal) ONCE -> opens the UI at localhost:5678.
;;   2. Any (tap> value) or (p/submit value) sends it to Portal.
;;   3. Navigate the data: click to expand, switch the "viewer" (tree/table).
(defn abrir-portal []
  (let [pp (p/open {:host "0.0.0.0" :port 5678 :browser false})]
    ;; wire tap> to Portal: everything you tap> shows up there
    (add-tap #'p/submit)
    pp))

(comment
  (abrir-portal)               ; open Portal -> go to http://localhost:5678
  (tap> datos-grandes)         ; send the big map to Portal and navigate it
  (tap> {:probando 123})       ; any data you want to inspect
  )


;; ------------------------------------------------------------
;; Entry point (not essential for the REPL workflow):
(defn -main [& _]
  (println "Clojure lab ready. Connect with Calva to localhost:7888"))
