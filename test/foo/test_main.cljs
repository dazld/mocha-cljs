(ns foo.test-main
  (:require ["jsdom" :as jsdom]
            ["enzyme" :as enzyme]
            ["enzyme-adapter-react-16" :as Adapter]
            [applied-science.js-interop :as j]
            foo.core-test
            foo.components.views-test))

(enzyme/configure #js{:adapter (Adapter.)})

(let [dom (jsdom/JSDOM. "<!doctype html><html><body></body></html>")]
  (j/assoc! js/global :window (.-window dom))
  (j/assoc! js/global :document (j/get-in dom [:window :document])))

(defn go []
  :does-nothing)
