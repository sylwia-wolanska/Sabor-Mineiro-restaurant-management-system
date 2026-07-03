package pjatk.mas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
