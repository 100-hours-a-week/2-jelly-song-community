package io.github.jeli01.kakao_bootcamp_community.cloud.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileUtils {
    private final AmazonS3Client amazonS3Client;
    
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            amazonS3Client.putObject(bucket, storeFileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new IllegalArgumentException("S3 업로드 실패", e);
        }

        return amazonS3Client.getUrl(bucket, storeFileName).toString();
    }

    public boolean deleteFile(String fileName) {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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
}
