package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(String username, String email, String password){
        if(username == null || username.isBlank() ||
        email == null || email.isBlank()||
        password == null || password.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username, email, and password are required.");
        }

        if(userRepository.existsByUsername(username)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username '" + username + "' is taken.");
        }

        if(userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email '" + email + "' is already registered.");
        }

        //Hugsanlega setja eitthvað um að athuga hvort að password sé nógu gott.

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

}
