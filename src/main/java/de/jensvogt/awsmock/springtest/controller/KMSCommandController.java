package de.jensvogt.awsmock.springtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.jensvogt.awsmock.springtest.dto.KeyMetadataDto;
import de.jensvogt.awsmock.springtest.service.KMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.kms.model.KeyListEntry;
import software.amazon.awssdk.services.kms.model.KeyMetadata;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/kms", produces = MediaType.APPLICATION_JSON_VALUE)
public class KMSCommandController {

    private final KMSService kmsService;

    @PostMapping(path = "/createSymmetricKey")
    ResponseEntity<KeyMetadataDto> createSymmetricKey(@RequestParam("keySpec") String keySpec, @RequestParam("keyUsage") String keyUsage,
                                                      @RequestParam("description") String description) throws JsonProcessingException {

        log.info("POST request, createSymmetricKey, keySpec: {}", keySpec);
        KeyMetadata keyMetadata = kmsService.createSymmetricKey(keySpec, keyUsage, description);
        log.info("POST request succeeded, createSymmetricKey, keySpec: {} keyId: {}", keySpec, keyMetadata.keyId());

        return ResponseEntity.ok(new KeyMetadataDto(keyMetadata));
    }

    @PostMapping(path = "/createRSA2048Key")
    ResponseEntity<KeyMetadata> createRSA2048Key(@RequestParam("keySpec") String keySpec, @RequestParam("keyUsage") String keyUsage,
                                                 @RequestParam("description") String description) throws JsonProcessingException {

        log.info("POST request, createRSA2048Key, keySpec: {}", keySpec);
        KeyMetadata keyMetadata = kmsService.createRSA2048Key(keySpec, keyUsage, description);
        log.info("POST request succeeded, createRSA2048Key, keySpec: {} keyId: {}", keySpec, keyMetadata.keyId());

        return ResponseEntity.ok(keyMetadata);
    }

    @PostMapping(path = "/createRSA3072Key")
    ResponseEntity<KeyMetadata> createRSA3072Key(@RequestParam("keySpec") String keySpec, @RequestParam("keyUsage") String keyUsage,
                                                 @RequestParam("description") String description) throws JsonProcessingException {

        log.info("POST request, createRSA3072Key, keySpec: {}", keySpec);
        KeyMetadata keyMetadata = kmsService.createRSA3072Key(keySpec, keyUsage, description);
        log.info("POST request succeeded, createRSA3072Key, keySpec: {} keyId: {}", keySpec, keyMetadata.keyId());

        return ResponseEntity.ok(keyMetadata);
    }

    @PostMapping(path = "/createRSA4096Key")
    ResponseEntity<KeyMetadata> createRSA4096Key(@RequestParam("keySpec") String keySpec, @RequestParam("keyUsage") String keyUsage,
                                                 @RequestParam("description") String description) throws JsonProcessingException {

        log.info("POST request, createRSA4096Key, keySpec: {}", keySpec);
        KeyMetadata keyMetadata = kmsService.createRSA4096Key(keySpec, keyUsage, description);
        log.info("POST request succeeded, createRSA4096Key, keySpec: {} keyId: {}", keySpec, keyMetadata.keyId());

        return ResponseEntity.ok(keyMetadata);
    }

    @PostMapping(path = "/listKeys")
    ResponseEntity<List<KeyListEntry>> listKeys(@RequestParam("limit") int limit) throws JsonProcessingException {

        log.info("POST request, listKeys, limit: {}", limit);
        List<KeyListEntry> keys = kmsService.listKeys(limit);
        log.info("POST request succeeded, listKeys, count: {}", keys.size());

        return ResponseEntity.ok(keys);
    }
}
