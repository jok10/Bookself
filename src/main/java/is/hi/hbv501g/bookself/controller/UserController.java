package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.BookService;
import is.hi.hbv501g.bookself.service.BookshelfService;
import is.hi.hbv501g.bookself.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{userId}")
    public ResponseEntity<User> getProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    public ResponseEntity<String> uploadProfilePic(Long userId, byte[] imageBytes) {
//    }

//    public void deleteAccount(@PathVariable Long userId) {
//        userService.deleteUserAccount(userId);
//    }

    @GetMapping("/{userId}/bookshelf")
    public ResponseEntity<Bookshelf> getUserBookshelf(@PathVariable Long userId) {
        Bookshelf bookshelf = bookshelfService.getUserBookshelf(userId);
        if (bookshelf != null) {
            return ResponseEntity.ok(bookshelf);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping ("/{userId}/bookshelf/add")
    public ResponseEntity<Bookshelf> addBookToBookshelf(@PathVariable Long userId, @RequestParam Long bookId) {
        Bookshelf updatedBookshelf = bookshelfService.addBookToShelf(userId, bookId);
        return ResponseEntity.ok(updatedBookshelf);
    }

    @DeleteMapping("/{userId}/bookshelf/remove")
    public ResponseEntity<Bookshelf> removeBookFromBookshelf(@PathVariable Long userId, @RequestParam Long bookId) {
        Bookshelf updatedBookshelf = bookshelfService.removeBookFromShelf(userId, bookId);
        return getUserBookshelf(userId);
    }
}
