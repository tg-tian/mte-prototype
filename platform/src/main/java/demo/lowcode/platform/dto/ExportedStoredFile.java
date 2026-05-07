package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportedStoredFile {
    private String ref;
    private Long sourceId;
    private String fileName;
    private String originalName;
    private String contentType;
    private Long fileSize;
    private String bizType;
    private Long bizId;
    private String blobPath;
    private String sha256;
}
