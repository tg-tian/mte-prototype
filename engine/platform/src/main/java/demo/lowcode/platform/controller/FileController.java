package demo.lowcode.platform.controller;

import demo.lowcode.common.CommonConfig;
import demo.lowcode.common.util.StringUtil;
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
    private static String UPLOADED_FOLDER = CommonConfig.getProjectPath()+"img/";

    @GetMapping("/data")
    public String getJsonData(@RequestParam String filePath) throws IOException {
        String absolutePath = CommonConfig.getWorkspacePath() +filePath;
        return new String(Files.readAllBytes(Paths.get(absolutePath)));
    }

    @PostMapping("/file/save")
    public String saveJsonData(@RequestBody Map<String, String> requestBody) throws IOException {
        String filePath = requestBody.get("filePath");
        String absolutePath = CommonConfig.getWorkspacePath()+filePath;
        String content = requestBody.get("content");
        Files.write(Paths.get(absolutePath), content.getBytes());
        return "文件保存成功";
    }

    // 上传设备类型图片
    @PostMapping("/file/upload")
    public ResponseEntity<?> uploadFileData(@RequestParam("deviceCode") String deviceCode, @RequestParam("file") MultipartFile file) throws IOException{
        try {
            byte[] bytes = file.getBytes();
            // 确保目标文件夹存在
            String originalName = file.getOriginalFilename();
            String fileName = StringUtil.capitalizeFirstLetter(deviceCode)+originalName.substring(originalName.lastIndexOf('.'));
            Path path = Paths.get(CommonConfig.getDefinitionPath()+ StringUtil.capitalizeFirstLetter(deviceCode)+"/"+fileName);
            System.out.println(path.toString());
            Files.createDirectories(path.getParent());
            // 将文件写入指定位置
            Files.write(path, bytes);
            return ResponseEntity.ok("/images/deviceType/"+StringUtil.capitalizeFirstLetter(deviceCode)+"/"+fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件上传失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
