package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateExportInfo {
    private Long template_id;
    private String name;
    private String template_index;
    private String template_description;
    private String tags;
    private String example_image_url;
    private String imageRef;
    private String code_url;
    private String repository_url;
    private String file_source;
    private String submitter;
    private String license;
    private String code_file;
}
