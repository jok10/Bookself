package is.hi.hbv501g.bookself.service;


import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.BookshelfRepository;
import is.hi.hbv501g.bookself.repository.UserRepository;
import is.hi.hbv501g.bookself.utilities.UsageStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import is.hi.hbv501g.bookself.dto.ImgBBResponseDTO;
import is.hi.hbv501g.bookself.dto.ImgBBData;


@Service //Segir spring að búa til og halda utan um þennan klasa
public class UserServiceImpl implements UserService { //lofar að implementa allt sem að er í userService
    private final UserRepository userRepository; //unchangeable
    private final BookshelfRepository bookshelfRepository;

    @Autowired
    // segir spring að finna UserRepo og setja það inn í þennan constructor strax í byrjun
    public UserServiceImpl(UserRepository userRepository, BookshelfRepository bookshelfRepository) {
        this.userRepository = userRepository;
        this.bookshelfRepository = bookshelfRepository;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with username: " + username));
    }

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
    @Transactional
    public void deleteUserAccount(Long id) {
        // The bookshelf points at the user, so it has to be deleted first or the database blocks the delete.
        bookshelfRepository.findByUserId(id).ifPresent(bookshelfRepository::delete);
        userRepository.deleteById(id);
        UsageStatistics.getInstance().decrementTotalUsers();
    }

    //When writing this code, AI was used to understand how things work
    @Override
    public String updateProfilePicture(Long id, MultipartFile file){
        User user = getUserById(id);

        String imgbbApiKey = "72194c793c4de582744e1bbe61bfe6b4";

        //Headers to send to imgBB.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // Wrap the content file into the request body. The actual image data.
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());
        //Wrap the headers and the body into a HTTP request to send to imBB.
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        //Built into Spring and used to send requests to other web services.
        RestTemplate restTemplate = new RestTemplate();
        //ImgBB endpoint and our API. This is the target endpoint.
        String uploadUrl = "https://api.imgbb.com/1/upload?key=" + imgbbApiKey;
        //Send a HTTP POST to imgBB. ImgBB sends a bunch of info back but we just want the link. Response is sent to the DTO where the link is extracted.
        ResponseEntity<ImgBBResponseDTO> response = restTemplate.postForEntity(
                uploadUrl,
                requestEntity,
                ImgBBResponseDTO.class
        );

        if (response.getBody() == null || response.getBody().getData() == null) {
            throw new RuntimeException("Failed to upload profile picture to ImgBB.");
        }
        String imageUrl = response.getBody().getData().getLink();

        user.setProfilePicURL(imageUrl);
        userRepository.save(user);

        return imageUrl;


    }

}
