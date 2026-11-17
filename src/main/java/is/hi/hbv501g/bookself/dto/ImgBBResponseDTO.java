package is.hi.hbv501g.bookself.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 * The root JSON response that imgBB API returns when an image has been uploaded. Response from imgBB is nested and the data is mapped to ImgBBData DTO
 */
@Data
@JsonIgnoreProperties (ignoreUnknown = true) //Ignora allt sem ivið þurfum ekki. Þurfum bara linkinn.
public class ImgBBResponseDTO {
    private ImgBBData data;
}
