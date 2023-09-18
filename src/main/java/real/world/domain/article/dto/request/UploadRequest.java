package real.world.domain.article.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName(value = "article")
@NoArgsConstructor
public class UploadRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String body;

    @JsonProperty("tagList")
    private Set<String> tags;

}
