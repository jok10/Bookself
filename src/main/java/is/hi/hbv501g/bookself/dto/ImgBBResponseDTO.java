package is.hi.hbv501g.bookself.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties (ignoreUnknown = true) //Ignora allt sem ivið þurfum ekki. Þurfum bara linkinn.
public class ImgBBResponseDTO {
    private ImgBBData data;
}
