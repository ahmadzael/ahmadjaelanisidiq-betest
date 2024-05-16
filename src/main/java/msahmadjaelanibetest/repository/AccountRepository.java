package msahmadjaelanibetest.repository;

import msahmadjaelanibetest.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByToken(String token);

    Optional<Account> findFirstByUsername(Object username);

    Optional<UserDetails> findByUsername(Object username);

}
