package de.jensvogt.awsmock.springtest.dto;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.services.kms.model.KeyMetadata;

@Data
public class KeyMetadataDto {

    private String arn;

    private String keyId;

    private String accountId;

    private String keySpec;

    private String keyUsage;

    public KeyMetadataDto(KeyMetadata keyMetadata) {
        this.arn = keyMetadata.arn();
        this.keyId = keyMetadata.keyId();
        this.accountId = keyMetadata.awsAccountId();
        this.keySpec = keyMetadata.keySpecAsString();
        this.keyUsage = keyMetadata.keyUsageAsString();
    }
}
