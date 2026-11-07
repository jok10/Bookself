package is.hi.hbv501g.bookself.security;

import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public AppUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = users.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username));
        //Við getum ekki notað (Spring Security) User beint því við erum með entity með sama nafni
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}