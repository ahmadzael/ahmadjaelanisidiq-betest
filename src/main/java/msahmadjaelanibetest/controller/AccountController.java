package msahmadjaelanibetest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import msahmadjaelanibetest.entity.Account;
import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.model.AccountResponse;
import msahmadjaelanibetest.model.WebResponse;
import msahmadjaelanibetest.repository.AccountRepository;
import msahmadjaelanibetest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping(
            path = "/account"
    )
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @GetMapping(
            path = "/api/account/{lastLogin}"
    )
    public WebResponse<List<AccountResponse>> getAccountByLastLogin(@PathVariable int lastLogin) throws JsonProcessingException {

        return accountService.getAccountByLastLogin(lastLogin);
    }

    @PatchMapping(
            path = "/api/account/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AccountResponse> update(Account account, @RequestBody Account request){
        AccountResponse accountResponse = accountService.updateAccount(account, request);

        return WebResponse.<AccountResponse>builder().data(accountResponse).build();
    }

}
