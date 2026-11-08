package is.hi.hbv501g.bookself.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Creates and reads the JWTs used instead of HTTP sessions. A token is a
 * signed, self-contained claim of "I am this username, until this time" -
 * the server doesn't store anything about it; it just re-checks the
 * signature and expiry whenever the token comes back in a request.
 */
@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * True only if the token's signature is valid, it hasn't expired, and
     * the username inside it matches the account it's being presented for.
     * Any parsing failure (bad signature, malformed token) is treated as
     * invalid rather than thrown further - a forged or corrupted token
     * should just fail authentication, not crash the request.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);
            boolean usernameMatches = claims.getSubject().equals(userDetails.getUsername());
            boolean notExpired = claims.getExpiration().after(new Date());
            return usernameMatches && notExpired;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
