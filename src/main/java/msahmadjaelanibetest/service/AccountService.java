package msahmadjaelanibetest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import msahmadjaelanibetest.config.MessageProducer;
import msahmadjaelanibetest.entity.Account;
import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.model.AccountResponse;
import msahmadjaelanibetest.model.WebResponse;
import msahmadjaelanibetest.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MessageProducer messageProducer;

    public WebResponse<List<AccountResponse>> getAccountByLastLogin(int lastLoginDays) throws JsonProcessingException {
        List<Account> listAccount = accountRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LocalDateTime limitDate = LocalDateTime.now().minusDays(lastLoginDays);
        System.out.println(limitDate);
        List<AccountResponse> accountFiltered = new ArrayList<>();

         for (var account:listAccount){
             if (account.getLastLoginDateTime() != null) {
                 if (account.getLastLoginDateTime().isAfter(limitDate)) {
                     accountFiltered.add(AccountResponse.builder()
                             .accountId(account.getAccountId())
                             .userId(account.getUserId())
                             .username(account.getUsername())
                             .lastLoginDateTime(account.getLastLoginDateTime())
                             .build());
                 }
             }
         }


        WebResponse<List<AccountResponse>> result = WebResponse.<List<AccountResponse>>builder()
                .data(accountFiltered)
                .build();
        String jsonString = objectMapper.writeValueAsString(result);
        System.out.println("dari object mapper"+jsonString);

        messageProducer.sendMessage("kafka_ahmadjaelani_betest",jsonString);

        return result;

    }

    public AccountResponse updateAccount(Account account,Account request){

        if(Objects.nonNull(request.getPassword())){
            request.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
        }

        accountRepository.save(request);

        return AccountResponse.builder()
                .username(request.getUsername())
                .lastLoginDateTime(account.getLastLoginDateTime())
                .accountId(account.getAccountId())
                .userId(account.getAccountId())
                .build();
    }

}
