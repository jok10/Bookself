package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.LoginRequest;
import is.hi.hbv501g.bookself.dto.RegisterRequest;
import is.hi.hbv501g.bookself.dto.UserResponseDTO;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.AuthService;
import is.hi.hbv501g.bookself.dto.TokenResponse;
import is.hi.hbv501g.bookself.security.TokenService;
import is.hi.hbv501g.bookself.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.net.URI;


@RestController //This class handles HTTP endpoints and will return data directly.
@RequestMapping("/api/auth") //Sets the base URL path for all endpoints in this class.
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, TokenService tokenService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication auth =
                authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(
                                request.getUsername(),
                                request.getPassword()));
        return new TokenResponse(tokenService.createToken(auth), 3600);
    }

    // Logout user
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok("Logged out successfully.");
    }

    //Register User
    @PostMapping("/register")
    public TokenResponse registerUser(@Valid @RequestBody RegisterRequest request) {

        authService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword());

        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.getUsername(),
                        request.getPassword()));

        return new TokenResponse(tokenService.createToken(auth), 3600);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(
            @Valid @RequestBody RegisterRequest request, @AuthenticationPrincipal Jwt jwt) {
        // Fetch the logged in user from database
        User currentUser = userService.getUserByUsername(jwt.getSubject());

        // Verify that logged-in user is ADMIN
        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only administrators can create admin accounts.");
        }
        User newAdmin = authService.registerAdmin(request.getUsername(), request.getEmail(), request.getPassword());

        URI location = URI.create("/api/users/" + newAdmin.getId());
        return ResponseEntity.created(location).body(UserResponseDTO.fromFromEntity(newAdmin));
    }
}
