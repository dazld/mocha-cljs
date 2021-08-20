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
  "Takes a reagent expression and shallowly mounts it into enzyme, returning the
  mounted component.

  In theory this doesn't need JSDOM, but not sure how that would play with reagent.

  https://enzymejs.github.io/enzyme/docs/api/shallow.html"
  [component]
  (let [el (r/as-element component)]
    (enzyme/shallow el)))

(defn mount
  "Takes a reagent expression and fully mounts it into enzyme, returning the
  mounted component.

  Full DOM rendering is ideal for use cases where you have components that may
  interact with DOM APIs or need to test components that are wrapped in higher
  order components.

  https://enzymejs.github.io/enzyme/docs/api/mount.html"
  [component]
  (let [el (r/as-element component)]
    (enzyme/mount el)))

(describe "[foo.components.views/article ...]"
  (it "asserts click behaviour"
    (let [!count (atom 0)
          mounted (shallow [fcv/article {:on-click #(swap! !count inc)
                                         :title "Title"}])
          button (j/call mounted :find "button.one")
          _ (j/call button :simulate "click")
          _ (j/call button :simulate "click")]
      (assert (= @!count 2))))
  (it "contains the title"
    (let [title (str (random-uuid))
          mounted (shallow [fcv/article {:title title}])
          h1 (j/call mounted :find "h1")]
      (assert (.exists mounted "h1"))
      (assert (= (.text h1)
                 title))))
  (it "asserts against ratom updates"
    (let [mounted (shallow [fcv/article {:title "THING"}])
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
                              "clicked"))))))))



