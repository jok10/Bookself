package is.hi.hbv501g.bookself.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Configuration class for JWT (JSON Web Token) encoding, decoding, and secret key management.
 */
@Configuration
public class JwtConfig {

    /**
     * Creates and decodes the secret key used for signing and verifying JWTs.
     *
     * @param secret the base64-encoded secret from application properties
     * @return the SecretKey for HMAC-SHA256
     */
    @Bean
    SecretKey jwtSecretKey(@Value("${jwt.secret}") String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    /**
     * Creates a JwtEncoder bean to generate tokens for authenticated users.
     *
     * @param key the secret key used for signing tokens
     * @return the configured JwtEncoder
     */
    @Bean
    JwtEncoder jwtEncoder(SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    /**
     * Creates a JwtDecoder bean to validate and parse incoming JWTs using HS256.
     *
     * @param key the secret key used for verification
     * @return the configured JwtDecoder
     */
    @Bean
    JwtDecoder jwtDecoder(SecretKey key) {
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
