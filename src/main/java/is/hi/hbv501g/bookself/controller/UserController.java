package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.BookService;
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

import java.util.List;

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

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getProfile(
            @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getUserByUsername(jwt.getSubject());

        return ResponseEntity.ok(
                UserResponseDTO.fromFromEntity(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @Valid @RequestBody UpdateUserRequest request, @AuthenticationPrincipal Jwt jwt) {
        User currentUser = userService.getUserByUsername(jwt.getSubject());

        User updatedUser = userService.updateUserProfile(
                currentUser.getId(),
                request.getUsername(),
                request.getEmail());

        return ResponseEntity.ok(
                UserResponseDTO.fromFromEntity(updatedUser));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = (List<User>) userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

//    public ResponseEntity<String> uploadProfilePic(Long userId, byte[] imageBytes) {
//    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        User currentUser = userService.getUserByUsername(jwt.getSubject());
        userService.deleteUserAccount(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookshelf")
    public ResponseEntity<Bookshelf> getUserBookshelf(@AuthenticationPrincipal Jwt jwt) {
        User currentUser = userService.getUserByUsername(jwt.getSubject());
        Bookshelf bookshelf = bookshelfService.getUserBookshelf(currentUser.getId());
        if (bookshelf != null) {
            return ResponseEntity.ok(bookshelf);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/bookshelf/add")
    public ResponseEntity<Bookshelf> addBookToBookshelf(
            @RequestParam Long bookId, @AuthenticationPrincipal Jwt jwt) {
        User currentUser = userService.getUserByUsername(jwt.getSubject());

        Bookshelf updatedBookshelf = bookshelfService.addBookToShelf(currentUser.getId(), bookId);
        return ResponseEntity.ok(updatedBookshelf);
    }

    @DeleteMapping("/bookshelf/remove")
    public ResponseEntity<Bookshelf> removeBookFromBookshelf(
            @RequestParam Long bookId, @AuthenticationPrincipal Jwt jwt) {
        User currentUser = userService.getUserByUsername(jwt.getSubject());

        Bookshelf updatedBookshelf = bookshelfService.removeBookFromShelf(currentUser.getId(), bookId);
        //return getUserBookshelf(currentUserId);
        return ResponseEntity.ok(updatedBookshelf);
    }
}
