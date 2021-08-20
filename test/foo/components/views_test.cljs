(ns foo.components.views-test
  (:require ["enzyme" :as enzyme]
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

(defn shallow
  "Takes a reagent expression and mounts it into enzyme, returning the
  mounted component."
  [component]
  (let [el (r/as-element component)]
    (enzyme/shallow el)))

(describe "[foo.components.views/article ...]"
  (it "asserts click behaviour"
    (let [!count (atom 0)
          mounted (shallow [fcv/article {:on-click #(swap! !count inc)
                                         :title "Title"}])
          button (j/call mounted :find "button")
          _ (j/call button :simulate "click")
          _ (j/call button :simulate "click")]
      (assert (= @!count 2))))
  (it "contains the title"
    (let [mounted (shallow [fcv/article {:title "THING"}])
          h1 (j/call mounted :find "h1")]
      (assert (= (.text h1)
                 "NOTHING")))))

