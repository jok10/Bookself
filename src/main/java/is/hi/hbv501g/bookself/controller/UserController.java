package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.BookshelfDTO;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.BookshelfService;
import is.hi.hbv501g.bookself.service.UserService;
import is.hi.hbv501g.bookself.dto.UserResponseDTO;
import is.hi.hbv501g.bookself.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller responsible for managing user profiles, account settings,
 * profile pictures, administrative user listings, and personal bookshelf operations
 * under the "/api/users" path.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final BookshelfService bookshelfService;

    @Autowired
    public UserController(UserService userService, BookshelfService bookshelfService) {
        this.userService = userService;
        this.bookshelfService = bookshelfService;
    }

    //Profile Management

    /**
     * Retrieves the profile details of the currently authenticated user.
     *
     * @param jwt the security token of the logged-in user
     * @return a ResponseEntity containing the user's UserResponseDTO.
     */

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);
        return ResponseEntity.ok(
                UserResponseDTO.fromEntity(currentUser));
    }

    /**
     * Updates the profile information (username and/or email) of the authenticated user.
     *
     * @param request the update request containing new profile details
     * @param jwt     the security token of the logged-in user
     * @return a ResponseEntity containing the updated UserResponseDTO
     */

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @Valid @RequestBody UpdateUserRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);

        User updatedUser = userService.updateUserProfile(
                currentUser.getId(),
                request.getUsername(),
                request.getEmail());

        return ResponseEntity.ok(
                UserResponseDTO.fromEntity(updatedUser));
    }

    /**
     * Uploads and updates the profile picture for the authenticated user.
     *
     * @param file the image file multipart upload
     * @param jwt  the security token of the logged-in user
     * @return a ResponseEntity containing the URL path of the uploaded image
     */
    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        String imageUrl = userService.updateProfilePicture(userId, file);
        return ResponseEntity.ok(imageUrl);
    }

    /**
     * Deletes the user account and associated data for the currently authenticated user.
     *
     * @param jwt the security token of the logged-in user
     * @return a 204 No Content response on successful account deletion
     */

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);
        userService.deleteUserAccount(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    //User administration

    /**
     /**
     * Retrieves a list of all users registered in the system.
     *
     * @return a ResponseEntity containing a list of UserResponseDTO objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users =
                userService.getAllUsers()
                        .stream()
                        .map(UserResponseDTO::fromEntity)
                        .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves the bookshelf that belongs to the currently authenticated user.
     *
     * @param jwt the security token of the logged-in user
     * @return a ResponseEntity containing a BookshelfDTO representing the user's bookshelf
     */
    @GetMapping("/bookshelf")
    public ResponseEntity<BookshelfDTO> getUserBookshelf(@AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);
        Bookshelf bookshelf = bookshelfService.getUserBookshelf(currentUser.getId());
        if (bookshelf != null) {
            return ResponseEntity.ok(BookshelfDTO.fromEntity(bookshelf));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    //Bookshelf Management
    /**
     * Retrieves the bookshelf that belongs to the currently authenticated user.
     *
     * @param jwt the security token of the logged-in user
     * @return a ResponseEntity containing the updated BookshelfDTO
     */

    @PostMapping("/bookshelf/add")
    public ResponseEntity<BookshelfDTO> addBookToBookshelf(
            @RequestParam Long bookId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);

        Bookshelf updatedBookshelf = bookshelfService.addBookToShelf(currentUser.getId(), bookId);
        return ResponseEntity.ok(BookshelfDTO.fromEntity(updatedBookshelf));
    }

    /**
     * Adds a specific book to the authenticated user's bookshelf.
     *
     * @param bookId the unique id of the book to add
     * @param jwt    the security token of the logged-in user
     * @return a ResponseEntity containing the updated BookshelfDTO
     */

    @DeleteMapping("/bookshelf/remove")
    public ResponseEntity<BookshelfDTO> removeBookFromBookshelf(
            @RequestParam Long bookId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);

        Bookshelf updatedBookshelf = bookshelfService.removeBookFromShelf(currentUser.getId(), bookId);
        return ResponseEntity.ok(BookshelfDTO.fromEntity(updatedBookshelf));
    }
}
