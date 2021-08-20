(ns foo.components.views-test
  (:require ["enzyme" :as enzyme]
            ["enzyme-adapter-react-16" :as Adapter]
            [applied-science.js-interop :as j]
            [reagent.core :as r]
            [foo.components.views :as fcv]
            [mocha.support :refer [describe
                                   describe-only
                                   it
                                   time-out
                                   it-only
                                   xit
                                   before-each]]))


(enzyme/configure #js{:adapter (Adapter.)})

(defn shallow
  "Takes a reagent expression and mounts it into enzyme, returning the
  mounted component."
  [component]
  (let [el (r/as-element component)]
    (enzyme/shallow el)))

(describe "Reagent Component"
  (it "asserts click behaviour"
    (let [!count (atom 0)
          mounted (shallow [fcv/article {:on-click #(swap! !count inc)
                                         :title "Title"}])
          button (j/call mounted :find "button")
          _ (j/call button :simulate "click")
          _ (j/call button :simulate "click")]
      (assert (= @!count 2)))))

