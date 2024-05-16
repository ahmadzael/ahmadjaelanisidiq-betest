package msahmadjaelanibetest.controller;

import msahmadjaelanibetest.config.MessageProducer;
import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping(
            path = "/health-check"
    )
    public String healthCheck(){
        return "OK";
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(
            path = "/api/user/register"
    )
    public User createUser(@RequestBody User user) {
        System.out.println("id" + user.getUserId());
        System.out.println("name" + user.getFullName());
        return userRepository.save(user);
    }

    @Autowired
    private MessageProducer messageProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        messageProducer.sendMessage("my-topic", message);
        return "Message sent: " + message;
    }

}
