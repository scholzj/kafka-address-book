package cz.scholz.kafka.addressbook;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
public class KafkaProducerConfig {
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
    public ProducerFactory<String, Address> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServer);
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

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

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Address> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
