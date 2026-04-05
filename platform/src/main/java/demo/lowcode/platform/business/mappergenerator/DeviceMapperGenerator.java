package demo.lowcode.platform.business.mappergenerator;

import demo.lowcode.platform.entity.Device;

public interface DeviceMapperGenerator {
    String getProvider();

    String generateContent(Device device, String className);

    default boolean supports(String provider) {
        return provider != null && provider.equalsIgnoreCase(getProvider());
    }
}
