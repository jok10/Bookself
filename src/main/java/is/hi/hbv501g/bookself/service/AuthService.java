package is.hi.hbv501g.bookself.service;
import is.hi.hbv501g.bookself.model.User;

/**
 * Service interface for handling user registration and authentication.
 */
public interface AuthService {

    /**
     * Registers a new user with the USER role.
     *
     * @param username the users username
     * @param email the user's email address
     * @param password the plain-text password
     * @return the saved User entity
     */
    User registerUser(String username, String email, String password);

    /**
     * Registers a new admin user with the ADMIN role.
     *
     * @param username the new admin's username
     * @param email the new admin's email address
     * @param password the plain-text password
     * @return the saved User entity
     */
    User registerAdmin(String username, String email, String password);

    /**
     * Registers a new user with a specific role.
     *
     * @param username the desired username
     * @param email the user's email address
     * @param password the plain-text password
     * @param role the role to assign to the user
     * @return the saved User entity
     */

    User registerUserWithRole(String username, String email, String password, User.Role role);

    /**
     * Authenticates a user by verifying their username and password.
     *
     * @param username the username
     * @param password the password
     * @return the authenticated User entity
     */

    User loginUser(String username, String password);

}
