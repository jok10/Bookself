package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.UserRepository;
import is.hi.hbv501g.bookself.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<User> loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session){

        User user = authService.loginUser(username, password);

        session.setAttribute("USER_ID", user.getId()); //Sama session-attribute og eftir skráningu.

        return ResponseEntity.ok(user);
    }

    // Logout user?
    //Register User
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session){

        User newUser = authService.registerUser(username, email, password);

        session.setAttribute("USER_ID", newUser.getId()); //Logga inn automatically.

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<User> registerAdmin(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password, HttpSession session){
       //Check if anyone is logged in
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged in to create an admin account.");
        }

        // Fetch the logged in user from database
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));

        // Verify that logged-in user is ADMIN
        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Only administrators can create admin accounts.");
        }
        User newAdmin = authService.registerAdmin(username, email, password);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

}
