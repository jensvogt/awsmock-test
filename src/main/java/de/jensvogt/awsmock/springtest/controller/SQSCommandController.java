package de.jensvogt.awsmock.springtest.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import de.jensvogt.awsmock.springtest.dto.TestMessage;
import de.jensvogt.awsmock.springtest.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.Map;

@RestController
@RequestMapping(
        path = "/api/sqs",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SQSCommandController {
    private final SqsService sqsService;

    @Autowired
    public SQSCommandController(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    @PostMapping(path = "/createQueue", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createQueue(@RequestBody String queueName) {

        String queueUrl = sqsService.createQueue(queueName);

        return ResponseEntity.ok(queueUrl);
    }

    @PostMapping(path = "/getAllQueueAttributes", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<QueueAttributeName, String>> getAllQueueAttributes(@RequestParam("queueUrl") String queueUrl) {

        Map<QueueAttributeName, String> attributes = sqsService.getAllQueueAttributes(queueUrl);

        return ResponseEntity.ok(attributes);
    }

    @GetMapping(path = "/getSingleQueueAttribute", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<QueueAttributeName, String>> getSingleQueueAttributes(@RequestParam("queueUrl") String queueUrl,
                                                                             @RequestParam("attributeName") String attributeName) {

        Map<QueueAttributeName, String> attributes = sqsService.getSingleQueueAttribute(queueUrl, attributeName);

        return ResponseEntity.ok(attributes);
    }

    @GetMapping(path = "/getQueueUrl", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getQueueUrl(@RequestParam("queueName") String queueName) throws InterruptedException {

        String queueUrl = sqsService.getQueueUrl(queueName);

        return ResponseEntity.ok(queueUrl);
    }

    @PostMapping(path = "/sqsTemplate", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestMessage> sendViaSqsTemplate(@RequestBody TestMessage testMessage) throws JsonProcessingException {

        sqsService.sendSqsTemplate(testMessage);

        return ResponseEntity.ok(testMessage);
    }

    @PostMapping(path = "/sendMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestMessage> sendMessage(@RequestParam("queueUrl") String queueUrl,
                                            @RequestBody TestMessage testMessage) throws JsonProcessingException {

        sqsService.sendMessage(queueUrl, testMessage);

        return ResponseEntity.ok(testMessage);
    }
}
