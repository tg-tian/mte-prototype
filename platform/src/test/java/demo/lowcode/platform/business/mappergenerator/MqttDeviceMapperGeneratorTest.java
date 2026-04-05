package demo.lowcode.platform.business.mappergenerator;

import demo.lowcode.platform.entity.Device;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MqttDeviceMapperGeneratorTest {

    @Test
    void shouldGenerateCoffeeMapperInHandwrittenStyle() throws IOException {
        MqttDeviceMapperGenerator generator = new MqttDeviceMapperGenerator();
        Device device = new Device();
        device.setProvider("mqtt");
        device.setDeviceId("DELONGHI-CF-2001");

        Map<String, Object> propertyMap = new LinkedHashMap<>();
        propertyMap.put("water_temperature", "waterTemperature");
        propertyMap.put("water_level", "waterLevel");
        propertyMap.put("status", "status");
        device.setPropertyMap(propertyMap);

        Map<String, Object> coffeeComplete = new LinkedHashMap<>();
        coffeeComplete.put("_to", "coffeeComplete");
        coffeeComplete.put("coffee_type", "coffee_type");
        coffeeComplete.put("duration", "duration");
        coffeeComplete.put("start_time", "start_time");
        Map<String, Object> eventMap = new LinkedHashMap<>();
        eventMap.put("coffeeComplete", coffeeComplete);
        device.setEventMap(eventMap);

        Map<String, String> actionMap = new LinkedHashMap<>();
        actionMap.put("makeCoffee", "const payload = {\n"
                + "  action: 'makeCoffee',\n"
                + "  args: {\n"
                + "    coffee_type: args.coffee_type\n"
                + "  }\n"
                + "};\n"
                + "this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));");
        device.setActionMap(actionMap);

        String content = generator.generateContent(device, "MqttCoffeeMakerMapper");

        Path outputPath = Path.of("C:/lowcode/device-entry-platform/src/adapters/mqtt/generated-coffee-mapper.ts");
        Files.writeString(outputPath, content);

        assertTrue(Files.exists(outputPath));
        assertTrue(content.contains("private isPlainObject(value: any): boolean"));
        assertTrue(content.contains("private hasNestedMapping(mapping: any): boolean"));
        assertTrue(content.contains("private processMapping(sourceData: any, mapping: any): Record<string, any>"));
        assertTrue(content.contains("mapEvents(rawEvent: any): Record<string, any>"));
        assertTrue(content.contains("makeCoffee(deviceId: string, args: any): void"));
        assertTrue(content.contains("'coffeeComplete': {") || content.contains("coffeeComplete: {"));
        assertTrue(content.contains("_to: 'coffeeComplete'"));
        assertTrue(content.contains("  propertyMap: Record<string, any> = {\n    water_temperature: 'waterTemperature',\n    water_level: 'waterLevel',\n    status: 'status'\n  };"));
        assertTrue(content.contains("if (this.isPlainObject(value) && this.hasNestedMapping(fieldMapping)) {"));
        assertTrue(content.contains("this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));"));
        assertTrue(content.endsWith("}\n"));
    }

    @Test
    void shouldGenerateAcMapperWithNestedMapAndActionStyle() throws IOException {
        MqttDeviceMapperGenerator generator = new MqttDeviceMapperGenerator();
        Device device = buildAcDevice();

        String content = generator.generateContent(device, "MqttACGeneratedMapper");
        Path outputPath = Path.of("C:/lowcode/mte-prototype/platform/src/main/resources/devicemapper/MqttACGeneratedMapper.ts");
        Files.writeString(outputPath, content);

        String handwritten = Files.readString(Path.of("C:/lowcode/mte-prototype/platform/src/main/resources/devicemapper/MqttACMapper.ts"));

        assertTrue(Files.exists(outputPath));
        assertTrue(content.contains("deviceModel = 'HAIER-AC-1001';"));
        assertTrue(content.contains("hvac_mode: {\n      _to: 'hvacMode',\n      _map: {\n        0: 'cool',\n        1: 'heat',\n        2: 'fan',\n        3: 'dry',\n        4: 'auto'\n      }\n    }"));
        assertTrue(content.contains("comp_status: {\n      _to: 'compressorStatus',\n      val: {\n        _to: 'status',\n        _map: {\n          0: 'off',\n          1: 'on',\n          2: 'defrosting'\n        }\n      }\n    }"));
        assertTrue(content.contains("if (this.isPlainObject(value) && this.hasNestedMapping(fieldMapping)) {\n        mappedData[targetKey] = this.processMapping(value, fieldMapping);"));
        assertTrue(content.contains("setMode(deviceId: string, args: any): void {\n    const modeMap: Record<string, number> = {"));
        assertTrue(content.contains("const payload = { action: 'setMode', args: { mode: modeVal } };"));
        assertTrue(content.contains("setTemperature(deviceId: string, args: any): void {\n    const payload = { action: 'setTemperature', args };"));
        assertEquals(normalizeTs(handwritten), normalizeTs(content));
    }

    private Device buildAcDevice() {
        Device device = new Device();
        device.setProvider("mqtt");
        device.setDeviceId("HAIER-AC-1001");

        Map<String, Object> propertyMap = new LinkedHashMap<>();
        propertyMap.put("current_temperature", "tempCurrent");
        propertyMap.put("temperature", "tempTarget");

        Map<String, Object> hvacMode = new LinkedHashMap<>();
        hvacMode.put("_to", "hvacMode");
        Map<String, Object> hvacModeMap = new LinkedHashMap<>();
        hvacModeMap.put("0", "cool");
        hvacModeMap.put("1", "heat");
        hvacModeMap.put("2", "fan");
        hvacModeMap.put("3", "dry");
        hvacModeMap.put("4", "auto");
        hvacMode.put("_map", hvacModeMap);
        propertyMap.put("hvac_mode", hvacMode);
        device.setPropertyMap(propertyMap);

        Map<String, Object> sysError = new LinkedHashMap<>();
        sysError.put("_to", "error");
        sysError.put("code", "code");
        sysError.put("msg", "message");

        Map<String, Object> filterWarn = new LinkedHashMap<>();
        filterWarn.put("_to", "filterAlert");
        filterWarn.put("op_hours", "hoursUsed");

        Map<String, Object> compStatus = new LinkedHashMap<>();
        compStatus.put("_to", "compressorStatus");
        Map<String, Object> compStatusVal = new LinkedHashMap<>();
        compStatusVal.put("_to", "status");
        Map<String, Object> compStatusValMap = new LinkedHashMap<>();
        compStatusValMap.put("0", "off");
        compStatusValMap.put("1", "on");
        compStatusValMap.put("2", "defrosting");
        compStatusVal.put("_map", compStatusValMap);
        compStatus.put("val", compStatusVal);

        Map<String, Object> eventMap = new LinkedHashMap<>();
        eventMap.put("sys_error", sysError);
        eventMap.put("filter_warn", filterWarn);
        eventMap.put("comp_status", compStatus);
        device.setEventMap(eventMap);

        Map<String, String> actionMap = new LinkedHashMap<>();
        actionMap.put("setMode", "const modeMap: Record<string, number> = {\n"
                + "  'cool': 0,\n"
                + "  'heat': 1,\n"
                + "  'fan': 2,\n"
                + "  'dry': 3,\n"
                + "  'auto': 4\n"
                + "};\n"
                + "const modeVal = modeMap[args.mode];\n"
                + "const payload = { action: 'setMode', args: { mode: modeVal } };\n"
                + "this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));");
        actionMap.put("setTemperature", "const payload = { action: 'setTemperature', args };\n"
                + "this.client.publish(`devices/${deviceId}/command`, JSON.stringify(payload));");
        device.setActionMap(actionMap);

        return device;
    }

    private String normalizeTs(String content) {
        return content.replace("MqttACMapper", "MqttACGeneratedMapper")
                .replace("metaModel :", "metaModel:")
                .replace("config: ProviderConfig , metaModel: BaseDeviceModel", "config: ProviderConfig, metaModel: BaseDeviceModel")
                .replaceAll("\r\n", "\n")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }
}
