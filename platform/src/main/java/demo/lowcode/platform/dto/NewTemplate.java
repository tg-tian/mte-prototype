package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewTemplate {
    private Long id;
    @Nullable
    private Long template_id;
    private String name;
    private String template_index;
    private String template_description;
    private String example_image_url;
    private String code_url;
    private String repository_url;
    private String file_source;
    private String submitter;
    private String license;
    private String code_file;
    private Map<String, Object> tags;
    private Date created_at;
    private Date updated_at;
}