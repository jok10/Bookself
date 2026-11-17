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

    /**
     * Converta a User entity into a UserResponseDTO.
     *
     * @param user the User entity to convert
     * @return a new UserResponseDTO with safe public user details
     */
    public static UserResponseDTO fromEntity(User user){
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
