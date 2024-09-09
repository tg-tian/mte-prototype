package lowcode.device.generator.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static void writeFile(String filePath, String content) {
        try {
            // 创建File对象
            File file = new File(filePath);

            // 获取父目录，并确保目录存在
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();  // 创建所有必要的目录
            }

            // 使用 FileWriter 写入文件
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String lowercaseFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }
}
