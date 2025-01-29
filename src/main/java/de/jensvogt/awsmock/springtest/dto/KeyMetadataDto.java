package de.jensvogt.awsmock.springtest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import software.amazon.awssdk.services.kms.model.KeyMetadata;

@Data
public class KeyMetadataDto {

    @JsonProperty("Arn")
    private String arn;

    @JsonProperty("KeyId")
    private String keyId;

    @JsonProperty("AccountId")
    private String accountId;

    @JsonProperty("KeySpec")
    private String keySpec;

    @JsonProperty("KeyUsage")
    private String keyUsage;

    public KeyMetadataDto(KeyMetadata keyMetadata) {
        this.arn = keyMetadata.arn();
        this.keyId = keyMetadata.keyId();
        this.accountId = keyMetadata.awsAccountId();
        this.keySpec = keyMetadata.keySpecAsString();
        this.keyUsage = keyMetadata.keyUsageAsString();
    }
}
