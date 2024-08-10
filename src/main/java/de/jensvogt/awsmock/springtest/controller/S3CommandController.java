package de.jensvogt.awsmock.springtest.controller;

import de.jensvogt.awsmock.springtest.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/s3", produces = MediaType.APPLICATION_JSON_VALUE)
public class S3CommandController {

    private final S3Service s3Service;

    @PostMapping(path = "/createBucket", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createBucket(@RequestParam("bucketName") String bucketName) {

        log.info("POST request, createBucket, bucketName: {}", bucketName);
        String queueUrl = s3Service.createBucket(bucketName);

        return ResponseEntity.ok(queueUrl);
    }
}
