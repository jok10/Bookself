package is.hi.hbv501g.bookself.config;
import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;

@Configuration
public class DataInitializer {
    //Initializes data for testing purposes.
    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {
        return args -> {
            // Check if database is already populated to avoid duplicate entries on restart
            if (bookRepository.count() == 0) {
                Book book1 = new Book(null, "The Hobbit", "J.R.R. Tolkien", "9780547928227", "Description", "Fantasy", 0.0, new ArrayList<>());
                Book book2 = new Book(null, "The Lord of the Rings", "J.R.R. Tolkien", "9780618640157", "Description", "Fantasy", 0.0, new ArrayList<>());
                Book book3 = new Book(null, "Pride and Prejudice", "Jane Austen", "9780141439518", "Description",  "Romance", 0.0, new ArrayList<>());
                Book book4 = new Book(null, "To Kill a Mockingbird", "Harper Lee", "9780061120084", "Description", "Fiction", 0.0, new ArrayList<>());
                Book book5 = new Book(null, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565","Description",  "Classic", 0.0, new ArrayList<>());
                Book book6 = new Book(null, "The Shining", "Stephen King", "9780307743657", "Description", "Horror", 0.0, new ArrayList<>());
                Book book7 = new Book(null, "Misery", "Stephen King", "9781501143106", "Description","Thriller", 0.0, new ArrayList<>());
                Book book8 = new Book(null, "The King of Torts", "John Grisham", "9780345531988", "Description","Legal Thriller", 0.0, new ArrayList<>());
                Book book9 = new Book(null, "Demon Copperhead", "Barbara Kingsolver", "9780063251922", "Description","Fiction", 0.0, new ArrayList<>());
                Book book10 = new Book(null, "The Return of the King", "J.R.R. Tolkien", "9780544003415", "Description","Fantasy", 0.0, new ArrayList<>());
                Book book11 = new Book(null, "The King's Way", "Stephen King", "9781476727653", "Description", "Horror", 0.0, new ArrayList<>());


                bookRepository.save(book1);
                bookRepository.save(book2);
                bookRepository.save(book3);
                bookRepository.save(book4);
                bookRepository.save(book5);
                bookRepository.save(book6);
                bookRepository.save(book7);
                bookRepository.save(book8);
                bookRepository.save(book9);
                bookRepository.save(book10);
                bookRepository.save(book11);


                System.out.println("--- SAMPLE BOOKS SUCCESSFULLY LOADED INTO DATABASE ---");
            } else {
                System.out.println("--- DATABASE ALREADY CONTAINS DATA - SKIPPING INITIALIZATION ---");
            }
        };
    }

}
