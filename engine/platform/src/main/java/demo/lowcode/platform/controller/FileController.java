package demo.lowcode.platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@CrossOrigin
public class FileController {

    // 设置文件存储的本地目录
    private static String UPLOADED_FOLDER = "D:/projects/ubml/mte-prototype/demo-frontend/src/assets/icon/";


    @GetMapping("/data")
    public String getJsonData(@RequestParam String filePath) throws IOException {
        String absolutePath = System.getProperty("user.dir")+filePath;
        return new String(Files.readAllBytes(Paths.get(absolutePath)));
    }

    @PostMapping("/file/save")
    public String saveJsonData(@RequestBody Map<String, String> requestBody) throws IOException {
        String filePath = requestBody.get("filePath");
        String absolutePath = System.getProperty("user.dir")+filePath;
        String content = requestBody.get("content");
        Files.write(Paths.get(absolutePath), content.getBytes());
        return "文件保存成功";
    }

    @PostMapping("/file/upload")
    public ResponseEntity<?> uploadFileData(@RequestParam("file") MultipartFile file) throws IOException{
        try {
            byte[] bytes = file.getBytes();
            // 确保目标文件夹存在
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            System.out.println(path.toString());
            Files.createDirectories(path.getParent());
            // 将文件写入指定位置
            Files.write(path, bytes);
            return ResponseEntity.ok(path.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件上传失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
