package msahmadjaelanibetest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account")
public class Account {

    //accountId, userName, password, lastLoginDateTime, userId

    @Id
    private String accountId;

    private String userName;

    private String password;

    private LocalDateTime lastLoginDateTime;

    private String userId;
}
