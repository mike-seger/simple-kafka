# Simple Kafka

This demonstrates a Kafka Producer and Consumer within a single SpringBoot application.
- The Kafka messages are in AVRO format.
- The application by default accesses a Kafka cluster on localhost:9092.
- By default, the application starts its own embedded Kafka broker. If the port 9092 is already in use, the application will not start its own broker, but connect to the existing one.
- Optionally a full Kafka broker can be run via docker compose.

## Start the application
```
./gradlew bootRun
```

### Start the application on a specific port
```
./gradlew bootRun --args='--server.port=8081'
```

## Access the application
### springdoc-ui
In order to send messages the REST API can be used via UI here:
http://localhost:8080/swagger-ui/index.html#/

### Controlling the listener
| Action                     | URL                                           |
|----------------------------|-----------------------------------------------|
| Send hello message         | GET: http://localhost:8080/send/topic1/hello  |
| Seek back 10 minutes       | GET: http://localhost:8080/seek-back/PT10M    |
| Seek back to the beginning | GET: http://localhost:8080/seek-to-beginning  |
| Seek back to the end       | GET: http://localhost:8080/seek-to-end        |

### Show a JSON message as an AVRO hexdump
Using: https://httpie.io/
```
http --json POST http://localhost:8080/schema/user1/to-avro-hex/ name=Joe2 age:=8000
```

## Running a Kafka cluster, Kafdrop and CMAK (Cluster Manager for Apache Kafka)
This requires the "simple-kafka" application to be stopped beforehand, as it would have started the embedded broker already.
```
docker compose up -d
```

### Access Kafdrop
Open http://localhost:9006/

### Access CMAK
1. Open http://localhost:9000/
2. Only the first time, Cluster/"Add Cluster":
   - Cluster Name: Cluster1
   - Cluster Zookeeper Hosts: localhost:2181
