## Mocha <3 CLJS

An experiment in how headless [mocha](https://mochajs.org), [testing-library](https://testing-library.com) and [reagent](http://reagent-project.github.io) might work together with clojurescript and shadow-cljs.

<img width="600" alt="image" src="https://user-images.githubusercontent.com/201036/130189484-b3748bc8-b913-4a3d-8efc-aeb933ad29ab.png">

## Running

- install [entr](http://eradman.com/entrproject/), will be `brew install entr` or the equivalent for most of you.
- Ensure you have node > 12 installed.
- run `npm ci`
- run `make test`
- open up `foo.core-test` in the test directory
- write tests with `assert`

## Mocha API calls available via cljs macros

- `beforeEach`
- `this.timeout`
- `describe`
- `describe.only`
- `xit`
- `it`
- `it.only`

### Example tests
```clj
(describe "foo/func"
  (let [!count (atom 0)]
    (before-each (swap! !count inc))
    (it "is true" (assert true))
    (xit "has a pending test" (assert false))
    (it "wraps functions" (assert (= 2 (fc/func 1 1))))
    (it "catch assertion failure"
        (let [c @!count]
          (assert (pos? c))
          (assert (= 2 (fc/func 1 1)))))))

(describe "Promises are handled automatically"
  (it "happy path resolution"
      (js/Promise. (fn [res rej]
                     (= 2 (+ 1 1))
                     (res :ok))))
  (it "should catch time outs without any interruption"
      (time-out 100)
      (js/Promise. (fn [res rej]
                     ::nope)))
  (it "should catch rejections"
      (js/Promise. (fn [res rej]
                     (rej (js/Error. "nope")))))
  (it "should catch assertion failures"
      (js/Promise. (fn [res rej]
                     (assert (= 1 2) "uhoh")
                     (res "ok")))))
```

### Example rtl (react testing library) test
```clj
(ns foo
  (:require ... ["@testing-library/react" :refer (fireEvent render)] ))

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
```

## Example output

### `spec` reporter

```
➜  mocha-cljs git:(master) ✗ make test

> test
> NODE_ENV=test mocha --exit --reporter spec 'target/*_test.js'

shadow-cljs - config: /Users/dan/projects/cljs/mocha-cljs/shadow-cljs.edn


  0 passing (1ms)

shadow-cljs - server version: 2.15.5 running at http://localhost:9630
shadow-cljs - nREPL server started on port 61024
shadow-cljs - watching build :test
[:test] Configuring build.
[:test] Compiling ...
[:test] Build completed. (44 files, 0 compiled, 0 warnings, 2.14s)

> test
> NODE_ENV=test mocha --exit --reporter spec 'target/*_test.js'



  nest
    ✔ is true
    ✔ wraps functions
    1) catch assertion failure

  Promises are handled automatically
    ✔ happy path resolution
    2) should catch time outs without any interruption
    3) should catch rejections
    4) should catch assertion failures


  3 passing (2s)
  4 failing

  1) nest
       catch assertion failure:
     Error: Assert failed: (= 3 (fc/func 1 1))
      at Context.<anonymous> (.shadow-cljs/builds/test/dev/out/cljs-runtime/foo/core_test.cljs:8:33)
      at processImmediate (node:internal/timers:464:21)

  2) Promises are handled automatically
       should catch time outs without any interruption:
     Error: Timeout of 2000ms exceeded. For async tests and hooks, ensure "done()" is called; if returning a Promise, ensure it resolves. (/Users/dan/projects/cljs/mocha-cljs/target/compiled_test.js)
      at listOnTimeout (node:internal/timers:557:17)
      at processTimers (node:internal/timers:500:7)

  3) Promises are handled automatically
       should catch rejections:
     Error: nope
      at /Users/dan/projects/cljs/mocha-cljs/.shadow-cljs/builds/test/dev/out/cljs-runtime/foo/core_test.cljs:20:27
      at new Promise (<anonymous>)
      at Context.<anonymous> (.shadow-cljs/builds/test/dev/out/cljs-runtime/foo/core_test.cljs:18:3)
      at processImmediate (node:internal/timers:464:21)

  4) Promises are handled automatically
       should catch assertion failures:
     Error: Assert failed: uhoh
(= 1 2)
      at /Users/dan/projects/cljs/mocha-cljs/.shadow-cljs/builds/test/dev/out/cljs-runtime/foo/core_test.cljs:23:22
      at new Promise (<anonymous>)
      at Context.<anonymous> (.shadow-cljs/builds/test/dev/out/cljs-runtime/foo/core_test.cljs:21:3)
      at processImmediate (node:internal/timers:464:21)

```
