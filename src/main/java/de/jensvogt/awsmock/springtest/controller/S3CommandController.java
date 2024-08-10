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

    @DeleteMapping(path = "/deleteBucket", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteBucket(@RequestParam("bucketName") String bucketName) {

        log.info("DELETE request, deleteBucket, bucketName: {}", bucketName);
        s3Service.deleteBucket(bucketName);

        return ResponseEntity.ok().build();
    }
}
