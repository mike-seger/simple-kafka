# start instance
```
./gradlew bootRun
```

## start instance on specific port
```
./gradlew bootRun --args='--server.port=8081'
```

# access springdoc-ui
http://localhost:8080/swagger-ui/index.html#/

# openapi json
http://localhost:8080/v3/api-docs

# Example API calls
| Action                     | URL                                     |
|----------------------------|-----------------------------------------|
| Send hello message         | http://localhost:8080/send/topic1/hello |
| Seek back 10 minutes       | http://localhost:8080/seek-back/PT10M   |
| Seek back to the beginning | http://localhost:8080/seek-to-beginning |
| Seek back to the end       | http://localhost:8080/seek-to-end       |
