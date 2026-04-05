package demo.lowcode.platform.business.mappergenerator;

import demo.lowcode.platform.entity.Device;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MqttDeviceMapperGenerator implements DeviceMapperGenerator {
    @Override
    public String getProvider() {
        return "mqtt";
    }

    @Override
    public String generateContent(Device device, String className) {
        String propertyMapStr = toTsLiteral(device.getPropertyMap(), 1, true);
        String eventMapStr = toTsLiteral(device.getEventMap(), 1, true);
        String actionMethodsStr = toActionMethods(device.getActionMap());

        StringBuilder builder = new StringBuilder();
        builder.append("import mqtt from 'mqtt';\n");
        builder.append("import { DeviceMapper } from '../device-mapper';\n");
        builder.append("import { ProviderConfig } from '../../domain/provider-config';\n");
        builder.append("import { BaseDeviceModel } from '../../domain/model';\n\n");
        builder.append("export class ").append(className).append(" implements DeviceMapper {\n");
        builder.append("  metaModel: BaseDeviceModel;\n");
        builder.append("  deviceModel = '").append(escapeSingleQuote(device.getDeviceId())).append("';\n");
        builder.append("  provider = '").append(escapeSingleQuote(device.getProvider())).append("';\n");
        builder.append("  private client: mqtt.MqttClient;\n");
        builder.append("  private cfg: ProviderConfig;\n\n");
        builder.append("  propertyMap: Record<string, any> = ").append(propertyMapStr).append(";\n\n");
        builder.append("  eventMap: Record<string, any> = ").append(eventMapStr).append(";\n\n");
        builder.append("  constructor(config: ProviderConfig, metaModel: BaseDeviceModel) {\n");
        builder.append("    this.cfg = config;\n");
        builder.append("    this.metaModel = metaModel;\n");
        builder.append("    this.client = mqtt.connect(this.cfg.communication.baseUrl);\n");
        builder.append("  }\n\n");
        builder.append("  private isPlainObject(value: any): boolean {\n");
        builder.append("    return value !== null && typeof value === 'object' && !Array.isArray(value);\n");
        builder.append("  }\n\n");
        builder.append("  private hasNestedMapping(mapping: any): boolean {\n");
        builder.append("    return Object.keys(mapping).some(key => !key.startsWith('_'));\n");
        builder.append("  }\n\n");
        builder.append("  private processMapping(sourceData: any, mapping: any): Record<string, any> {\n");
        builder.append("    const mappedData: Record<string, any> = {};\n");
        builder.append("    if (!this.isPlainObject(sourceData) || !this.isPlainObject(mapping)) {\n");
        builder.append("      return mappedData;\n");
        builder.append("    }\n");
        builder.append("    for (const [key, value] of Object.entries(sourceData)) {\n");
        builder.append("      const fieldMapping = mapping[key];\n");
        builder.append("      if (!fieldMapping) {\n");
        builder.append("        mappedData[key] = value;\n");
        builder.append("        continue;\n");
        builder.append("      }\n");
        builder.append("      if (typeof fieldMapping === 'string') {\n");
        builder.append("        mappedData[fieldMapping] = value;\n");
        builder.append("        continue;\n");
        builder.append("      }\n");
        builder.append("      if (!this.isPlainObject(fieldMapping)) {\n");
        builder.append("        mappedData[key] = value;\n");
        builder.append("        continue;\n");
        builder.append("      }\n");
        builder.append("      const targetKey = fieldMapping._to || key;\n");
        builder.append("      if (fieldMapping._map) {\n");
        builder.append("        mappedData[targetKey] = fieldMapping._map[value as any] ?? value;\n");
        builder.append("        continue;\n");
        builder.append("      }\n");
        builder.append("      if (this.isPlainObject(value) && this.hasNestedMapping(fieldMapping)) {\n");
        builder.append("        mappedData[targetKey] = this.processMapping(value, fieldMapping);\n");
        builder.append("      } else {\n");
        builder.append("        mappedData[targetKey] = value;\n");
        builder.append("      }\n");
        builder.append("    }\n");
        builder.append("    return mappedData;\n");
        builder.append("  }\n\n");
        builder.append("  mapProperties(rawProps: any): Record<string, any> {\n");
        builder.append("    return this.processMapping(rawProps, this.propertyMap);\n");
        builder.append("  }\n\n");
        builder.append("  mapEvents(rawEvent: any): Record<string, any> {\n");
        builder.append("    const result: Record<string, any> = {};\n");
        builder.append("    for (const [key, value] of Object.entries(rawEvent)) {\n");
        builder.append("      if (this.eventMap[key]) {\n");
        builder.append("        const mapping = this.eventMap[key];\n");
        builder.append("        const targetEventName = mapping._to || key;\n");
        builder.append("        result[targetEventName] = this.processMapping(value, mapping);\n");
        builder.append("      }\n");
        builder.append("    }\n");
        builder.append("    return result;\n");
        builder.append("  }");

        if (!actionMethodsStr.isBlank()) {
            builder.append("\n\n");
            builder.append(actionMethodsStr);
        }

        builder.append("\n}\n");
        return builder.toString();
    }

    private String toActionMethods(Map<String, String> actionMap) {
        if (actionMap == null || actionMap.isEmpty()) {
            return "";
        }
        return actionMap.entrySet().stream()
                .map(this::toSingleActionMethod)
                .collect(Collectors.joining("\n\n"));
    }

    private String toSingleActionMethod(Map.Entry<String, String> entry) {
        String methodName = sanitizeMethodName(entry.getKey());
        String impl = entry.getValue() == null ? "" : normalizeActionBody(entry.getValue());
        String body = impl.isBlank()
                ? "    // TODO: implement action: " + entry.getKey()
                : indentBody(impl, 2);

        return "  " + methodName + "(deviceId: string, args: any): void {\n"
                + body + "\n"
                + "  }";
    }

    private String sanitizeMethodName(String name) {
        if (name == null || name.isBlank()) {
            return "unnamedAction";
        }
        String sanitized = name.replaceAll("[^a-zA-Z0-9_$]", "");
        if (sanitized.isEmpty()) {
            return "unnamedAction";
        }
        if (!Character.isJavaIdentifierStart(sanitized.charAt(0))) {
            sanitized = "action" + sanitized;
        }
        return sanitized;
    }

    private String toTsLiteral(Object value, int indentLevel, boolean trailingComma) {
        if (value == null) {
            return "{}";
        }
        if (value instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return "{}";
            }
            String indent = indent(indentLevel);
            String childIndent = indent(indentLevel + 1);
            String entries = map.entrySet().stream()
                    .map(entry -> childIndent
                            + formatKey(entry.getKey())
                            + ": "
                            + toTsLiteral(entry.getValue(), indentLevel + 1, true))
                    .collect(Collectors.joining(",\n"));
            String closing = trailingComma ? "\n" + indent + "}" : "\n" + indent + "}";
            return "{\n" + entries + closing;
        }
        if (value instanceof List<?> list) {
            if (list.isEmpty()) {
                return "[]";
            }
            return "[" + list.stream()
                    .map(item -> toTsLiteral(item, indentLevel, false))
                    .collect(Collectors.joining(", ")) + "]";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        return "'" + escapeSingleQuote(String.valueOf(value)) + "'";
    }

    private String formatKey(Object key) {
        String text = String.valueOf(key);
        if (text.matches("-?\\d+(\\.\\d+)?")) {
            return text;
        }
        if (text.matches("[A-Za-z_$][A-Za-z0-9_$]*")) {
            return text;
        }
        return "'" + escapeSingleQuote(text) + "'";
    }

    private String normalizeActionBody(String body) {
        List<String> lines = body.lines().collect(Collectors.toList());
        int minIndent = lines.stream()
                .filter(line -> !line.isBlank())
                .mapToInt(this::leadingWhitespaceCount)
                .min()
                .orElse(0);

        return lines.stream()
                .map(line -> {
                    if (line.isBlank()) {
                        return "";
                    }
                    int remove = Math.min(minIndent, leadingWhitespaceCount(line));
                    return line.substring(remove).replaceAll("\\s+$", "");
                })
                .collect(Collectors.joining("\n"))
                .trim();
    }

    private int leadingWhitespaceCount(String line) {
        int count = 0;
        while (count < line.length() && Character.isWhitespace(line.charAt(count))) {
            count++;
        }
        return count;
    }

    private String indentBody(String body, int indentLevel) {
        String indent = indent(indentLevel);
        return body.lines()
                .map(line -> line.isBlank() ? "" : indent + line)
                .collect(Collectors.joining("\n"));
    }

    private String indent(int level) {
        return "  ".repeat(Math.max(0, level));
    }

    private String escapeSingleQuote(String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
