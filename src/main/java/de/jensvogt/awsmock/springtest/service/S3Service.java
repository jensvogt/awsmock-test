package de.jensvogt.awsmock.springtest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    private final ObjectMapper objectMapper;

    public String createBucket(String bucketName) {

        CreateBucketResponse response = s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        return bucketName;
    }
}
