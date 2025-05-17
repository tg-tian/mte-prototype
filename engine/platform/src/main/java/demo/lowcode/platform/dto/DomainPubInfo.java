package demo.lowcode.platform.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class DomainPubInfo {
    private Long domainId;
    private String status;
    private String url;
    @Nullable
    private DomainTemInfo dslData;
}
