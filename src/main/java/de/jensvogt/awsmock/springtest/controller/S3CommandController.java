package de.jensvogt.awsmock.springtest.controller;

import de.jensvogt.awsmock.springtest.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/s3", produces = MediaType.APPLICATION_JSON_VALUE)
public class S3CommandController {

    private final S3Service s3Service;

    @PostMapping(path = "/createBucket", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createBucket(@RequestParam("bucketName") String bucketName) {

        log.info("POST request, createBucket, bucketName: {}", bucketName);
        String response = s3Service.createBucket(bucketName);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/listBuckets", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Integer> listBuckets() {

        log.info("GET request, listBuckets");
        int response = s3Service.listBucket();

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/putObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> putObject(@RequestParam("bucketName") String bucketName, @RequestParam("key") String key, @RequestParam("size") Long size) throws IOException {

        log.info("PUT request, putObject, bucketName: {}, key: {}, size: {}", bucketName, key, size);
        s3Service.putObject(bucketName, key, size);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getObject(@RequestParam("bucketName") String bucketName, @RequestParam("key") String key) {

        log.info("GET request, getObject, bucketName: {}, key: {}", bucketName, key);
        s3Service.getObject(bucketName, key);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getHead", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Long> getHead(@RequestParam("bucket") String bucket, @RequestParam("key") String key) {

        log.info("GET request, getObject, bucket: {}, key: {}", bucket, key);
        long size = s3Service.getHead(bucket, key);

        return ResponseEntity.ok(size);
    }

    @PutMapping(path = "/putBucketVersioning", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Long> putBucketVersioning(@RequestParam("bucket") String bucket) {

        log.info("PUT request, putBucketVersioning, bucket: {}", bucket);
        s3Service.putBucketVersioning(bucket);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/listObjectVersions", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Integer> listObjectVersions(@RequestParam("bucket") String bucket, @RequestParam("prefix") String prefix) {

        log.info("GET request, listObjectVersions, bucket: {}, prefix: {}", bucket, prefix);
        int count = s3Service.listObjectVersions(bucket, prefix);

        return ResponseEntity.ok(count);
    }

    @GetMapping(path = "/uploadObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> uploadObject(@RequestParam("bucketName") String bucketName, @RequestParam("key") String key, @RequestParam("size") int size) throws IOException {

        log.info("GET request, uploadObject, bucketName: {}, key: {}, size: {}", bucketName, key, size);
        s3Service.uploadObject(bucketName, key, size);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/downloadObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> downloadObject(@RequestParam("bucketName") String bucketName, @RequestParam("key") String key) throws IOException {

        log.info("GET request, downloadObject, bucketName: {}, key: {}", bucketName, key);
        s3Service.downloadObject(bucketName, key);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/copyObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> copyObject(@RequestParam("sourceBucket") String sourceBucket, @RequestParam("sourceKey") String sourceKey,
                                      @RequestParam("destinationBucket") String destinationBucket, @RequestParam("destinationKey") String destinationKey) {

        log.info("POST request, copyObject, sourceBucket: {}, sourceKey: {}, destinationBucket: {}, destinationKey: {}", sourceBucket, sourceKey, destinationBucket, destinationKey);
        s3Service.copyObject(sourceBucket, sourceKey, destinationBucket, destinationKey);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteBucket", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteBucket(@RequestParam("bucketName") String bucketName) {

        log.info("DELETE request, deleteBucket, bucketName: {}", bucketName);
        s3Service.deleteBucket(bucketName);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteObject(@RequestParam("bucketName") String bucketName, @RequestParam("key") String key) {

        log.info("DELETE request, deleteObject, bucketName: {}, key: {}", bucketName, key);
        s3Service.deleteObject(bucketName, key);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteObjects", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteObjects(@RequestParam("bucketName") String bucketName, @RequestParam("key1") String key1, @RequestParam("key2") String key2) {

        log.info("DELETE request, deleteObjects, bucketName: {}, key1: {}, key2: {}", bucketName, key1, key2);
        s3Service.deleteObjects(bucketName, key1, key2);

        return ResponseEntity.ok().build();
    }
}
