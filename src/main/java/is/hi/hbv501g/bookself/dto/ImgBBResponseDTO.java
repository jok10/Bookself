package is.hi.hbv501g.bookself.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 * Data Transfer Object (DTO) representing the root JSON response
 * returned by the imgBB API after a successful image upload.
 * Maps the nested response payload to the ImgBBData DTO.
 */
@Data
@JsonIgnoreProperties (ignoreUnknown = true) //Ignora allt sem ivið þurfum ekki. Þurfum bara linkinn.
public class ImgBBResponseDTO {
    private ImgBBData data;
}
