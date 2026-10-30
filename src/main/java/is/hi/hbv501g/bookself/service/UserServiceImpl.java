package is.hi.hbv501g.bookself.service;


import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //Segir spring að búa til og halda utan um þennan klasa
public class UserServiceImpl implements UserService { //lofar að implementa allt sem að er í userService
    private final UserRepository userRepository; //unchangeable

    @Autowired
    // segir spring að finna UserRepo og setja það inn í þennan constructor strax í byrjun
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    ;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserProfile(Long id, String username, String email) {
        User user = getUserById(id);

        userRepository.findByUsername(username)
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new RuntimeException("Username already exists, please choose another one.");
                    }
                });

        userRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new RuntimeException("There is already an account with this e-mail.");
                    }
                });

        user.setUsername(username);
        user.setEmail(email);
        return userRepository.save(user);
    }

    ;

    @Override
    public void deleteUserAccount(Long id) {
        userRepository.deleteById(id);
    }

    /* Implement later
    String updateProfilePicture(Long id, byte[] imageBytes);
     */
}
