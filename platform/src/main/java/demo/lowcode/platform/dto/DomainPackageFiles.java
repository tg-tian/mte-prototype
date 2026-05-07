package demo.lowcode.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainPackageFiles {
    private Integer version;
    private List<ExportedStoredFile> files = new ArrayList<>();
}
