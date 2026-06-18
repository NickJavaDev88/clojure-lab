(ns learn.core
  "學習互動式 Clojure 開發 4 個核心概念的實驗檔。

   如何使用本檔案：
   1. 將 Calva 連線到容器的 REPL（見 README）。
   2. 把游標放在每個 form（括號）的「上面」或「後面」，然後按
      Ctrl+Enter（求值目前的 form）或 Alt+Enter（求值 top-level）。
   3. 結果會「行內」顯示在程式碼旁邊。這就是概念 #1：互動式開發。"
  (:require [clojure.pprint :as pp]
            [portal.api :as p]))

;; ============================================================
;; 概念 1 — 互動式開發（INTERACTIVE DEVELOPMENT）
;; ============================================================
;; 重點：你不重啟程式。你讓一個行程（REPL）持續存活，然後把一段段程式碼
;; 送進去，即時改變它的狀態。

;; 逐一求值這些 form（游標放上去 + Ctrl+Enter）：
(+ 1 2 3)                       ; => 6，行內顯示


(def saludo "hola mundo")       ; 在存活的 REPL 裡定義一個 var

(clojure.string/upper-case saludo)  ; 使用你剛剛定義的東西

;; 定義一個函式、求值、試用，接著「修改」它再重新求值：
;; 改一下函式本體，按 Alt+Enter，新版本就立刻生效。
;; 這個循環（編輯 -> 求值 -> 看結果）就是一切。
(defn doblar [x]
  (* 2 x))

(doblar 21)                     ; => 42 （把 2 改成 3 再重新求值）

;; 「comment」是你的試驗草稿區：裡面的程式碼在載入檔案時「不會」執行，
;; 但你「可以」手動逐一求值。
(comment
  (doblar 10)
  (map doblar [1 2 3 4 5])
  )


;; ============================================================
;; 概念 2 — 以行內檢視進行除錯（DEBUG USING INLINE INSPECTION）
;; ============================================================
;; 兩個你每天都會用到的技巧：

;; (a) tap> ：把一個值送到「檢視器」，又不打擾原本的資料流。
;;     tap> 會回傳它收到的同一個值，所以你可以把它插在計算的
;;     任何位置來窺看資料。
(defn procesar [xs]
  (->> xs
       (filter even?)
       (tap> )            ; <- 在這裡窺看：在 Portal 看到「那些偶數」
       (map doblar)
       (reduce +)))

;; (b) Calva 的「除錯器」：把游標放進 `sumar-debug` 裡，
;;     執行命令「Calva: Instrument Current Form for Debugging」
;;     （從命令面板：Cmd+Shift+P / Ctrl+Shift+P），
;;     接著呼叫該函式。Calva 會「暫停」執行，並一步步「行內」顯示
;;     每個符號的值（Enter = 下一步）。
(defn sumar-debug [a b]
  (let [doble-a (* 2 a)
        doble-b (* 2 b)
        total   (+ doble-a doble-b)]
    total))

(comment
  (procesar [1 2 3 4 5 6])      ; => 24
  (sumar-debug 3 4)             ; instrument 它並觀察每一步
  ;;=> 14
  )


;; ============================================================
;; 概念 3 — S-EXPRESSION 編輯（結構式編輯 / Paredit）
;; ============================================================
;; 在 Clojure 裡你「不是」在編輯文字，而是在編輯括號的「樹」。Calva 內建
;; Paredit。用這個 form 來練習（別刪掉它，拿它來玩）：
;;
;;   - Slurp  forward（Ctrl+Alt+→）：把右邊的元素「吞」進
;;                     目前的括號內。
;;   - Barf   forward（Ctrl+Alt+←）：把最後一個元素「吐」到
;;                     括號外。
;;   - 擴展選取範圍：Ctrl+W（macOS）/ Shift+Alt+→（Win/Linux）。
;;   - Raise、splice 等等。
;;
;; 練習：把游標放在 (inc 1) 後面，做 Slurp 讓它「吃掉」那個  2 ，
;; 再做 Barf 把它放回去。注意括號永遠保持平衡。
(comment
  (+ (inc 1) 2 3)
  )


;; ============================================================
;; 概念 4 — 大型 HASHMAP 的美化輸出
;; ============================================================
;; 我們建立一個巢狀、單行看起來很「醜」的 map：
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

;; --- 選項 A：clojure.pprint（隨時可用，輸出到 REPL）---
;; 求值這段，然後看 REPL 主控台：map 會以縮排、易讀的方式印出。
(comment
  (pp/pprint datos-grandes)

  ;; 對「超大」的 map 限制深度／寬度：
  (binding [*print-level* 3
            *print-length* 5]
    (pp/pprint datos-grandes))
  )

;; --- 選項 B：PORTAL（「系統化」且推薦的做法）---
;; Portal 會開啟一個可瀏覽的 UI（樹狀、表格、展開/收合節點）。
;; 對於大型 map，這比讀文字「好太多」。
;;
;; 步驟：
;;   1. 求值 (abrir-portal) 「一次」-> 在 localhost:5678 開啟 UI。
;;   2. 任何 (tap> 值) 或 (p/submit 值) 都會送到 Portal。
;;   3. 瀏覽資料：點擊展開，切換「viewer」（tree/table）。
(defn abrir-portal []
  (let [pp (p/open {:host "0.0.0.0" :port 5678 :browser false})]
    ;; 把 tap> 接到 Portal：你 tap> 的所有東西都會出現在那裡
    (add-tap #'p/submit)
    pp))

(comment
  (abrir-portal)               ; 開啟 Portal -> 前往 http://localhost:5678
  (tap> datos-grandes)         ; 把大型 map 送到 Portal 並瀏覽它
  (tap> {:probando 123})       ; 任何你想檢視的資料
  )


;; ------------------------------------------------------------
;; 進入點（對 REPL 工作流程而言並非必要）：
(defn -main [& _]
  (println "Clojure 實驗環境就緒。用 Calva 連線到 localhost:7888"))
