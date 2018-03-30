# loooger

Kubernetes上で2週間ほど動かしていて、開発時間を取れなくなって挫折した趣味アプリなので公開します。
integrant/ductとk8sの参考になれば幸いです。

## subsystems

- auth.proxyは認証用のリバースプロキシです。
- front.apiはAPIサーバーです。
- front.webはHTMLサーバーです。RumというReactラッパーをつかってSSRしています。

## ミドルウェア群

開発用のミドルウェアはdocker-compose.ymlにおいてあります。
プロダクションではRDBのPodを立てています。

## ビルドパイプライン

GCPのCloud Container Builder APIを使っています。
