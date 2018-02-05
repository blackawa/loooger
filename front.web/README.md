# front.web

Frontend web application.

## Deployment

Submit project to Google Cloud Container Builder.

```
gcloud container builds submit --config=cloudbuild.yml .
gcloud container builds submit --config=cloudbuild.migrator.yml .
```

## Developing

### Setup

```sh
lein duct setup
```

### Environment

```sh
lein repl
```

```clojure
user=> (dev)
:loaded
dev=> (go)
;; various logs...
:initiated
dev=> (reset)
:reloading (...)
:resumed
```

If you want to access a ClojureScript REPL, make sure that the site is loaded
in a browser and run:

```clojure
dev=> (cljs-repl)
Waiting for browser connection... Connected.
To quit, type: :cljs/quit
nil
cljs.user=>
```

### Testing

Testing is fastest through the REPL, as you avoid environment startup
time.

```clojure
dev=> (test)
...
```

But you can also run tests through Leiningen.

```sh
lein test
```

## Legal

Copyright © 2018 blackawa
