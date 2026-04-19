package demo.lowcode.platform.service;

import demo.lowcode.platform.entity.StoredFile;
import demo.lowcode.platform.mapper.StoredFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private final StoredFileMapper storedFileMapper;

    @Autowired
    public FileService(StoredFileMapper storedFileMapper) {
        this.storedFileMapper = storedFileMapper;
    }

    public String saveImage(MultipartFile file) {
        try {
            StoredFile storedFile = new StoredFile();
            storedFile.setFileName(UUID.randomUUID() + getFileExtension(Objects.requireNonNull(file.getOriginalFilename())));
            storedFile.setOriginalName(file.getOriginalFilename());
            storedFile.setContentType(resolveContentType(file.getContentType()));
            storedFile.setFileSize(file.getSize());
            storedFile.setFileData(file.getBytes());
            storedFile.setBizType("image");
            Date now = new Date();
            storedFile.setCreatedAt(now);
            storedFile.setUpdatedAt(now);
            storedFileMapper.insert(storedFile);
            return "/file/image/" + storedFile.getId();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public StoredFile getFileById(Long id) {
        return storedFileMapper.selectById(id);
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return index >= 0 ? filename.substring(index) : "";
    }

    private String resolveContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return contentType;
    }
}
