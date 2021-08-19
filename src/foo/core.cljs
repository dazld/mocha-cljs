(ns foo.core)

(defn func [a b]
  (+ a b))

(defn thing+ []
  (js/Promise. (fn [res rej]
                 (res "ok"))))
