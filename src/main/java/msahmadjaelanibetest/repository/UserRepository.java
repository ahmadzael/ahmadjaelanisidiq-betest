package msahmadjaelanibetest.repository;

import msahmadjaelanibetest.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findFirstByRegistrationNumber(Object registrationNumber);

    Optional<User> findByAccountNumber(Object accountNumber);

    Optional<User> findByUserId(Object userId);
}
