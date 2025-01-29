package de.jensvogt.awsmock.springtest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import software.amazon.awssdk.services.kms.model.KeyListEntry;

@Data
public class KeyDto {

    @JsonProperty("KeyId")
    private String key;

    @JsonProperty("KeyArn")
    private String arn;

    public KeyDto(KeyListEntry key) {
        this.key = key.keyId();
        this.arn = key.keyArn();
    }
}
