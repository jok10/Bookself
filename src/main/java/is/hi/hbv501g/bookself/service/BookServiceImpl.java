package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.dto.BookRequest;
import is.hi.hbv501g.bookself.dto.BookResponseDTO;
import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAll() {
        return bookRepository.findAll().stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookResponseDTO> findById(Long id) {
        return bookRepository.findById(id).map(BookResponseDTO::fromEntity);
    }

    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setYear(request.getYear());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setGenre(request.getGenre());

        Book saved = bookRepository.save(book);
        return BookResponseDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public Optional<BookResponseDTO> updateBook(Long id, BookRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isEmpty()){
            return Optional.empty();
        }
        Book existing = optionalBook.get();
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            existing.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null && !request.getAuthor().isBlank()) {
            existing.setAuthor(request.getAuthor());
        }
        if (request.getYear() > 0) {
            existing.setYear(request.getYear());
        }
        if (request.getIsbn() != null && !request.getIsbn().isBlank()) {
            existing.setIsbn(request.getIsbn());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            existing.setDescription(request.getDescription());
        }
        if (request.getGenre() != null && !request.getGenre().isBlank()) {
            existing.setGenre(request.getGenre());
        }

        Book updated = bookRepository.save(existing);
        return Optional.of(BookResponseDTO.fromEntity(updated));
    }
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchBooks(String query){
        if (query == null || query.trim().isEmpty()){
            return Collections.emptyList();
        }
        return bookRepository.findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query.trim(), query.trim()).stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getBooksByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return bookRepository.findAllByGenreIgnoreCase(genre.trim()).stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());
    }
}
