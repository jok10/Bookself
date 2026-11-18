package is.hi.hbv501g.bookself.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an incoming login request payload,
 * containing the user's username and password along with validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank (message = "Username cannot be blank")
    private String username;

    @NotBlank (message = "Password cannot be blank")
    private String password;
}
