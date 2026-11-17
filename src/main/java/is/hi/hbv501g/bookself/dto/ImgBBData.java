package is.hi.hbv501g.bookself.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * The nested data object from the imgBB API response. Extracts the hosted image URL while ignoring other metadata.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgBBData {
    private String link;
}
