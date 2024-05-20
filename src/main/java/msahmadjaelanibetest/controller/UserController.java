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


    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping(
            path = "/api/users/{id}"
    )
    public WebResponse<String> delete(@PathVariable String id){
        String result = "Ok";
        userService.deleteUser(id);
        return WebResponse.<String>builder().data("OK").build();
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

    @GetMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UsersResponse> getUserInfo(
            @RequestParam(value = "account", required = false) String accountNumber,
            @RequestParam(value = "reg", required = false) String registrationNumber) throws JsonProcessingException {

        if (accountNumber == null && registrationNumber == null) {
            return WebResponse.<UsersResponse>builder().data(null).errors("InvalidRequest: Either account or registration number must be provided").build();
        }

        Optional<User> userOptional = Optional.empty();
        if (accountNumber != null) {
            userOptional = userService.getUserInfoByAccountNumber(accountNumber);
        } else if (registrationNumber != null) {
            userOptional = userService.getUserInfoByRegistrationNumber(registrationNumber);
        }

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
            System.out.println("dari object mapper" + jsonString);

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
        return WebResponse.<UsersResponse>builder().data(null).errors("UserNotFound").build();
    }
}
