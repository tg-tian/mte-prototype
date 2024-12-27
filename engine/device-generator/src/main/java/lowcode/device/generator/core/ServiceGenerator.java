package lowcode.device.generator.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.common.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceGenerator {
    public void generateServiceFile(String groupId, String deviceType, String devicePath, String jsonFilePath) {
        try {
            // 读取 JSON 文件内容
            String jsonContent = JsonUtils.readJson(jsonFilePath);

            // 将 JSON 内容解析为 JSONObject
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            String className = jsonNode.path("code").asText();
            JsonNode paramList = jsonNode.path("parameters");
            JsonNode commandList = jsonNode.path("commands");

            // 构建 Java 类内容
            StringBuilder classContent = new StringBuilder();
            String packageName = groupId+".generate.service";
            classContent.append("package ").append(packageName).append(";\n\n");

            classContent.append("import demo.lowcode.common.*;").append("\n");
            classContent.append("import demo.lowcode.common.connect.*;").append("\n");
            classContent.append("import ").append(groupId).append(".service.*;").append("\n");
            classContent.append("import java.io.*;").append("\n");
            classContent.append("import java.util.*;").append("\n\n");

            classContent.append("public class ").append(className).append(" extends ").append(deviceType).append("Service").append(" {\n\n");

            // 添加参数信息
            List<JsonNode> initialParams = new ArrayList<>();
            for (JsonNode param : paramList) {
                String paramName = param.path("code").asText();
                String paramType = param.path("code").asText();
                JsonNode paramValue = param.get("default");

                classContent.append("    private ").append(paramType).append(" ").append(paramName).append(" = ");
                if (paramValue.isTextual()) {
                    // 如果默认值是字符串
                    classContent.append("\"").append(paramValue).append("\"");
                } else if (paramValue.isNumber()) {
                    // 如果默认值是数字
                    classContent.append(paramValue);
                } else if (paramValue.isArray()) {
                    // 如果默认值是 JSON 数组
                    ArrayNode arrayNode = (ArrayNode) paramValue;
                    classContent.append("List.of(");
                    for (int j = 0; j < arrayNode.size(); j++) {
                        JsonNode item = arrayNode.get(j);
                        if (item.isTextual()) {
                            classContent.append("\"").append(item.asText()).append("\"");
                        } else {
                            classContent.append(item.asText());
                        }
                        if (j < arrayNode.size() - 1) {
                            classContent.append(", ");
                        }
                    }
                    classContent.append(")");
                } else {
                    // 对于其他情况，直接转换为字符串
                    classContent.append(paramValue);
                }
                classContent.append(";\n");

                boolean canInitial = param.path("canInitial").asBoolean();
                if (canInitial){
                    initialParams.add(param);
                }
            }
            classContent.append("\n");

            // 添加构造函数信息
            classContent.append("    public ").append(className).append("(");
            classContent.append(initialParams.stream().map(element-> {
                try {
                    String paramType = element.path("type").asText();
                    String paramCode = element.path("code").asText();
                    return paramType + " " + paramCode;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("生成服务java文件失败，对应json文件");
                }
            }).collect(Collectors.joining(",")));
            classContent.append("){").append("\n");
            for (JsonNode param : initialParams) {
                String paramCode = param.path("code").asText();
                classContent.append("        this.").append(paramCode).append(" = ").append(paramCode).append(";\n");
            }
            classContent.append("    }\n\n");

            // 添加方法定义
            for (JsonNode command: commandList) {
                String signature = command.path("signature").asText();
                String output = command.path("output").asText();
                String body = command.path("body").asText();

                // 添加方法定义
                classContent.append("    @Override").append("\n");
                classContent.append("    public ").append(output).append(" ").append(signature).append(" {\n");
                classContent.append("        ").append(body).append("\n");
                classContent.append("    }\n\n");
            }

            classContent.append("}");

            // 将生成的内容写入 Java 文件
            FileUtil.writeFile(devicePath+"\\generate\\service\\"+className + ".java", classContent.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("生成服务java文件失败，对应json文件："+jsonFilePath);
        }
    }
}
