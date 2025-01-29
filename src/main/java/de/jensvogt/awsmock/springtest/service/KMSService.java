package de.jensvogt.awsmock.springtest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KMSService {

    private final KmsClient kmsClient;

    public KeyMetadata createSymmetricKey(String keySpec, String keyUsage, String description) {

        KeyMetadata keyMetadata = null;
        CreateKeyResponse response = kmsClient.createKey(CreateKeyRequest.builder().keySpec(keySpec).keyUsage(keyUsage).description(description).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            keyMetadata = response.keyMetadata();
            log.info("Key created keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        } else {
            log.error("Could not create key, keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        }
        return keyMetadata;
    }

    public KeyMetadata createRSA2048Key(String keySpec, String keyUsage, String description) {

        KeyMetadata keyMetadata = null;
        CreateKeyResponse response = kmsClient.createKey(CreateKeyRequest.builder().keySpec(keySpec).keyUsage(keyUsage).description(description).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            keyMetadata = response.keyMetadata();
            log.info("RSA 2048 key created keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        } else {
            log.error("Could not create RSA 2048 key, keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        }
        return keyMetadata;
    }

    public KeyMetadata createRSA3072Key(String keySpec, String keyUsage, String description) {

        KeyMetadata keyMetadata = null;
        CreateKeyResponse response = kmsClient.createKey(CreateKeyRequest.builder().keySpec(keySpec).keyUsage(keyUsage).description(description).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            keyMetadata = response.keyMetadata();
            log.info("RSA 3072 key created keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        } else {
            log.error("Could not create RSA 3072 key, keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        }
        return keyMetadata;
    }

    public KeyMetadata createRSA4096Key(String keySpec, String keyUsage, String description) {

        KeyMetadata keyMetadata = null;
        CreateKeyResponse response = kmsClient.createKey(CreateKeyRequest.builder().keySpec(keySpec).keyUsage(keyUsage).description(description).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            keyMetadata = response.keyMetadata();
            log.info("RSA 4096 key created keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        } else {
            log.error("Could not create RSA 4096 key, keySpec: {}, keyUsage: {}", keySpec, keyUsage);
        }
        return keyMetadata;
    }

    public List<KeyListEntry> listKeys(int limit) {

        List<KeyListEntry> keys = new ArrayList<>();
        ListKeysResponse response = kmsClient.listKeys(ListKeysRequest.builder().limit(limit).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            keys = response.keys();
            log.info("List keys, limit: {}", limit);
        } else {
            log.error("Could not list keys, limit: {}", limit);
        }
        return keys;
    }
}
