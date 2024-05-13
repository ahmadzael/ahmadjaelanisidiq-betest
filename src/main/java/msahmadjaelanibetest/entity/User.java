package msahmadjaelanibetest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {
    //userId, fullName, accountNumber, emailAddress,
    //registrationNumber

    @Id
    private String userId;

    private String fullName;

    private String accountNumber;

    private  String emailAddress;

    private String registrationNumber;
}