package msahmadjaelanibetest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import msahmadjaelanibetest.config.MessageProducer;
import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.model.UsersResponse;
import msahmadjaelanibetest.model.WebResponse;
import msahmadjaelanibetest.repository.UserRepository;
import msahmadjaelanibetest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageProducer messageProducer;
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
            path = "/api/users"
    )
    public User createUser(@RequestBody User user) {
        System.out.println("id" + user.getUserId());
        System.out.println("name" + user.getFullName());
        return userRepository.save(user);
    }

    @GetMapping(
            path ="/api/users/account/{accountnumber}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UsersResponse> getUserInfoByAccountNumber(@PathVariable String accountnumber) throws JsonProcessingException {
        Optional<User> userOptional = userService.getUserInfoByAccountNumber(accountnumber);
        ObjectMapper objectMapper = new ObjectMapper();

        if (userOptional.isPresent()) {
            WebResponse<UsersResponse> result = WebResponse.<UsersResponse>builder().data(
                    UsersResponse.builder()
                            .userId(userOptional.get().getUserId())
                            .emailAddress(userOptional.get().getEmailAddress())
                            .registrationNumber(userOptional.get().getRegistrationNumber())
                            .fullName(userOptional.get().getFullName())
                            .accountNumber(userOptional.get().getAccountNumber())
                            .build()
            ).build();

            String jsonString = objectMapper.writeValueAsString(result);
            System.out.println("dari object mapper"+jsonString);
            //messageProducer.sendMessage("kafka_ahmadjaelani_betest",jsonString);

            Runnable kafkaMessageSender = new Runnable() {
                @Override
                public void run() {
                    try {
                        messageProducer.sendMessage("kafka_ahmadjaelani_betest", jsonString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(kafkaMessageSender).start();
            return result;
        }
        return WebResponse.<UsersResponse>builder().data(null
        ).errors("UserNotFound").build();
    }

    @GetMapping(
            path ="/api/users/reg/{registrationnumber}"
    )
    public WebResponse<UsersResponse> getUserInfoByRegistrationNumber(@PathVariable String registrationnumber) throws JsonProcessingException {
        Optional<User> userOptional = userService.getUserInfoByRegistrationNumber(registrationnumber);
        ObjectMapper objectMapper = new ObjectMapper();

        if (userOptional.isPresent()) {
            WebResponse<UsersResponse> result = WebResponse.<UsersResponse>builder().data(
                    UsersResponse.builder()
                            .userId(userOptional.get().getUserId())
                            .emailAddress(userOptional.get().getEmailAddress())
                            .registrationNumber(userOptional.get().getRegistrationNumber())
                            .fullName(userOptional.get().getFullName())
                            .accountNumber(userOptional.get().getAccountNumber())
                            .build()
            ).build();

            String jsonString = objectMapper.writeValueAsString(result);
            System.out.println("dari object mapper"+jsonString);
            messageProducer.sendMessage("kafka_ahmadjaelani_betest",jsonString);

            return result;
        }
        return WebResponse.<UsersResponse>builder().data(null
        ).errors("UserNotFound").build();
    }

    @PatchMapping(
            path = "/api/users"
    )
    public WebResponse<User> update(User user, @RequestBody User request){
        User userResponse = userService.updateUser(request);

        return WebResponse.<User>builder().data(userResponse).build();
    }


    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        messageProducer.sendMessage("kafka_ahmadjaelani_betest", message);
        return "Message sent: " + message;
    }

}
