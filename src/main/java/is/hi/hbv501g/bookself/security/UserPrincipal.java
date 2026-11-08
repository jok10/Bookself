package is.hi.hbv501g.bookself.security;

import is.hi.hbv501g.bookself.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * Adapts our User entity to the UserDetails interface Spring Security
 * expects, without putting any Spring Security code inside the User
 * entity itself. The authenticated user is available in controllers as
 * this type via @AuthenticationPrincipal.
 */
public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public boolean isAdmin() {
        return user.isAdmin();
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        // "ROLE_" prefix is a Spring Security convention - hasRole("ADMIN")
        // looks for an authority literally named "ROLE_ADMIN".
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Already hashed - see AuthServiceImpl.
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
