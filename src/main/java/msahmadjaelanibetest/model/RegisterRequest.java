package msahmadjaelanibetest.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;

    private String password;

    private String username;

    private String fullName;

    private String accountNumber;

    private String registrationNumber;
}
