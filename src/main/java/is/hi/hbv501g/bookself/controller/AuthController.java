package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.AuthResponse;
import is.hi.hbv501g.bookself.dto.LoginRequest;
import is.hi.hbv501g.bookself.dto.RegisterRequest;
import is.hi.hbv501g.bookself.dto.UserResponseDTO;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.security.JwtUtil;
import is.hi.hbv501g.bookself.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController //This class handles HTTP endpoints and will return data directly.
@RequestMapping("/api/auth") //Sets the base URL path for all endpoints in this class.
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil){
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request){
        User user = authService.loginUser(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, UserResponseDTO.fromFromEntity(user)));
    }

    // Logout user: nothing to do server-side. The token is stateless, so
    // "logging out" just means the client discards it. There's no session
    // to invalidate on this end.
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok("Logged out successfully.");
    }

    //Register User
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request){
        User newUser = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(newUser.getUsername());

        URI location = URI.create("api/users/" + newUser.getId());
        return ResponseEntity.created(location)
                .body(new AuthResponse(token, UserResponseDTO.fromFromEntity(newUser)));
    }

    // Only an already-authenticated admin can reach this endpoint at all -
    // enforced declaratively here instead of by hand-checking the caller,
    // the way the old session-based version used to.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(@RequestBody RegisterRequest request){
        User newAdmin = authService.registerAdmin(request.getUsername(), request.getEmail(), request.getPassword());
        URI location = URI.create("/api/users/" + newAdmin.getId());
        return ResponseEntity.created(location).body(UserResponseDTO.fromFromEntity(newAdmin));
    }

}
