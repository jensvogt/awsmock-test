package de.jensvogt.awsmock.springtest.dto;

import lombok.Data;
import software.amazon.awssdk.services.kms.model.KeyListEntry;

@Data
public class KeyDto {

    private String key;

    private String arn;

    public KeyDto(KeyListEntry key) {
        this.key = key.keyId();
        this.arn = key.keyArn();
    }
}
