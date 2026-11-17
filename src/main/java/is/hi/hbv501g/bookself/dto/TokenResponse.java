package is.hi.hbv501g.bookself.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing the authentication response payload,
 * containing the generated JWT token and its expiration time in seconds.
 */
@Data
@AllArgsConstructor
public class TokenResponse {

    private String token;
    private long expiresIn;


}