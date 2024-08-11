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
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.FileDownload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static software.amazon.awssdk.services.s3.model.BucketVersioningStatus.ENABLED;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3TransferManager s3TransferManager;

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

    public int listBucket() {

        ListBucketsResponse response = s3Client.listBuckets();
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Bucket list created");
            return response.buckets().size();
        } else {
            log.error("Could not list buckets");
        }
        return 0;
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

    public Long getHead(String bucket, String key) {

        HeadObjectResponse response = s3Client.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Got object metadata, bucket: {}, key: {}, size: {}", bucket, key, response.contentLength());
            return response.contentLength();
        } else {
            log.error("Could not get metadata of object, bucket: {}, key: {}", bucket, key);
        }
        return 0L;
    }

    public void putBucketVersioning(String bucket) {

        VersioningConfiguration versioningConfiguration = VersioningConfiguration.builder().status(ENABLED).build();
        PutBucketVersioningResponse response = s3Client.putBucketVersioning(PutBucketVersioningRequest.builder().bucket(bucket).versioningConfiguration(versioningConfiguration).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Put bucket versioning, bucket: {}", bucket);
        } else {
            log.error("Could not put bucket versioning, bucket: {}", bucket);
        }
    }

    public int listObjectVersions(String bucket, String prefix) {

        ListObjectVersionsResponse response = s3Client.listObjectVersions(ListObjectVersionsRequest.builder().bucket(bucket).prefix(prefix).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("List object version, bucket: {}, prefix: {}, size: {}", bucket, prefix, response.versions().size());
            return response.versions().size();
        } else {
            log.error("Could not list object versions, bucket: {}, prefix: {}", bucket, prefix);
        }
        return 0;
    }

    public void uploadObject(String bucketName, String key, int size) throws IOException {

        Path destination = writeRandomFile(size * 1024 * 1024);
        UploadFileRequest uploadFileRequest =
                UploadFileRequest.builder()
                        .putObjectRequest(r -> r.bucket(bucketName).key(key))
                        .source(destination)
                        .build();

        FileUpload uploadFile = s3TransferManager.uploadFile(uploadFileRequest);
        uploadFile.completionFuture().join();

        log.info("Object uploaded, bucketName: {}, key: {}, size: {}", bucketName, key, size);
        FileUtils.deleteQuietly(destination.toFile());
    }

    public void downloadObject(String bucketName, String key) throws IOException {

        Path destination = Files.createTempFile("random", ".txt");
        DownloadFileRequest downloadFileRequest =
                DownloadFileRequest.builder()
                        .getObjectRequest(b -> b.bucket(bucketName).key(key))
                        .destination(destination)
                        .build();

        FileDownload downloadFile = s3TransferManager.downloadFile(downloadFileRequest);
        downloadFile.completionFuture().join();

        log.info("Object downloaded, bucketName: {}, key: {}", bucketName, key);
        FileUtils.deleteQuietly(destination.toFile());
    }

    public void copyObject(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {

        CopyObjectResponse response = s3Client.copyObject(CopyObjectRequest.builder()
                .sourceBucket(sourceBucket)
                .sourceKey(sourceKey)
                .destinationBucket(destinationBucket)
                .destinationKey(destinationKey)
                .build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Object copied, sourceBucket: {}, sourceKey: {}, destinationBucket: {}, destinationKey: {}", sourceBucket, sourceKey, destinationBucket, destinationKey);
        } else {
            log.error("Could not copy object, sourceBucket: {}, sourceKey: {}, destinationBucket: {}, destinationKey: {}", sourceBucket, sourceKey, destinationBucket, destinationKey);
        }
    }

    public void deleteBucket(String bucket) {

        DeleteBucketResponse response = s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucket).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Bucket deleted, bucket: {}", bucket);
        } else {
            log.error("Could not delete bucket, bucketName: {}", bucket);
        }
    }

    public void deleteObject(String bucket, String key) {

        DeleteObjectResponse response = s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Object deleted, bucket: {}, key: {}", bucket, key);
        } else {
            log.error("Could not delete object, bucket: {}, key: {}", bucket, key);
        }
    }

    public void deleteObjects(String bucket, String key1, String key2) {

        List<ObjectIdentifier> objectIdentifiers = new ArrayList<>();
        objectIdentifiers.add(ObjectIdentifier.builder().key(key1).build());
        objectIdentifiers.add(ObjectIdentifier.builder().key(key2).build());

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(d -> d.objects(objectIdentifiers).build())
                .build();

        DeleteObjectsResponse response = s3Client.deleteObjects(deleteObjectsRequest);
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Objects deleted, bucket: {}, key1: {}, key2: {}", bucket, key1, key2);
        } else {
            log.error("Could not delete objects, bucket: {}, key1: {}, key2: {}", bucket, key1, key2);
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
