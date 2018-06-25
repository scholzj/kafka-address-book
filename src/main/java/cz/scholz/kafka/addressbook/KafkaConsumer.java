package cz.scholz.kafka.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    AddressBook addressBook;

    @KafkaListener(topics = "${KAFKA_TOPIC}", groupId = "${KAFKA_GROUP_ID}")
    public void addressListener(@Payload(required = false) Address address,
                                @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String id) {
        if (address != null) {
            LOG.info("Received address {}", address);
            addressBook.put(address);
        } else if (addressBook.exists(id)) {
            LOG.info("Address {} was deleted", id);
            addressBook.delete(id);
        }
    }
}
