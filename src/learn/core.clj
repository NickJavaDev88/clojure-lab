(ns learn.core
  "Laboratorio para aprender los 4 conceptos del desarrollo en Clojure.

   CÓMO USAR ESTE ARCHIVO:
   1. Conecta Calva al REPL del contenedor (ver README).
   2. Coloca el cursor SOBRE o DESPUÉS de cada forma (paréntesis) y pulsa
      Cmd+Enter (evaluar forma actual) o Cmd+Shift+Enter (evaluar top-level).
   3. El resultado aparece INLINE, al lado del código. Eso ya es el
      concepto #1: desarrollo interactivo."
  (:require [clojure.pprint :as pp]
            [portal.api :as p]))

;; ============================================================
;; CONCEPTO 1 — INTERACTIVE DEVELOPMENT (desarrollo interactivo)
;; ============================================================
;; La idea: no reinicias el programa. Mantienes un proceso vivo (el REPL)
;; y le vas mandando trozos de código que modifican su estado al vuelo.

;; Evalúa estas formas una a una (cursor encima + Cmd+Enter):
(+ 1 2 3)                       ; => 6, aparece inline


(def saludo "hola mundo")       ; define una var en el REPL vivo

(clojure.string/upper-case saludo)  ; usa lo que acabas de definir

;; Define una función, evalúala, pruébala, MODIFÍCALA y re-evalúala:
;; cambia el cuerpo, pulsa Cmd+Shift+Enter, y la nueva versión está activa
;; al instante. Ese ciclo (editar -> evaluar -> ver) es TODO.
(defn doblar [x]
  (* 2 x))

(doblar 21)                     ; => 42  (cambia el 2 por 3 y re-evalúa)

;; Un "comment" es tu cuaderno de pruebas: el código de dentro NO se
;; ejecuta al cargar el archivo, pero SÍ puedes evaluarlo a mano.
(comment
  (doblar 10)
  (map doblar [1 2 3 4 5])
  )


;; ============================================================
;; CONCEPTO 2 — DEBUG USING INLINE INSPECTION (depuración inline)
;; ============================================================
;; Dos técnicas que usarás todos los días:

;; (a) tap> : envía un valor a un "inspector" sin ensuciar el flujo.
;;     tap> devuelve el mismo valor que recibe, así que puedes
;;     intercalarlo en cualquier parte de un cálculo para espiarlo.
(defn procesar [xs]
  (->> xs
       (filter even?)
       (tap> )            ; <- espía aquí: ver "los pares" en Portal
       (map doblar)
       (reduce +)))

;; (b) El DEBUGGER de Calva: pon el cursor dentro de `sumar-debug`,
;;     ejecuta el comando "Calva: Instrument Current Form for Debugging"
;;     (desde la paleta Cmd+Shift+P; sin atajo propio por el swap Ctrl/Cmd),
;;     luego llama a la función. Calva PAUSA la
;;     ejecución y te muestra el valor de cada símbolo INLINE, paso a paso
;;     (Enter = siguiente paso).
(defn sumar-debug [a b]
  (let [doble-a (* 2 a)
        doble-b (* 2 b)
        total   (+ doble-a doble-b)]
    total))

(comment
  (procesar [1 2 3 4 5 6])      ; => 24
  (sumar-debug 3 4)             ; instrumenta y observa cada paso
  ;;=> 14
  )


;; ============================================================
;; CONCEPTO 3 — S-EXPRESSION EDITING (edición estructural / Paredit)
;; ============================================================
;; En Clojure NO editas texto, editas el ÁRBOL de paréntesis. Calva trae
;; Paredit. Practica con esta forma (no la borres, juega con ella):
;;
;;   - Slurp  forward  (Cmd+Alt+.): "traga" el elemento de
;;                     la derecha DENTRO del paréntesis actual.
;;   - Barf   forward  (Ctrl+Alt+, o Ctrl+Shift+J): "escupe" el último
;;                     elemento FUERA del paréntesis.
;;   - Expandir selección semántica: Ctrl+Shift+Flecha (crece por formas).
;;   - Subir/raise, splice, etc.
;;
;; Ejercicio: pon el cursor justo después de (inc 1) y haz Slurp para que
;; se "coma" el  2 , luego Barf para soltarlo. Ves cómo el paréntesis
;; respeta siempre el balanceo.
(comment
  (+ (inc 1) 2 3)
  )


;; ============================================================
;; CONCEPTO 4 — PRETTY PRINT DE UN HASHMAP GRANDE
;; ============================================================
;; Generamos un mapa anidado y "feo" de ver en una sola línea:
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

;; --- Opción A: clojure.pprint (siempre disponible, va al REPL) ---
;; Evalúa esto y mira la consola del REPL: el mapa sale indentado y legible.
(comment
  (pp/pprint datos-grandes)

  ;; Limita la profundidad/anchura para mapas ENORMES:
  (binding [*print-level* 3
            *print-length* 5]
    (pp/pprint datos-grandes))
  )

;; --- Opción B: PORTAL (la forma "sistemática" y recomendada) ---
;; Portal abre una UI navegable (árbol, tabla, expandir/colapsar nodos).
;; Es MUCHO mejor que leer texto para mapas grandes.
;;
;; Pasos:
;;   1. Evalúa (abrir-portal) UNA vez -> abre la UI en localhost:5678.
;;   2. Cualquier (tap> valor) o (p/submit valor) lo manda a Portal.
;;   3. Navega el dato: clic para expandir, cambia la "viewer" (tree/table).
(defn abrir-portal []
  (let [pp (p/open {:host "0.0.0.0" :port 5678 :browser false})]
    ;; conecta tap> a Portal: todo lo que hagas tap> aparece allí
    (add-tap #'p/submit)
    pp))

(comment
  (abrir-portal)               ; abre Portal -> ve a http://localhost:5678
  (tap> datos-grandes)         ; manda el mapa grande a Portal y navégalo
  (tap> {:probando 123})       ; cualquier dato que quieras inspeccionar
  )


;; ------------------------------------------------------------
;; Punto de entrada (no imprescindible para el flujo REPL):
(defn -main [& _]
  (println "Laboratorio Clojure listo. Conéctate con Calva a localhost:7888"))
