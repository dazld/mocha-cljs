(ns foo.test-main
  (:require ["jsdom" :as jsdom]
            [applied-science.js-interop :as j]
            foo.core-test
            foo.components.views-test))

(let [dom (jsdom/JSDOM. "<!doctype html><html><body></body></html>")]
  (j/assoc! js/global :window (.-window dom))
  (j/assoc! js/global :document (j/get-in dom [:window :document])))

(defn go []
  :does-nothing)
