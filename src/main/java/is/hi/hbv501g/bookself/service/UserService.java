package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getUserById(Long id);
    User getUserByUsername(String username);

    Iterable<User> getAllUsers();

    User updateUserProfile(Long id, String username, String email);

    void deleteUserAccount(Long id);

    String updateProfilePicture(Long id, MultipartFile file); // Inngyggt í Spring Boot til þess að hlaða upp skjölum

}
