package msahmadjaelanibetest.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "kafka_ahmadjaelani_betest", groupId = "kafka_ahmadjaelani_betest")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }

}