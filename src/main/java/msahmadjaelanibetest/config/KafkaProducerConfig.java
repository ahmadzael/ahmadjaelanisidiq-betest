package msahmadjaelanibetest.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroup;

    @Value("${spring.kafka.bootstrap-servers}")
    private String server;
    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.session.timeout.ms}")
    private String sessionTimeoutMs;

    @Value("${spring.kafka.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.schema.registry.basic.auth.user-info}")
    private String schemaRegistryBasicAuthUserInfo;

    @Bean
    public ProducerFactory<String, String> producerFactory() {


        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Set security-related properties
        configProps.put("sasl.mechanism", saslMechanism);
        configProps.put("security.protocol", securityProtocol);
        configProps.put("sasl.jaas.config", saslJaasConfig);

        // Set session timeout property for higher availability
        configProps.put("session.timeout.ms", sessionTimeoutMs);

        // Set schema registry properties if required
        configProps.put("schema.registry.url", schemaRegistryUrl);
        configProps.put("basic.auth.credentials.source", "USER_INFO");
        configProps.put("schema.registry.basic.auth.user.info", schemaRegistryBasicAuthUserInfo);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
