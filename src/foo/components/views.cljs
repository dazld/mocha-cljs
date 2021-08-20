(ns foo.components.views
  (:require [reagent.core :as r]))

(defn article [{:keys [title on-click]}]
  (r/with-let [!state (r/atom {:title title})]
    [:article
     [:h1 (:title @!state)]
     [:button.one {:on-click on-click}
      "Click me"]
     [:button.two {:on-click (fn []
                               (swap! !state assoc :title "clicked"))}
      "Other"]]))

