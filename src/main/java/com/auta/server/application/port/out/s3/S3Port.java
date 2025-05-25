package com.auta.server.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Port {

    String upload(MultipartFile jsonFile);

    void delete(String oldFigmaJsonUrl);
}
