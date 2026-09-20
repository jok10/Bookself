package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //This class handles HTTP endpoints and will return data directly.
@RequestMapping("/api/auth") //Sets the base URL path for all endpoints in this class.
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
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

    // Logout user
    //Register User
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session){

        User newUser = authService.registerUser(username, email, password);

        session.setAttribute("USER_ID", newUser.getId()); //Logga inn automatically.

        return new ResponseEntity<>(newUser, HttpStatus.CREATED); //ResponseEntity er pakki sem umvefur nýjan notanda með upplýsingum um hann og hvernig gekk að búa til.
    }

}
