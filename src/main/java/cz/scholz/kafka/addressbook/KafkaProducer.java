package cz.scholz.kafka.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    @Value("${KAFKA_TOPIC}")
    private String kafkaTopic;

    @Autowired
    private KafkaTemplate<String, Address> kafkaTemplate;

    public void sendMessage(Address address) {
        LOG.info("Updating address {}", address);
        kafkaTemplate.send(kafkaTopic, address.getId(), address);
    }

    public void deleteMessage(String id) {
        LOG.info("Deleting address {}", id);
        kafkaTemplate.send(kafkaTopic, id, null);
    }
}
