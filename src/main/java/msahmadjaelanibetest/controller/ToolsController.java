package msahmadjaelanibetest.controller;

import msahmadjaelanibetest.config.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class ToolsController {
    @Autowired
    private MessageProducer messageProducer;

    @PostMapping("/api/tools/test-producer")
    public String sendMessage(@RequestParam("message") String message) {
        messageProducer.sendMessage("kafka_ahmadjaelani_betest", message);
        return "Message sent: " + message;
    }

    @GetMapping(
            path = "/api/tools/health-check"
    )
    public String healthCheck(){
        return "OK";
    }
}
