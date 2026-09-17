package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Segir spring að þetta se "Data Access Component"
public interface UserRepository extends JpaRepository<User, Long> {
    //innbyggt er findById Save og ýmislegt annað
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username); //Til að athuga hvort að þetta usedrname sé í notkun
    boolean existsByEmail(String email); //Til að athuga hvort að þetta emeil sé í notkun.
}
