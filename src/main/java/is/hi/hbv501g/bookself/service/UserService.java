package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing user accounts, profiles, and profile pictures.
 */
public interface UserService {

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the unique ID of the user
     * @return the User entity
     */
    User getUserById(Long id);

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return the User entity
     */
    User getUserByUsername(String username);

    /**
     * Retrieves all users in the system.
     *
     * @return an iterable collection of all User entities
     */
    Iterable<User> getAllUsers();

    /**
     * Updates an existing user's profile information (username and email).
     *
     * @param id the unique ID of the user
     * @param username the new username
     * @param email the new email address
     * @return the updated User entity
     */
    User updateUserProfile(Long id, String username, String email);

    /**
     * Deletes a user account by its unique ID.
     *
     * @param id the unique ID of the user to delete
     */
    void deleteUserAccount(Long id);

    /**
     * Updates a user's profile picture using an uploaded file.
     *
     * @param id the unique ID of the user
     * @param file the uploaded profile picture file
     * @return the URL or path to the saved profile picture
     */
    String updateProfilePicture(Long id, MultipartFile file);
}
