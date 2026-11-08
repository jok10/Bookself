package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.security.UserPrincipal;
import is.hi.hbv501g.bookself.service.BookService;
import is.hi.hbv501g.bookself.service.BookshelfService;
import is.hi.hbv501g.bookself.service.UserService;
import is.hi.hbv501g.bookself.dto.UserResponseDTO;
import is.hi.hbv501g.bookself.dto.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getId());
        return ResponseEntity.ok(
                UserResponseDTO.fromFromEntity(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        User updatedUser = userService.updateUserProfile(
                principal.getId(),
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
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UserPrincipal principal) {
        userService.deleteUserAccount(principal.getId());
        // No session to invalidate - the client just discards its token.
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/bookshelf")
    public ResponseEntity<Bookshelf> getUserBookshelf(@AuthenticationPrincipal UserPrincipal principal) {
        Bookshelf bookshelf = bookshelfService.getUserBookshelf(principal.getId());
        if (bookshelf != null) {
            return ResponseEntity.ok(bookshelf);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/bookshelf/add")
    public ResponseEntity<Bookshelf> addBookToBookshelf(
            @RequestParam Long bookId, @AuthenticationPrincipal UserPrincipal principal) {
        Bookshelf updatedBookshelf = bookshelfService.addBookToShelf(principal.getId(), bookId);
        return ResponseEntity.ok(updatedBookshelf);
    }

    @DeleteMapping("/bookshelf/remove")
    public ResponseEntity<Bookshelf> removeBookFromBookshelf(
            @RequestParam Long bookId, @AuthenticationPrincipal UserPrincipal principal) {
        Bookshelf updatedBookshelf = bookshelfService.removeBookFromShelf(principal.getId(), bookId);
        return ResponseEntity.ok(updatedBookshelf);
    }
}
