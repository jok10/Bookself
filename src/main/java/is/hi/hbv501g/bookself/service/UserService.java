package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.User;

public interface UserService {
    User getUserById(Long id);

    User updateUserProfile(Long id, String username, String email);

    String updateProfilePicture(Long id, byte[] imageBytes);

    void deleteUserAccount(Long id);
}
