# kafka-druid-superset
kafka(with kafka streams), druid, superset data pipeline example

To connect debezium to kafka as a raw source, you need to post a request to debezium connector api.

```json
{
  "name": "seouldb-connector",  
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "raw-db",
    "database.port": "3306",
    "database.user": "root",
    "database.password": "1234",
    "database.server.id": "184054",
    "topic.prefix": "dbserver1",
    "database.include.list": "seoulDB", 
    "database.allowPublicKeyRetrieval":"true", 
    // "schema.history.internal.kafka.bootstrap.servers": "kafka:9092",
    "schema.history.internal.kafka.bootstrap.servers": "kafka1:9092,kafka2:9092,kafka3:9092",  
    "schema.history.internal.kafka.topic": "schema-changes.seouldb"  
  }
}
```