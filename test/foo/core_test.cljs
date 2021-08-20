(ns foo.core-test
  (:require [foo.core :as fc]
            [mocha.support :refer [before-each describe describe-only it it-only xit]]))

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
      (-> (fc/thing+)
          (.then (fn []
                   (+ 1 2 3)))
          (.then (fn []
                   (assert (= 1 2))))))
  (xit "should catch time outs without any interruption"
      (js/Promise. (fn [res rej]
                     ::nope)))
  (it "should catch rejections"
      (js/Promise. (fn [res rej]
                     (rej (js/Error. "nope")))))
  (it "should catch assertion failures"
      (js/Promise. (fn [res rej]
                     (assert (= 1 2) "uhoh")
                     (res "ok")))))

