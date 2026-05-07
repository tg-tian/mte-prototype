package demo.lowcode.platform.dto;

import lombok.Data;

@Data
public class DomainPackageManifest {
    private Integer version;
    private String domainCode;
    private String exportedAt;
    private Boolean includesDomain;
    private Boolean includesFiles;
}
