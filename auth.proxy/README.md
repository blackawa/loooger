# auth.proxy

Authentication proxy server for Web and API server.

## Development

```
lein repl
user=> (go)
;; will start server
user=> (reset)
;; will reload server
user=> (halt)
;; will stop server
```

## Deployment

```
lein do clean, uberjar
```

will create standalone jar file.

You can start this file like

```
PORT=3000 java -jar target/uberjar/auth.proxy-0.1.0-SNAPSHOT.standalone.jar
```

## License

Copyright © 2018 blackawa
Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
