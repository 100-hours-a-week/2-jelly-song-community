package io.github.jeli01.kakao_bootcamp_community.cloud.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtils {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String storeFile(MultipartFile multipartFile) {
        validateExistsMultipartFile(multipartFile);

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        try {
            ObjectMetadata metadata = createS3MetaData(multipartFile);
            amazonS3Client.putObject(bucket, storeFileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new IllegalArgumentException("S3 업로드 실패", e);
        }

        return amazonS3Client.getUrl(bucket, storeFileName).toString();
    }

    private void validateExistsMultipartFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }
    }

    private static ObjectMetadata createS3MetaData(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
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

    public void deleteFile(String fileName) {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new IllegalArgumentException("S3 파일 삭제 실패", e);
        }
    }

}
