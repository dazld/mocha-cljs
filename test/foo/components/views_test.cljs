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
                                   before-each]]))

(defn click [el]
  (j/call fireEvent :click el "click"))

(defn by-test-id [^js el test-id]
  (.getByTestId el test-id))

(defn render-el
  "Takes a reagent expression and mounts it into jsdom, returning the mounted component."
  [component]
  (let [el (r/as-element component)]
    (render el)))

(describe-only "[foo.components.views/article ...]"
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
      (assert h1 "Title Exists"))))


(xit "asserts against ratom updates"
  (let [mounted (render-el [fcv/article {:title "THING"}])
        button (j/call mounted :find "button.two")]

    (assert (= (.text (j/call mounted :find "h1"))
               "THING"))
    (j/call button :simulate "click")
    (-> (js/Promise. (fn [res]
                       ;; This should really be invoked after a hook of some sort has been called
                       ;; indicating that reagent has processed updates.
                       (js/setTimeout res 16)))
        (.then (fn []
                 (assert (= (.text (j/call mounted :find "h1"))
                            "clicked")))))))



