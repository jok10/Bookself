package is.hi.hbv501g.bookself.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central security configuration. This is what makes authentication
 * happen for every endpoint before a controller is reached, rather than
 * each controller checking it manually - replacing the old pattern of
 * reading session.getAttribute("USER_ID") by hand in every controller.
 *
 * @EnableMethodSecurity turns on @PreAuthorize so admin-only endpoints
 * (e.g. BookController.createBook) can declare that requirement right
 * on the method, the same way verifyAdmin() used to.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Stateless API secured with bearer tokens - CSRF protection
                // exists to stop a browser silently replaying a user's own
                // session cookie; there's no session cookie here to replay.
                .csrf(csrf -> csrf.disable())

                // No HttpSession is created or consulted for auth - every
                // request carries its own proof of identity via the token.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Without this, Spring Security's default fallback for an
                // unauthenticated request is 403 Forbidden, which is wrong:
                // 403 means "you're logged in but not allowed to do this",
                // 401 means "you're not logged in at all". @PreAuthorize
                // failures (an authenticated non-admin hitting an admin
                // endpoint) still correctly produce 403 on their own.
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                .authorizeHttpRequests(auth -> auth
                        // Anyone can register or log in without already holding a token.
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()

                        // Browsing the catalogue and reading reviews doesn't require an account (UC1, UC9, UC10, UC12).
                        .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/reviews/**").permitAll()

                        // H2 console stays reachable for local development/debugging.
                        .requestMatchers("/h2-console/**").permitAll()

                        // When a controller or @PreAuthorize check rejects a
                        // request, the servlet container internally forwards
                        // to /error to render that response. That forward is
                        // a second pass through this same filter chain, and
                        // our JWT filter deliberately skips it (error
                        // dispatches aren't re-authenticated). Without this
                        // line, /error itself gets rejected as unauthenticated
                        // and silently overwrites the real 403/404/etc with a
                        // 401 - the original decision never reaches the client.
                        .requestMatchers("/error").permitAll()

                        // Everything else needs a valid token. Admin-only
                        // endpoints add @PreAuthorize("hasRole('ADMIN')")
                        // on top of this in their own controller methods.
                        .anyRequest().authenticated()
                )

                // The H2 console renders itself inside a frame; browsers block
                // that by default unless we explicitly allow same-origin framing.
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // Run our token check before Spring's own username/password
                // filter, since we're not using that login mechanism at all.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
