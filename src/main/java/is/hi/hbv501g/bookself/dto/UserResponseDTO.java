package is.hi.hbv501g.bookself.dto;

import is.hi.hbv501g.bookself.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for safe, public view of a User entity. Excludes sensitive fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private User.Role role;

    public static UserResponseDTO responseFromEntity(User user){
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
