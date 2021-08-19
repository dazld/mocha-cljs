(ns foo.core-test
  (:require [foo.core :as fc])
  (:require-macros [mocha.support :refer [describe it it-only]]))

(describe "nest"
  (it "is true" (assert true))
  (it "wraps functions" (assert (= 2 (fc/func 1 1))))
  (it "catch assertion failure" (assert (= 3 (fc/func 1 1)))))

(describe "Promises are handled automatically"
  (it "happy path resolution"
      (js/Promise. (fn [res rej]
                     (= 2 (+ 1 1))
                     (res :ok))))
  (it"should catch time outs without any interruption"
      (js/Promise. (fn [res rej]
                     ::nope)))
  (it "should catch rejections"
      (js/Promise. (fn [res rej]
                     (rej (js/Error. "nope")))))
  (it "should catch assertion failures"
      (js/Promise. (fn [res rej]
                     (assert (= 1 2) "uhoh")
                     (res "ok")))))

