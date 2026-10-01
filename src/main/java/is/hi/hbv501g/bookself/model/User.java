package is.hi.hbv501g.bookself.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity // Segir spring þetta sé klasi í SQL database.
@Table(name = "users") //Til að segja að hér eru users geymdir. Má ekki vera user því það er reserved heiti
@Data //Býr til getters, setters, toString() og ýmislegt annað automatically.
@NoArgsConstructor //Býr til no ARgs constructor
@AllArgsConstructor
public class User {
    @Id //Segir að þetta sér primary key í töflunni
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Segir að þetta sé autoincremented ids
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    public enum Role{
        USER, ADMIN
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public User(String username, String email, String password, Role role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isAdmin(){
        return Role.ADMIN.equals(this.role);
    }

}
