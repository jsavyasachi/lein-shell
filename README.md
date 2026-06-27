# lein-shell

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.savya/lein-shell.svg)](https://clojars.org/net.clojars.savya/lein-shell)
[![cljdoc](https://cljdoc.org/badge/net.clojars.savya/lein-shell)](https://cljdoc.org/d/net.clojars.savya/lein-shell/CURRENT)
[![test](https://github.com/jsavyasachi/lein-shell/actions/workflows/ci.yml/badge.svg)](https://github.com/jsavyasachi/lein-shell/actions/workflows/ci.yml)

A Leiningen plugin for calling shell commands.

## Stack

<a href="https://clojure.org"><img src="https://img.shields.io/badge/Clojure-5881D8?style=flat&logo=clojure&logoColor=fff" alt="Clojure" /></a>
<a href="https://leiningen.org"><img src="https://img.shields.io/badge/Leiningen-2D2D2D?style=flat" alt="Leiningen" /></a>

## Installation

Put `[net.clojars.savya/lein-shell "1.0.2"]` into the `:plugins` vector of your `:user` profile
inside `~/.lein/profiles.clj` if you want to use lein shell on a per user basis
(this doesn't *really* make much sense, but you're allowed to if you want to!).

To explicitly say that this project needs lein-shell to be built, put
`[net.clojars.savya/lein-shell "1.0.2"]` into the `:plugins` vector of your `project.clj`. If you
have no `:plugins` vector in your `project.clj`, it should look like this:

```clj
(defproject your-project-here "version"
 ...
 :plugins [[net.clojars.savya/lein-shell "1.0.2"]]
 ...)
```

## Usage

It is very straightforward to use lein-shell: lein-shell will call the shell
command with eventual parameters you include. For instance, if you want your
favourite cow to say hello to you from Leiningen, the following will be printed
within your shell:

    $ lein shell cowsay 'Hello from Leiningen!'
	 _______________________
    < Hello from Leiningen! >
     -----------------------
            \   ^__^
             \  (oo)\_______
                (__)\       )\/\
                    ||----w |
                    ||     ||

Now, this may look rather useless as you can just omit `lein shell` and get the
exact same result in less time. However, it may be of value if you're using
`make` or `ANTLR` to generate files for you, needed by your Clojure project. For
example, to automatically call `make` before running tasks, add this to your
`project.clj` map:

```clj
:prep-tasks [["shell" "make"] "javac" "compile"]
```

If the command exits with a nonzero exit code, shell will (attempt to) exit
Leiningen with the same exit code. This functionality can be overridden if
desired, and many other settings can be modified as well.

## Documentation

For more information, have a look at [the documentation][documentation]. It
contains a lot of examples, some which hopefully are useful to you.

[documentation]: https://github.com/jsavyasachi/lein-shell/blob/main/doc/DOCUMENTATION.md

## License

Copyright © 2013-2015 Jean Niklas L'orange and [contributors][].

Maintenance fork (2026) by Savyasachi, original: https://github.com/hyPiRion/lein-shell.
Distributed under the [Eclipse Public License 1.0](https://www.eclipse.org/legal/epl-v10.html), preserving the original license.

[contributors]: https://github.com/hyPiRion/lein-shell/contributors

Distributed under the Eclipse Public License, the same as Clojure.
