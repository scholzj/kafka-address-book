# Kafka Address Book

This project is a REST based address book backed by Kafka as the data store. 
It is written using Spring framework.
It will listen on port 8080.

## Configuration

It is configured using environment variables:

* `KAFKA_BOOTSTRAP_SERVER`: Address of the Kafka bootstrap server (one of the Kafka brokers)
* `KAFKA_TOPIC`: Kafka topic which should be used to store data  
* `KAFKA_GROUP_ID`: group ID which the application should use

## UI

The application has a simple UI for managing the addresses.
To open the UI, go to your browser and open [http://localhost:8080/](http://localhost:8080/) or the address where it is running.
The UI is based on []this blog post](http://www.baeldung.com/angular-js-rest-api). 

## REST interface

You can also interact with the REST API directly - for example using `curl`.

```bash
curl -sv -X POST -H "Content-Type: application/json" --data '{ "name": "Jakub Scholz", "address": "Some address" }' http://localhost:8080/addressbook/
curl -vs http://localhost:8080/addressbook/
curl -vs -X PUT -H "Content-Type: application/json" --data '{ "name": "Jakub Scholz", "address": "Some other address" }' http://localhost:8080/addressbook/SmFrdWIgU2Nob2x6
curl -vs http://localhost:8080/addressbook/SmFrdWIgU2Nob2x6
curl -sv -X DELETE http://localhost:8080/addressbook/SmFrdWIgU2Nob2x6
```

## Kubernetes / OpenShift deployment

To deploy on OpenShift or Kubernetes, you can used the attached [deployment.yaml](deployment.yaml) file