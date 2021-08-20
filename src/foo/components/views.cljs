(ns foo.components.views)

(defn article [{:keys [title on-click]}]
  [:article
   [:h1 title]
   [:button {:on-click on-click}
    "Click me"]])

