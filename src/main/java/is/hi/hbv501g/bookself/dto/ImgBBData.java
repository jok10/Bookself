package is.hi.hbv501g.bookself.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing the nested data object
 * from the imgBB API response. Extracts the hosted image URL and
 * ignores other metadata.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgBBData {
    private String url;
}
