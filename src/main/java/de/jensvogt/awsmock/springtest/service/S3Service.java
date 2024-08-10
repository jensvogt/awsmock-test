package de.jensvogt.awsmock.springtest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public String createBucket(String bucketName) {

        CreateBucketResponse response = s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Bucket created {}", bucketName);
            return response.location();
        } else {
            log.error("Could not create bucket, bucketName: {}", bucketName);
        }
        return "";
    }

    public void putObject(String bucketName, String key, Long size) throws IOException {

        Path randomPath = writeRandomFile((int) (size * 1024 * 1024));
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromFile(randomPath.toFile()));

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Object created, bucketName: {}, key: {}, size: {}", bucketName, key, size);
        } else {
            log.error("Could not create object, bucketName: {}, key: {}, size: {}", bucketName, key, size);
        }
    }

    public void getObject(String bucketName, String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();

        try {
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            byte[] data = objectBytes.asByteArray();

            // Write the data to a local file.
            Path destination = Files.createTempFile("random", ".txt");
            OutputStream os = new FileOutputStream(destination.toFile());
            os.write(data);
            os.close();

            log.info("Object created, bucketName: {}, key: {}", bucketName, key);
            FileUtils.deleteQuietly(destination.toFile());
        } catch (IOException | S3Exception ex) {
            log.error("Could not create object, bucketName: {}, key: {}", bucketName, key);
        }
    }

    public void deleteBucket(String bucketName) {

        DeleteBucketResponse response = s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Bucket deleted {}", bucketName);
        } else {
            log.error("Could not delete bucket, bucketName: {}", bucketName);
        }
    }

    private Path writeRandomFile(int length) throws IOException {
        Path destination = Files.createTempFile("random", ".txt");
        try (FileWriter writer = new FileWriter(destination.toFile())) {
            String generatedString = RandomStringUtils.random(length, true, true);
            writer.write(generatedString);
        }
        return destination;
    }
}
