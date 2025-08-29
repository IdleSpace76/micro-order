```
./mvnw spring-boot:build-image
```
```
docker run -d -p 8080:8080 --name micro-order order:0.0.1-SNAPSHOT
```
