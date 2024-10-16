package lowcode.device.generator.core;

import demo.lowcode.common.util.JsonUtils;
import demo.lowcode.common.util.FileUtil;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class EventGenerator {
    public void generateEventFile(String groupId, String devicePath, JSONObject jsonObject) {
        try {
//            // 读取 JSON 文件内容
//            String jsonContent = JsonUtils.readJson(jsonFilePath);
//
//            // 将 JSON 内容解析为 JSONObject
//            JSONObject jsonObject = new JSONObject(jsonContent);

            // 获取类名和事件列表
            String className = jsonObject.getString("eventPath").replace(".java", "");
            JSONArray eventList = jsonObject.getJSONArray("eventList");

            // 构建 Java 类内容
            StringBuilder classContent = new StringBuilder();
            String packageName = groupId+".generate.event";
            classContent.append("package ").append(packageName).append(";\n\n");

            classContent.append("import lombok.*;").append("\n");
            classContent.append("import java.util.*;").append("\n");
            classContent.append("import demo.lowcode.common.*;").append("\n");
            classContent.append("import ").append(groupId).append(".event.*;").append("\n\n");

            classContent.append("@AllArgsConstructor").append("\n");
            classContent.append("public class ").append(className).append(" {\n\n");

            for (int i = 0; i < eventList.length(); i++) {
                JSONObject event = eventList.getJSONObject(i);

                String signature = event.getString("signature");
                String body = event.getString("body");

                // 添加方法定义
                classContent.append("    public void ").append(signature).append(" {\n");
                classContent.append("        ").append(body).append("\n");
                classContent.append("    }\n\n");
            }

            classContent.append("}");

            // 将生成的内容写入 Java 文件
            FileUtil.writeFile(devicePath+"java/"+className + ".java", classContent.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("生成事件java文件失败");
        }
    }
}
