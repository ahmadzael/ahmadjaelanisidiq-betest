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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

        if(accountRepository.existsById(request.getUsername())){
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
        account.setUsername(request.getEmail());
        account.setPassword(bCrypt.encode(request.getPassword()));

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
            account.setTokenExpiredAt(next30Days());
            account.setLastLoginDateTime(LocalDateTime.now());
            accountRepository.save(account);

            return TokenResponse.builder()
                    .token(account.getToken())
                    .expiredAt(account.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username Or Pasword wrong");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }

    private Long next30Days(){
        return System.currentTimeMillis() + (1000*16*24*30);
    }


}
