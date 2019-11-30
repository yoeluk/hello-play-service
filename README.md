## Hello Play Service

### Build

```
> sbt docker:publishLocal
```

### Run
```
> docker run -it -e BEARER_TOKEN=<GCloud Translate Api Toke> -p 9000:9000 yoeluk/hello-play-service:0.3 -Dpidfile.path=/dev/null -Dplay.http.secret.key=ad31779d4ee49d5ad5162bf1429c32e2e9933f3b
```