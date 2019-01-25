package cz.scholz.kafka.addressbook;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${KAFKA_BOOTSTRAP_SERVER}")
    private String bootstrapServer;

    @Value("${TRUSTSTORE_PATH:#{null}}")
    private String truststorePath;

    @Value("${TRUSTSTORE_PASSWORD:#{null}}")
    private String truststorePassword;

    @Value("${KEYSTORE_PATH:#{null}}")
    private String keystorePath;

    @Value("${KEYSTORE_PASSWORD:#{null}}")
    private String keystorePassword;

    @Value("${USERNAME:#{null}}")
    private String username;

    @Value("${PASSWORD:#{null}}")
    private String password;

    @Bean
    public ConsumerFactory<String, Address> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServer);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // TLS
        if (truststorePassword != null && truststorePath != null)   {
            props.put("security.protocol", "SSL");
            props.put("ssl.truststore.type", "PKCS12");
            props.put("ssl.truststore.password", truststorePassword);
            props.put("ssl.truststore.location", truststorePath);
        }

        if (keystorePassword != null && keystorePath != null)   {
            props.put("security.protocol", "SSL");
            props.put("ssl.keystore.type", "PKCS12");
            props.put("ssl.keystore.password", keystorePassword);
            props.put("ssl.keystore.location", keystorePath);
        }

        if (username != null && password != null)   {
            props.put("sasl.mechanism","SCRAM-SHA-512");
            props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + username + "\" password=\"" + password + "\";");

            if (props.get("security.protocol") != null && props.get("security.protocol").equals("SSL"))  {
                props.put("security.protocol","SASL_SSL");
            } else {
                props.put("security.protocol","SASL_PLAINTEXT");
            }
        }

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Address>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Address> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setAckDiscarded(true);
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);
        return factory;
    }
}
