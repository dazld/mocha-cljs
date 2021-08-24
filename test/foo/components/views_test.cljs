(ns foo.components.views-test
  (:require [applied-science.js-interop :as j]
            [reagent.core :as r]
            ["@testing-library/react" :refer (fireEvent render)]
            [foo.components.views :as fcv]
            [mocha.support :refer [describe
                                   describe-only
                                   it
                                   time-out
                                   it-only
                                   xit
                                   before-each]]
            [promesa.core :as p]))

(defn click [el]
  (j/call fireEvent :click el "click"))

(defn by-test-id [^js el test-id]
  (.getByTestId el test-id))

(defn render-el
  "Takes a reagent expression and mounts it into jsdom, returning the mounted component."
  [component]
  (let [el (r/as-element component)]
    (render el)))

(describe "using promesa"
  (it "changes state"
    (time-out 10000)
    (p/let [!count (r/atom 0)
            ^js el (render-el [fcv/article {:on-click #(swap! !count inc)}])
            a1 (.findByTestId el "a1")
            a2 (.findByTestId el "a2")
            a3 (.findByTestId el "a3")]
      (click a1)
      (assert (= 1 @!count))
      (click a2)
      (-> (js/Promise. (fn [res rej]
                         (js/setTimeout res 2000)))
          (.then #(.findByText el "clicked")))
      (click a3)
      (.findByText el "final"))))

(describe "[foo.components.views/article ...]"
  (it "asserts click behaviour"
    (let [!count (atom 0)
          mounted (render-el [fcv/article {:on-click #(swap! !count inc)
                                           :title "Title"}])
          button (by-test-id mounted "a1")]
      (click button)
      (click button)
      (assert (= @!count 2))))
  (it "contains the title"
    (let [title (str (random-uuid))
          mounted (render-el [fcv/article {:title title}])
          h1 (by-test-id mounted "maintitle")]
      (assert h1 "Title Exists")))

  (it "asserts against ratom updates"
    (let [mounted (render-el [fcv/article {:title "THING"}])
          button (by-test-id mounted "a2")]
      (assert (.getByText mounted "THING"))
      (click button)
      ;; Returns a promise to a query, or throws failing the test
      (.findByText mounted "clicked"))))
