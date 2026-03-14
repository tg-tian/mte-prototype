package demo.lowcode.platform.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
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
    private String describing_the_model;
    private String url;
}
