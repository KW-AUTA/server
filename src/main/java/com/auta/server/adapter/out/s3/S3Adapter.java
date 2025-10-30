package com.auta.server.adapter.out.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.auta.server.application.port.out.s3.S3Port;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"local", "prod"})
public class S3Adapter implements S3Port {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${webclient.base-url}")
    private String baseUrl;

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
            if (staticUrl == null || staticUrl.isBlank()) {
                log.warn("업로드할 URL이 null 또는 비어있음");
                return null;
            }

            if (!staticUrl.startsWith("http")) {
                staticUrl = baseUrl + staticUrl;
            }

            log.info("S3 업로드 시작 - URL: {}", staticUrl);

            // 1. URL에서 데이터 다운로드
            URL url = new URL(staticUrl);
            URLConnection connection = url.openConnection();

            String contentType = connection.getContentType();

            // 2. 스트림을 byte[]로 읽기 (재시도 가능하도록)
            byte[] data;
            try (InputStream inputStream = connection.getInputStream()) {
                data = inputStream.readAllBytes();
            }

            log.info("파일 다운로드 완료 - Size: {} bytes", data.length);

            // 3. 확장자 추출
            String extension = "";
            if (contentType != null && contentType.contains("/")) {
                extension = "." + contentType.split("/")[1];
            }

            String fileName = UUID.randomUUID() + extension;

            // 4. 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(data.length);

            // 5. byte[]를 InputStream으로 변환하여 S3 업로드
            try (java.io.ByteArrayInputStream byteStream = new java.io.ByteArrayInputStream(data)) {
                amazonS3Client.putObject(bucket, fileName, byteStream, metadata);
            }

            String s3Url = amazonS3Client.getUrl(bucket, fileName).toString();
            log.info("S3 업로드 성공 - URL: {}", s3Url);

            return s3Url;
        } catch (IOException e) {
            log.error("Static URL로부터 S3 업로드 중 오류 발생 - URL: {}", staticUrl, e);
            return null;
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
