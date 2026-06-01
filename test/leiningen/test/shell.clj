(ns leiningen.test.shell
  (:use [clojure.test])
  (:require [leiningen.core.eval :as eval]
            [leiningen.core.main :as main]
            [leiningen.core.utils :as utils]
            [leiningen.shell :as shell]))

(def ^:private replace-values @#'leiningen.shell/replace-values)
(def ^:private param-expand @#'leiningen.shell/param-expand)
(def ^:private lookup-command @#'leiningen.shell/lookup-command)
(def ^:private get-exit-code @#'leiningen.shell/get-exit-code)
(def ^:private get-pipe-stdin? @#'leiningen.shell/get-pipe-stdin?)

(deftest test-replacement
  (let [p {:a {:a 'a :b 1 :c "2"}
           :foo "banana"
           :bar {:baz "zap"}}]
    (is (= (replace-values p "foo bar baz") "foo bar baz"))
    (is (= (replace-values p "${:foo}") "banana"))
    (is (= (replace-values p "${:baz:-not-found}") "not-found"))
    (is (= (replace-values p "${:baz:-foo: ${:foo}}") "foo: banana"))
    (is (= (replace-values p "${:recursive:-10 ${:thing:-delicious ${:foo}}s}")
           "10 delicious bananas"))

    (is (= (replace-values p "${[:a :b]}") "1"))
    (is (= (replace-values p "${[:a :c]}") "2"))
    (is (= (replace-values p "${[:a :a]}") "a"))

    (is (thrown-with-msg? Exception #"Unexpected end of argument"
                          (replace-values p "${:a")))
    (is (thrown-with-msg? Exception #"Unexpected end of argument"
                          (replace-values p "${:a:-Only partially closed: ${:b}")))

    (is (thrown-with-msg? Exception #"Expected \{ after \$"
                          (replace-values p "$foo")))))

(deftest test-param-expand
  (let [p {:foo "banana"}]
    (testing "strings are run through parameter expansion"
      (is (= (param-expand p "${:foo}") "banana"))
      (is (= (param-expand p "plain") "plain")))
    (testing "non-strings are stringified, not expanded"
      (is (= (param-expand p 42) "42"))
      (is (= (param-expand p :kw) ":kw"))
      (is (= (param-expand p [1 2]) "[1 2]"))
      (is (= (param-expand p nil) "")))))

(deftest test-lookup-command
  (let [os (utils/get-os)
        p {:shell {:commands {"as-string"  {os "winner"}
                              "as-vector"  {os ["a" "b"]}
                              "fallback"   {:default-command "catch-all"}
                              "prefer-os"  {os "os-cmd"
                                            :default-command "default-cmd"}}}}]
    (testing "string replacement is normalized to a vector and prepended to args"
      (is (= (lookup-command p ["as-string" "arg1" "arg2"])
             '("winner" "arg1" "arg2"))))
    (testing "vector replacement is spliced ahead of the original args"
      (is (= (lookup-command p ["as-vector" "arg"])
             '("a" "b" "arg"))))
    (testing ":default-command is used when there is no os-specific match"
      (is (= (lookup-command p ["fallback" "arg"])
             '("catch-all" "arg"))))
    (testing "an os-specific command takes priority over :default-command"
      (is (= (lookup-command p ["prefer-os" "arg"])
             '("os-cmd" "arg"))))
    (testing "unknown commands pass through untouched"
      (is (= (lookup-command p ["unknown" "arg"])
             ["unknown" "arg"])))))

(deftest test-get-setting-priority
  (testing "exit-code: command-level > global > built-in default"
    (is (= (get-exit-code {} ["cmd"]) :default))
    (is (= (get-exit-code {:shell {:exit-code :ignore}} ["cmd"]) :ignore))
    (is (= (get-exit-code {:shell {:exit-code :ignore
                                   :commands {"cmd" {:exit-code :default}}}}
                          ["cmd"])
           :default)))
  (testing "pipe-stdin?: built-in default true, falsey overrides are honored"
    (is (= (get-pipe-stdin? {} ["cmd"]) true))
    (is (= (get-pipe-stdin? {:shell {:pipe-stdin? false}} ["cmd"]) false))
    (is (= (get-pipe-stdin? {:shell {:pipe-stdin? false
                                     :commands {"cmd" {:pipe-stdin? true}}}}
                            ["cmd"])
           true))))

(deftest test-exit-code-dispatch
  (testing ":default action exits with the process' nonzero exit code"
    (let [exited (atom :untouched)]
      (with-redefs [eval/sh   (fn [& _] 3)
                    main/exit (fn [code] (reset! exited code))]
        (shell/shell {} "false")
        (is (= @exited 3)))))
  (testing ":default action does not exit when the process succeeds"
    (let [exited (atom :untouched)]
      (with-redefs [eval/sh   (fn [& _] 0)
                    main/exit (fn [code] (reset! exited code))]
        (shell/shell {} "true")
        (is (= @exited :untouched)))))
  (testing ":ignore action never exits, even on a nonzero exit code"
    (let [exited (atom :untouched)]
      (with-redefs [eval/sh   (fn [& _] 5)
                    main/exit (fn [code] (reset! exited code))]
        (shell/shell {:shell {:exit-code :ignore}} "false")
        (is (= @exited :untouched))))))
