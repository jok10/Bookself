package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.BookService;
import is.hi.hbv501g.bookself.service.BookshelfService;
import is.hi.hbv501g.bookself.service.UserService;
import is.hi.hbv501g.bookself.dto.UserResponseDTO;
import is.hi.hbv501g.bookself.dto.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<UserResponseDTO> getProfile(HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in.");
        }
        User user = userService.getUserById(currentUserId);
        return ResponseEntity.ok(
                UserResponseDTO.fromFromEntity(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @RequestBody UpdateUserRequest request,
            HttpSession session) {

        Long currentUserId =
                (Long) session.getAttribute("USER_ID");

        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in.");
        }

        User updatedUser = userService.updateUserProfile(
                currentUserId,
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

//    public void deleteAccount(@PathVariable Long userId) {
//        userService.deleteUserAccount(userId);
//    }

    @GetMapping("/bookshelf")
    public ResponseEntity<Bookshelf> getUserBookshelf(HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("USER_ID");

        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in.");
        }
        Bookshelf bookshelf = bookshelfService.getUserBookshelf(currentUserId);
        if (bookshelf != null) {
            return ResponseEntity.ok(bookshelf);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/bookshelf/add")
    public ResponseEntity<Bookshelf> addBookToBookshelf(
            @RequestParam Long bookId, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in.");
        }
        Bookshelf updatedBookshelf = bookshelfService.addBookToShelf(currentUserId, bookId);
        return ResponseEntity.ok(updatedBookshelf);
    }

    @DeleteMapping("/bookshelf/remove")
    public ResponseEntity<Bookshelf> removeBookFromBookshelf(
            @RequestParam Long bookId, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("USER_ID");

        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in.");
        }
        Bookshelf updatedBookshelf = bookshelfService.removeBookFromShelf(currentUserId, bookId);
        //return getUserBookshelf(currentUserId);
        return ResponseEntity.ok(updatedBookshelf);
    }
}
