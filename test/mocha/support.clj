(ns mocha.support)

(defmacro describe
  ([title & forms]
   `(js/describe
      ~title
      (fn []
        ~@forms))))

(defmacro describe-only
  ([title & forms]
   `(js/describe.only
      ~title
      (fn []
        ~@forms))))

(defmacro it
  ([title forms]
   `(js/it
      ~title
       (fn []
         ~forms))))

(defmacro it-only
  ([title forms]
   `(js/it.only
      ~title
      (fn []
        ~forms))))

