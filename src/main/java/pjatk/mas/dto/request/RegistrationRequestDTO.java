package pjatk.mas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String phoneNumber;

    @Email
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
