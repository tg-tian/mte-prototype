package demo.lowcode.common.util;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {
    // 创建文件
    public static void createFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()){
            // 获取父目录，并确保目录存在
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();  // 创建所有必要的目录
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 向文件中写
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

    // 删除文件夹
    public static void deleteDir(File dir){
        // 检查是否是目录
        if (dir.isDirectory()) {
            // 获取目录下的所有文件和子目录
            File[] files = dir.listFiles();
            if (files != null) { // 防止空指针异常
                for (File file : files) {
                    // 递归删除每个文件或子目录
                    deleteDir(file);
                }
            }
        }
        // 最后删除空目录或文件
        dir.delete();
    }

    public static void deleteDirFiles(File dir){
        // 检查是否是目录
        if (dir.isDirectory()) {
            // 获取目录下的所有文件和子目录
            File[] files = dir.listFiles();
            if (files != null) { // 防止空指针异常
                for (File file : files) {
                    // 递归删除每个文件或子目录
                    deleteDirFiles(file);
                }
            }
        }
    }

    // 拷贝文件
    public static void copyFile(File source, File destination) throws IOException {
        if (destination.isDirectory()) {
            // 构造目标文件的完整路径，包含源文件的文件名
            Path destinationPath = destination.toPath().resolve(source.getName());
            Path sourcePath = source.toPath();
            // 复制文件并覆盖已有文件
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new IllegalArgumentException("Destination is not a directory");
        }
    }

    // 读取jar包中的json文件（resource目录下）
    public static JSONObject readJarJson(String jarFilePath, String jsonFile) {
        JSONObject result = null;
        try {
            // 打开 JAR 文件
            JarFile jarFile = new JarFile(jarFilePath);

            // 获取 JAR 包内的 JSON 文件条目
            JarEntry jarEntry = jarFile.getJarEntry(jsonFile);

            if (jarEntry != null) {
                // 打开文件输入流
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                // 逐行读取文件内容
                String line;
                StringBuilder jsonContent = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line).append("\n");
                }
                reader.close();

                result = new JSONObject(String.valueOf(jsonContent));
            } else {
                throw new RuntimeException("jar包中不存在该json文件");
            }

            // 关闭 JAR 文件
            jarFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取json内容失败："+e.getMessage());
        }

        return result;
    }
}
