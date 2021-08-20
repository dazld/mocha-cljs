(ns foo.core-test
  (:require [foo.core :as fc]
            [mocha.support :refer [time-out
                                   before-each describe describe-only it it-only xit]]))

(describe "foo/func"
  (let [!count (atom 0)]
    (before-each (swap! !count inc))
    (it "is true" (assert true))
    (xit "has a pending test" (assert false))
    (it "wraps functions" (assert (= 2 (fc/func 1 1))))
    (it "catch assertion failure"
        (let [c @!count]
          (assert (pos? c))
          (assert (= 2 (fc/func 1 1)))))))

(describe "Promises are handled automatically"
  (it "happy path resolution"
    (-> (fc/thing+)
        (.then (fn []
                 (assert (= 2 2))))))
  (it "happy path rejection"
    (let [!caught (atom nil)]
      (-> (fc/thing+)
          (.then (fn []
                   (+ 1 2 3)))
          (.then (fn []
                   (assert (= 1 2))))
          (.catch (fn [err]
                    (reset! !caught err)))
          (.then (fn []
                   (assert (instance? js/Error @!caught)))))))
  (it "should warn about time outs without any interruption"
    ;; Try different times here
    (time-out 40)
    (js/Promise. (fn [res rej]
                   (js/setTimeout res 25)))))


