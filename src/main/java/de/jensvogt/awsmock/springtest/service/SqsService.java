package de.jensvogt.awsmock.springtest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jensvogt.awsmock.springtest.dto.TestMessage;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsService {

    private static final int VISIBILITY_TIMEOUT = 99;

    private final SqsClient sqsClient;

    private final ObjectMapper objectMapper;

    private final SqsTemplate sqsTemplate;

    public String createQueue(String queueName) {

        Map<QueueAttributeName, String> attributes = new HashMap<>();
        attributes.put(QueueAttributeName.VISIBILITY_TIMEOUT, String.valueOf(VISIBILITY_TIMEOUT));

        Map<String, String> tags = new HashMap<>();
        tags.put("tagName", "tagValue");

        String queueUrl = null;
        CreateQueueResponse response = sqsClient.createQueue(CreateQueueRequest
                .builder()
                .queueName(queueName)
                .attributes(attributes)
                .tags(tags)
                .build());
        if (response.sdkHttpResponse().isSuccessful()) {
            queueUrl = response.queueUrl();
            log.info("Queue created queueUrl: {}", queueUrl);
        } else {
            log.error("Could not create queue, queueName: {}", queueName);
        }
        return queueUrl;
    }

    public Map<QueueAttributeName, String> getAllQueueAttributes(String queueUrl) {

        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(GetQueueAttributesRequest
                .builder()
                .queueUrl(queueUrl)
                .attributeNames(QueueAttributeName.ALL)
                .build());

        if (response.sdkHttpResponse().isSuccessful()) {

            Map<QueueAttributeName, String> attributes = response.attributes();
            log.info("Get all queue attributes, count: {}", attributes.size());
            return response.attributes();

        } else {

            log.error("Could not get all queue attributes, queueUrl: {}", queueUrl);

        }
        return new HashMap<>();
    }

    public Map<QueueAttributeName, String>  getSingleQueueAttribute(String queueUrl, String attributeName) {

        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(GetQueueAttributesRequest
                .builder()
                .queueUrl(queueUrl)
                .attributeNames(QueueAttributeName.valueOf(attributeName))
                .build());

        if (response.sdkHttpResponse().isSuccessful()) {

            Map<QueueAttributeName, String> attributes = response.attributes();
            log.info("Get single queue attributes, count: {}", attributes.size());
            return response.attributes();

        } else {

            log.error("Could not get single queue attributes, queueUrl: {}", queueUrl);

        }
        return new HashMap<>();
    }

    public String getQueueUrl(String queueName) {

        GetQueueUrlResponse response = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());

        if (response.sdkHttpResponse().isSuccessful()) {

            log.info("Get queue URL, url: {}", response.queueUrl());
            return response.queueUrl();

        } else {

            log.error("Could not get queue URL, queueName: {}", queueName);

        }
        return "";
    }

    public void sendMessage(String queueUrl, TestMessage testMessage) throws JsonProcessingException {

        String jsonString = objectMapper.writeValueAsString(testMessage);
        SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest
                .builder()
                .queueUrl(queueUrl)
                .messageBody(jsonString)
                .build());
        if (response.sdkHttpResponse().isSuccessful()) {
            String messageId = response.messageId();
            log.info("Send message, id: {}", messageId);
        } else {
            log.error("Could not send message, queueUrl: {}", ""/*queueUrl*/);
        }
    }

    public void sendSqsTemplate(TestMessage testMessage) throws JsonProcessingException {

        log.info("Received send message via sns template request, testMessage: {}", testMessage);

        SendResult<TestMessage> response = sqsTemplate.send("test-queue", testMessage);
        log.info("Send message via sns template, testMessage: {} endpoint: {}", testMessage, response.endpoint());

    }

    /*@SqsListener(queueNames = {"http://vogje01-nuc:4566/000000000000/test-queue"})
    public void SqsListener(@Payload TestMessage testMessage, @Headers MessagingMessageHeaders messageHeaders) {
        log.info("Receive message, id: {}", messageHeaders.get(MessageHeaders.ID));
        log.info("Receive message, message: {}", testMessage.toString());
        cleanup();
    }*/
}
