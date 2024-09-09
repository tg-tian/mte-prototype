package demo.lowcode.platform.controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@CrossOrigin
public class FileController {
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
}
