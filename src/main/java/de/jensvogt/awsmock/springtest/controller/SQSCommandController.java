package de.jensvogt.awsmock.springtest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.jensvogt.awsmock.springtest.dto.TestMessage;
import de.jensvogt.awsmock.springtest.service.SQSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/sqs", produces = MediaType.APPLICATION_JSON_VALUE)
public class SQSCommandController {

    private final SQSService sqsService;

    @PostMapping(path = "/createQueue", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createQueue(@RequestParam("queueName") String queueName) {

        log.info("POST request, createQueue, queueName: {}", queueName);
        String queueUrl = sqsService.createQueue(queueName);

        return ResponseEntity.ok(queueUrl);
    }

    @PostMapping(path = "/getAllQueueAttributes", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<QueueAttributeName, String>> getAllQueueAttributes(@RequestParam("queueUrl") String queueUrl) {

        log.info("POST request, getAllQueueAttributes, queueUrl: {}", queueUrl);
        Map<QueueAttributeName, String> attributes = sqsService.getAllQueueAttributes(queueUrl);

        return ResponseEntity.ok(attributes);
    }

    @GetMapping(path = "/getSingleQueueAttribute", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<QueueAttributeName, String>> getSingleQueueAttributes(@RequestParam("queueUrl") String queueUrl, @RequestParam("attributeName") String attributeName) {

        log.info("GET request, getSingleQueueAttribute, queueUrl: {} attribute: {}", queueUrl, attributeName);
        Map<QueueAttributeName, String> attributes = sqsService.getSingleQueueAttribute(queueUrl, attributeName);

        return ResponseEntity.ok(attributes);
    }

    @PostMapping(path = "/setQueueAttribute", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<QueueAttributeName, String>> setQueueAttribute(@RequestParam("queueUrl") String queueUrl, @RequestParam("attributeValue") String attributeValue) {

        log.info("POST request, setQueueAttribute, queueUrl: {} attributeValue: {}", queueUrl, attributeValue);
        sqsService.setQueueAttribute(queueUrl, attributeValue);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/changeVisibility", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<QueueAttributeName, String>> changeVisibility(@RequestParam("queueUrl") String queueUrl, @RequestParam("visibility") int visibility,
                                                                     @RequestParam("receiptHandle") String receiptHandle) {

        log.info("POST request, changeVisibility, queueUrl: {} visibility: {}, receiptHandle: {}", queueUrl, visibility, receiptHandle);
        sqsService.changeMessageVisibility(queueUrl, visibility, receiptHandle);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getQueueUrl", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getQueueUrl(@RequestParam("queueName") String queueName) {

        log.info("GET request, getQueueUrl, queueName: {}", queueName);
        String queueUrl = sqsService.getQueueUrl(queueName);

        return ResponseEntity.ok(queueUrl);
    }

    @GetMapping(path = "/purgeQueue", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> purgeQueue(@RequestParam("queueUrl") String queueUrl) {

        log.info("GET request, purgeQueue, queueName: {}", queueUrl);
        sqsService.purgeQueue(queueUrl);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/sqsTemplate", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestMessage> sendViaSqsTemplate(@RequestBody TestMessage testMessage) {

        log.info("POST request, sqsTemplate, testMessage: {}", testMessage);
        sqsService.sendSqsTemplate(testMessage);

        return ResponseEntity.ok(testMessage);
    }

    @PostMapping(path = "/sendMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> sendMessage(@RequestParam("queueUrl") String queueUrl, @RequestBody TestMessage testMessage) throws JsonProcessingException {

        log.info("POST request, sendMessage, queueUrl: {} testMessage: {}", queueUrl, testMessage);
        String messageId = sqsService.sendMessage(queueUrl, testMessage);

        return ResponseEntity.ok(messageId);
    }

    @PostMapping(path = "/sendMessageAttributes", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Integer> sendMessageAttributes(@RequestParam("queueUrl") String queueUrl, @RequestBody TestMessage testMessage) throws JsonProcessingException {

        log.info("POST request, sendMessageAttributes, queueUrl: {} testMessage: {}", queueUrl, testMessage);
        int count = sqsService.sendMessageAttributes(queueUrl, testMessage);

        return ResponseEntity.ok(count);
    }

    @GetMapping(path = "/receiveMessages", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TestMessage>> receiveMessage(@RequestParam("queueUrl") String queueUrl, @RequestParam("maxMessages") int maxMessages, @RequestParam("maxWaitTime") int maxWaitTime) throws JsonProcessingException {

        log.info("POST request, receiveMessage, queueUrl: {}", queueUrl);
        List<TestMessage> messages = sqsService.receiveMessages(queueUrl, maxMessages, maxWaitTime);

        return ResponseEntity.ok(messages);
    }

    @DeleteMapping(path = "/deleteMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteMessage(@RequestParam("queueUrl") String queueUrl, @RequestParam("receiptHandle") String receiptHandle) {

        log.info("DELETE request, deleteMessage, receiptHandle: {}", receiptHandle);
        sqsService.deleteMessage(queueUrl, receiptHandle);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteMessageBatch", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteMessageBatch(@RequestParam("queueUrl") String queueUrl, @RequestParam("receiptHandle1") String receiptHandle1
            , @RequestParam("receiptHandle2") String receiptHandle2) {

        log.info("DELETE request, deleteMessageBatch, queueUrl: {}", queueUrl);
        sqsService.deleteMessageBatch(queueUrl, receiptHandle1, receiptHandle2);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/deleteQueue", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteQueue(@RequestBody String queueName) {

        log.info("GET request, deleteQueue, queueName: {}", queueName);
        sqsService.deleteQueue(queueName);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/cleanup", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> sendMessage() {

        log.info("GET request, cleanup");
        sqsService.cleanup();

        return ResponseEntity.ok().build();
    }
}
