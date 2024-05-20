package msahmadjaelanibetest.service;

import lombok.RequiredArgsConstructor;
import msahmadjaelanibetest.entity.Account;
import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.model.*;
import msahmadjaelanibetest.repository.AccountRepository;
import msahmadjaelanibetest.repository.UserRepository;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final JwtService jwtService;

    @Transactional
    public String register(RegisterRequest request) {
        Optional<UserDetails> byUsername = accountRepository.findByUsername(request.getUsername());
        if(byUsername.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already registered");
        }

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setAccountNumber(request.getAccountNumber());
        user.setEmailAddress(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRegistrationNumber(request.getRegistrationNumber());

        userRepository.save(user);

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        Account account = new Account();
        account.setAccountId(UUID.randomUUID().toString());
        account.setUserId(user.getUserId());
        account.setUsername(request.getUsername());
        account.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        accountRepository.save(account);

        return "OK";
    }

    public TokenResponse login(LoginRequest request){
        Account account = accountRepository.findFirstByUsername(request.getUsername())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username Or Pasword wrong"));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String token = jwtService.generateToken(account);

        if (passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            account.setToken(token);
            account.setLastLoginDateTime(LocalDateTime.now());
            accountRepository.save(account);

            return TokenResponse.builder()
                    .token(account.getToken())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username Or Pasword wrong");
        }
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }

}
