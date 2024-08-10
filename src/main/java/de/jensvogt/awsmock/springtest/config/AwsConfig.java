package de.jensvogt.awsmock.springtest.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.awscore.AwsClient;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.text.SimpleDateFormat;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AwsConfig {

    @Value("${de.jensvogt.awsmock.endpoint}")
    protected String awsmockEndpoint;

    @Value("${server.port}")
    private int serverPort;

    @Primary
    @Bean(name = "credentialsProvider")
    public AwsCredentialsProvider awsCredentialsProvider() {
        return ProfileCredentialsProvider.create();
    }

    @Bean
    @Primary
    public SqsAsyncClient sqsAsyncClient(
            @Autowired(required = false) AwsCredentialsProvider awsCredentialsProvider) {
        SqsAsyncClientBuilder builder = SqsAsyncClient.builder();
        return buildClient(builder, awsCredentialsProvider);
    }

    @Bean
    @Primary
    public SqsClient sqsClient(
            @Autowired(required = false) AwsCredentialsProvider awsCredentialsProvider) {
        return buildClient(SqsClient.builder(), awsCredentialsProvider);
    }

    @Bean
    @Primary
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper) {
        // Receiving end will fail to deserialize message when JavaType-Header is set in message and DTO
        // is not in same package as on sending side. Disabling default behaviour. See
        // https://docs.awspring.io/spring-cloud-aws/docs/3.0.0/reference/html/index.html#specifying-a-payload-class-for-receive-operations
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .configureDefaultConverter(
                        converter -> {
                            converter.setObjectMapper(objectMapper);
                            converter.setPayloadTypeHeaderValueFunction(message -> null);
                        })
                .build();
    }

    @Bean
    @Primary
    public S3Client s3Client(
            @Autowired(required = false) AwsCredentialsProvider awsCredentialsProvider) {
        S3ClientBuilder builder = S3Client.builder().forcePathStyle(true);
        return buildClient(builder, awsCredentialsProvider);
    }

    /**
     * Jackson JSON mapper.
     *
     * @return JSON object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        return objectMapper;
    }

    private <T extends AwsClientBuilder<T, S>, S extends AwsClient> S buildClient(
            T builder, AwsCredentialsProvider awsCredentialsProvider) {
        setEndpointAndRegion(builder, awsCredentialsProvider);

        return builder.build();
    }

    private <T extends AwsClientBuilder<T, S>, S extends AwsClient> void setEndpointAndRegion(
            T builder, AwsCredentialsProvider awsCredentialsProvider) {
        log.info("Using endpoint: {}, serverPort: {}", awsmockEndpoint, serverPort);
        builder.region(Region.EU_CENTRAL_1);
        builder.endpointOverride(URI.create(awsmockEndpoint));
        if (awsCredentialsProvider != null) {
            builder.credentialsProvider(awsCredentialsProvider);
        }
    }
}
