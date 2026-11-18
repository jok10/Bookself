package is.hi.hbv501g.bookself.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = "reviews")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Segir að þetta sé autoincremented ids
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * Defines the authorization roles of users.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    public enum Role{
        USER, ADMIN
    }

    @Column
    private String profilePicURL;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    /**
     * Constructor that creates a new user with the specified account details and role.
     *
     * @param username the unique username
     * @param email the unique email address
     * @param password the hashed user password
     * @param role the security role assigned to the user
     */
    public User(String username, String email, String password, Role role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Checks whether the user has admin privileges.
     *
     * @return true if the user role is ADMIN, false otherwise
     */
    public boolean isAdmin(){
        return Role.ADMIN.equals(this.role);
    }

}
