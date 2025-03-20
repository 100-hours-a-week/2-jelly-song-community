package io.github.jeli01.kakao_bootcamp_community.util.file;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStoreUtils {
    @Value("${image.file.dir}")
    private String fileDir;

    public FileStoreUtils(@Value("${image.file.dir}") String fileDir) {
        this.fileDir = fileDir;
    }

    public String storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);
        System.out.println("fullPath = " + fullPath);
        try {
            multipartFile.transferTo(new File(fullPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fullPath;
    }

    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private String getFullPath(String filename) {
        return fileDir + filename;
    }
}
