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

import java.util.*;

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
        CreateQueueResponse response = sqsClient.createQueue(CreateQueueRequest.builder().queueName(queueName).attributes(attributes).tags(tags).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            queueUrl = response.queueUrl();
            log.info("Queue created queueUrl: {}", queueUrl);
        } else {
            log.error("Could not create queue, queueName: {}", queueName);
        }
        return queueUrl;
    }

    public Map<QueueAttributeName, String> getAllQueueAttributes(String queueUrl) {

        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder().queueUrl(queueUrl).attributeNames(QueueAttributeName.ALL).build());

        if (response.sdkHttpResponse().isSuccessful()) {

            Map<QueueAttributeName, String> attributes = response.attributes();
            log.info("Get all queue attributes, count: {}", attributes.size());
            return response.attributes();

        } else {

            log.error("Could not get all queue attributes, queueUrl: {}", queueUrl);

        }
        return new HashMap<>();
    }

    public Map<QueueAttributeName, String> getSingleQueueAttribute(String queueUrl, String attributeName) {

        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder().queueUrl(queueUrl).attributeNames(QueueAttributeName.valueOf(attributeName)).build());

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

    public String sendMessage(String queueUrl, TestMessage testMessage) throws JsonProcessingException {

        String messageId = null;
        String jsonString = objectMapper.writeValueAsString(testMessage);
        SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(jsonString).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            messageId = response.messageId();
            log.info("Send message, queuerUrl: {},id: {}", queueUrl, messageId);
        } else {
            log.error("Could not send message, queueUrl: {}", queueUrl);
        }
        return messageId;
    }

    public void sendSqsTemplate(TestMessage testMessage) {

        log.info("Received send message via sns template request, testMessage: {}", testMessage);

        SendResult<TestMessage> response = sqsTemplate.send("test-queue", testMessage);
        log.info("Send message via sns template, testMessage: {} endpoint: {}", testMessage, response.endpoint());

    }

    public List<TestMessage> receiveMessages(String queueUrl, int maxMessages, int maxWaitTime) throws JsonProcessingException {

        List<Message> messages;
        List<TestMessage>testMessages = new ArrayList<>();
        ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(maxMessages).waitTimeSeconds(maxWaitTime).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            messages = response.messages();

            for(Message message:messages) {
                TestMessage testMessage = objectMapper.readValue(message.body(), TestMessage.class);
                testMessage.setReceiptHandle(message.receiptHandle());
                testMessages.add(testMessage);
            }

            log.info("Receive messages, queueUrl: {}, size: {}", queueUrl, messages.size());
        } else {
            log.error("Could not receive messages, queueUrl: {}", queueUrl);
        }
        return testMessages;
    }

    public void deleteMessage(String queueName, String receiptHandle) {

        log.info("Received delete message request, queueName: {} receiptHandle: {}", queueName, receiptHandle);

        String queueUrl = getQueueUrl(queueName);
        DeleteMessageResponse response = sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build());

        log.info("Message deleted, queueUrl: {}, receiptHandle: {}, httpStatus: {}", queueUrl, receiptHandle, response.sdkHttpResponse().statusCode());
    }

    public void deleteQueue(String queueName) {

        log.info("Received delete queue request, queueName: {}", queueName);

        String queueUrl = getQueueUrl(queueName);
        DeleteQueueResponse response = sqsClient.deleteQueue(DeleteQueueRequest.builder().queueUrl(queueUrl).build());

        log.info("Queue deleted, queueUrl: {}, httpStatus: {}", queueUrl, response.sdkHttpResponse().statusCode());
    }

    public void cleanup() {

        log.info("Received cleanup request");

        String queueUrl = getQueueUrl("test-queue");
        DeleteQueueResponse response = sqsClient.deleteQueue(DeleteQueueRequest.builder().queueUrl(queueUrl).build());

        log.info("Cleanup, httpStatus: {}", response.sdkHttpResponse().statusCode());
    }
}
