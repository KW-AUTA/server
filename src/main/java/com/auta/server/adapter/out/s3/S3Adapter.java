package com.auta.server.adapter.out.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.auta.server.application.port.out.s3.S3Port;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Profile({"local", "prod"})
public class S3Adapter implements S3Port {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile jsonFile) {
        try {
            String originalFilename = jsonFile.getOriginalFilename();
            String extension = "";

            if (originalFilename.contains(".")) {
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
    public String upload(String staticUrl) {
        try {
            if (!staticUrl.startsWith("http")) {
                String baseUrl = "http://localhost:8000";  // 여기에 실제 정적 리소스 서버 도메인 입력
                staticUrl = baseUrl + staticUrl;
            }
            // 1. static URL로부터 파일 다운로드
            URL url = new URL(staticUrl);
            URLConnection connection = url.openConnection();

            String extension = "";
            String contentType = connection.getContentType();
            long contentLength = connection.getContentLengthLong();

            // 간단한 확장자 유추 (필요 시 더 정확한 처리 가능)
            if (contentType != null && contentType.contains("/")) {
                extension = "." + contentType.split("/")[1];
            }

            String fileName = UUID.randomUUID() + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(contentLength);

            // 2. S3에 업로드
            amazonS3Client.putObject(
                    bucket,
                    fileName,
                    connection.getInputStream(),
                    metadata
            );

            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("Static URL로부터 S3 업로드 중 오류 발생", e);
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
