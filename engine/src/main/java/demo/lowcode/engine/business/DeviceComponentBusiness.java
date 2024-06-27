package demo.lowcode.engine.business;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class DeviceComponentBusiness {
    @Value("${definitionPath}")
    private String definitionPath;

    public void loadDevice(String devicePath) throws IOException {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(input);
        definitionPath = properties.getProperty("definitionPath");
        File file = new File(devicePath+devicePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        String deviceType = rootNode.path("type").asText();

        // 读取commands数组
        JsonNode commandsNode = rootNode.path("commands");
        List<String> commands = new ArrayList<>();
        if (commandsNode.isArray()) {
            for (JsonNode command : commandsNode) {
                commands.add(command.path("name").asText());
            }
        }

        // 读取events数组
        JsonNode eventsNode = rootNode.path("events");
        List<String> events = new ArrayList<>();
        if (eventsNode.isArray()) {
            for (JsonNode event : eventsNode) {
                events.add(event.asText());
            }
        }

//        generateDeviceProject(deviceType, commands, events);
    }
}
