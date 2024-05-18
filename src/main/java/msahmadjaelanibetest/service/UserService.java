package msahmadjaelanibetest.service;

import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.model.UsersResponse;
import msahmadjaelanibetest.model.WebResponse;
import msahmadjaelanibetest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserInfoByRegistrationNumber(String registNo){
        Optional<User> userOptional = userRepository.findFirstByRegistrationNumber(registNo);

        return userOptional;
    }

    public Optional <User> getUserInfoByAccountNumber(String accountNumber) {
        Optional<User> userOptional = userRepository.findByAccountNumber(accountNumber);

        return userOptional;
    }

    @Transactional
    public User updateUser(User request){

        userRepository.save(request);

        return userRepository.findById(request.getUserId()).get();

    }

}
