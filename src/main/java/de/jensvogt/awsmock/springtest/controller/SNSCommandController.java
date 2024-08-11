package de.jensvogt.awsmock.springtest.controller;

import de.jensvogt.awsmock.springtest.service.SNSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/sns", produces = MediaType.APPLICATION_JSON_VALUE)
public class SNSCommandController {

    private final SNSService snsService;

    @PostMapping(path = "/createTopic", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createTopic(@RequestParam("name") String name) {

        log.info("POST request, createQueue, name: {}", name);
        String topicArn = snsService.createTopic(name);

        return ResponseEntity.ok(topicArn);
    }

    @GetMapping(path = "/listTopics", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Integer> listTopics() {

        log.info("GET request, listTopics");
        int count = snsService.listTopics();

        return ResponseEntity.ok(count);
    }

    @PostMapping(path = "/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> subscribe(@RequestParam("topicArn") String topicArn, @RequestParam("queueUrl") String queueUrl, @RequestParam("protocol") String protocol) {

        log.info("POST request, subscribe, topicArn: {}, queueUrl: {}, protocol: {}", topicArn, queueUrl, protocol);
        String subscriptionArn = snsService.subscribe(topicArn, queueUrl, protocol);

        return ResponseEntity.ok(subscriptionArn);
    }

    @PostMapping(path = "/unsubscribe", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> unsubscribe(@RequestParam("subscriptionArn") String subscriptionArn) {

        log.info("POST request, unsubscribe, subscriptionArn: {}", subscriptionArn);
        snsService.unsubscribe(subscriptionArn);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteTopic", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteTopic(@RequestParam("topicArn") String topicArn) {

        log.info("POST request, deleteTopic, topicArn: {}", topicArn);
        snsService.deleteTopic(topicArn);

        return ResponseEntity.ok().build();
    }
}
