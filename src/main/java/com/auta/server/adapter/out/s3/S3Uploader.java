package com.auta.server.adapter.out.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.auta.server.application.port.out.s2.S3Port;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Profile({"local", "prod"})
public class S3Uploader implements S3Port {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Override
    public String upload(MultipartFile jsonFile) {
        try {
            String originalFilename = jsonFile.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(jsonFile.getContentType());
            metadata.setContentLength(jsonFile.getSize());

            amazonS3Client.putObject(
                    bucket,
                    fileName,
                    jsonFile.getInputStream(),
                    metadata
            );

            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }
    }

    @Override
    public void delete(String oldFigmaJsonUrl) {
        if (oldFigmaJsonUrl == null || oldFigmaJsonUrl.isBlank()) {
            return;
        }

        String key = extractKeyFromUrl(oldFigmaJsonUrl);

        if (amazonS3Client.doesObjectExist(bucket, key)) {
            amazonS3Client.deleteObject(bucket, key);
        }
    }

    private String extractKeyFromUrl(String url) {
        int bucketUrlLength = amazonS3Client.getUrl(bucket, "").toString().length();
        return url.substring(bucketUrlLength);
    }
}
