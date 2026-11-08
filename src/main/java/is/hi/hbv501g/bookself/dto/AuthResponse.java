package is.hi.hbv501g.bookself.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned from register/login. The client stores the token and sends it
 * back as "Authorization: Bearer <token>" on every later request instead
 * of relying on a session cookie.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserResponseDTO user;
}
