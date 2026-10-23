package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.LoginRequest;
import is.hi.hbv501g.bookself.dto.RegisterRequest;
import is.hi.hbv501g.bookself.dto.UserResponseDTO;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.UserRepository;
import is.hi.hbv501g.bookself.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController //This class handles HTTP endpoints and will return data directly.
@RequestMapping("/api/auth") //Sets the base URL path for all endpoints in this class.
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository){
        this.authService = authService;
        this.userRepository = userRepository;
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(
            @RequestBody LoginRequest request,
            HttpSession session){

        User user = authService.loginUser(request.getUsername(), request.getPassword());

        session.setAttribute("USER_ID", user.getId()); //Sama session-attribute og eftir skráningu.

        return ResponseEntity.ok(UserResponseDTO.fromFromEntity(user));
    }

    // Logout user
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    //Register User
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @RequestBody RegisterRequest request,
            HttpSession session){

        User newUser = authService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());

        session.setAttribute("USER_ID", newUser.getId()); //Logga inn automatically.

        URI location = URI.create("api/users/" + newUser.getId());

        return ResponseEntity.created(location).body(UserResponseDTO.fromFromEntity(newUser));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(
            @RequestBody RegisterRequest request, HttpSession session){
       //Check if anyone is logged in
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged in as an admin to create an admin account.");
        }

        // Fetch the logged in user from database
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));

        // Verify that logged-in user is ADMIN
        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only administrators can create admin accounts.");
        }
        User newAdmin = authService.registerAdmin(request.getUsername(), request.getEmail(), request.getPassword());

        URI location = URI.create("/api/users/" + newAdmin.getId());
        return ResponseEntity.created(location).body(UserResponseDTO.fromFromEntity(newAdmin));
    }

}
