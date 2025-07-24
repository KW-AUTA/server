package com.auta.server.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Port {

    String upload(MultipartFile jsonFile);

    String upload(String staticUrl);

    void delete(String oldFigmaJsonUrl);
}
