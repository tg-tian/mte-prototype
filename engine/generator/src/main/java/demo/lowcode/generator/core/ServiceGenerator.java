package demo.lowcode.generator.core;

import demo.lowcode.generator.util.FileUtil;
import demo.lowcode.generator.util.JsonUtil;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceGenerator {
    public void generateServiceFile(String groupId, String deviceType, String devicePath, String jsonFilePath) {
        try {
            // 读取 JSON 文件内容
            String jsonContent = JsonUtil.readJson(jsonFilePath);

            // 将 JSON 内容解析为 JSONObject
            JSONObject jsonObject = new JSONObject(jsonContent);
            String className = jsonObject.getString("code");
            JSONArray paramList = jsonObject.getJSONArray("parameters");
            JSONArray commandList = jsonObject.getJSONArray("commands");

            // 构建 Java 类内容
            StringBuilder classContent = new StringBuilder();
            String packageName = groupId+".generate.service";
            classContent.append("package ").append(packageName).append(";\n\n");

            classContent.append("import demo.lowcode.common.*;").append("\n");
            classContent.append("import ").append(groupId).append(".service.*;").append("\n");
            classContent.append("import java.util.*;").append("\n\n");

            classContent.append("public class ").append(className).append(" extends ").append(deviceType).append("Service").append(" {\n\n");

            // 添加参数信息
            List<JSONObject> initialParams = new ArrayList<>();
            for (int i = 0; i < paramList.length(); i++) {
                JSONObject param = paramList.getJSONObject(i);

                String paramName = param.getString("code");
                String paramType = param.getString("type");
                Object paramValue = param.get("default");

                classContent.append("    private ").append(paramType).append(" ").append(paramName).append(" = ");
                if (paramValue instanceof String) {
                    // 如果默认值是字符串
                    classContent.append("\"").append(paramValue).append("\"");
                } else if (paramValue instanceof Number) {
                    // 如果默认值是数字
                    classContent.append(paramValue);
                } else if (paramValue instanceof JSONArray jsonArray) {
                    // 如果默认值是 JSON 数组
                    classContent.append("List.of(");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        Object item = jsonArray.get(j);
                        if (item instanceof String) {
                            classContent.append("\"").append(item).append("\"");
                        } else {
                            classContent.append(item);
                        }
                        if (j < jsonArray.length() - 1) {
                            classContent.append(", ");
                        }
                    }
                    classContent.append(")");
                } else {
                    // 对于其他情况，直接转换为字符串
                    classContent.append(paramValue);
                }
                classContent.append(";\n");

                boolean canInitial = param.getBoolean("canInitial");
                if (canInitial){
                    initialParams.add(param);
                }
            }
            classContent.append("\n");

            // 添加构造函数信息
            classContent.append("    public ").append(className).append("(");
            classContent.append(initialParams.stream().map(element-> {
                try {
                    return element.getString("type")+" "+element.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new RuntimeException("生成服务java文件失败，对应json文件："+jsonFilePath);
                }
            }).collect(Collectors.joining(",")));
            classContent.append("){").append("\n");
            for (JSONObject param : initialParams) {
                classContent.append("        this.").append(param.getString("code")).append(" = ").append(param.getString("code")).append(";\n");
            }
            classContent.append("    }\n\n");

            // 添加方法定义
            for (int i = 0; i < commandList.length(); i++) {
                JSONObject command = commandList.getJSONObject(i);

                String signature = command.getString("signature");
                String output = command.getString("output");
                String body = command.getString("body");

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
