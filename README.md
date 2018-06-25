# Kafka Address Book

This project is a REST based address book backed by Kafka as the data store. To use it, you can use following commands:

```bash
curl -sv -X POST -H "Content-Type: application/json" --data '{ "name": "Jakub Scholz", "address": "Some address" }' http://localhost:8080/addressbook/
curl -vs http://localhost:8080/addressbook/
curl -vs -X PUT -H "Content-Type: application/json" --data '{ "name": "Jakub Scholz", "address": "Some other address" }' http://localhost:8080/addressbook/SmFrdWIgU2Nob2x6
curl -vs http://localhost:8080/addressbook/SmFrdWIgU2Nob2x6
curl -sv -X DELETE http://localhost:8080/addressbook/SmFrdWIgU2Nob2x6
```