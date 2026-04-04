package demo.lowcode.platform.entity;

import java.util.Locale;

public enum DomainStatus {
    DEVELOPING("0"),
    PUBLISHED("1");

    private final String code;

    DomainStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DomainStatus fromValue(String value) {
        if (value == null) {
            return DEVELOPING;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return DEVELOPING;
        }
        if ("1".equals(normalized) || "published".equals(normalized)) {
            return PUBLISHED;
        }
        if ("0".equals(normalized) || "2".equals(normalized) || "testing".equals(normalized) || "editing".equals(normalized) || "developing".equals(normalized) || "development".equals(normalized)) {
            return DEVELOPING;
        }
        return DEVELOPING;
    }

    public static String normalizeCode(String value) {
        return fromValue(value).getCode();
    }

    public static boolean isDeveloping(String value) {
        return fromValue(value) == DEVELOPING;
    }
}
