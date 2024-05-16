package msahmadjaelanibetest.controller;

import msahmadjaelanibetest.entity.Account;
import msahmadjaelanibetest.entity.User;
import msahmadjaelanibetest.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {
//    get user info by accountNumber
//    get user info by registrationNumber
//    get account login by lastLoginDateTime > 3 days
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(
            path = "/account"
    )
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @GetMapping(
            path = "/account/{days}"
    )
    public List<Account> getAccount(String lastLogin){
        return null;
    }

}
