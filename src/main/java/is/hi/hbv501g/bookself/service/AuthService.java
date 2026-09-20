package is.hi.hbv501g.bookself.service;
import is.hi.hbv501g.bookself.model.User;

public interface AuthService {
    User registerUser(String username, String email, String password);
    User loginUser(String username, String password);
}
