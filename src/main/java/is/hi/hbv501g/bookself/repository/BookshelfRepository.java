package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {

    Optional<Bookshelf> findByUserId(Long userId);

    boolean existsByUser_IdAndBooks_Id(Long userId, Long bookId);

}