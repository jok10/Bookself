package is.hi.hbv501g.bookself.security;

import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final UserRepository userRepository;

    public TokenService(JwtEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }


    //    public String createToken(Authentication auth) {
//
//        Instant now = Instant.now();
//
//        List<String> roles = auth.getAuthorities().stream()
//                .map(a -> a.getAuthority().replace("ROLE_", ""))
//                .toList();
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("library-api")
//                .subject(auth.getName())
//                .issuedAt(now)
//                .expiresAt(now.plus(1, ChronoUnit.HOURS))
//                .claim("roles", roles)
//                .build();
//
//        JwsHeader header =
//                JwsHeader.with(MacAlgorithm.HS256).build();
//
//        return encoder
//                .encode(JwtEncoderParameters.from(header, claims))
//                .getTokenValue();
//    }
    public String createToken(Authentication auth) {

        Instant now = Instant.now();

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        List<String> roles = auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("library-api")
                .subject(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .claim("userId", user.getId())
                .claim("roles", roles)
                .build();

        JwsHeader header =
                JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}