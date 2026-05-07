package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewTemplate {
    private Long id;
    @Nullable
    private Long template_id;
    private String name;
    private String description;
    private String category;
    private String tags;
    private String domain;
    private String image_url;
    private String image_ref;
    private String describing_the_model;
    private String url;
}
