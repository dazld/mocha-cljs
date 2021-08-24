(ns foo.components.views
  (:require [reagent.core :as r]))

(defn article [{:keys [title on-click]}]
  (r/with-let [!state (r/atom {:title title})]
    [:article
     [:h1 {:data-testid "maintitle"}
      (:title @!state)]
     [:button.one {:data-testid "a1"
                   :on-click on-click}
      "Click me"]
     [:button.two {:data-testid "a2"
                   :on-click (fn []
                               (swap! !state assoc :title "clicked"))}
      "Other"]]))

