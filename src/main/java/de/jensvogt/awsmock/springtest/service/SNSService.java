package de.jensvogt.awsmock.springtest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SNSService {

    private final SnsClient snsClient;

    public String createTopic(String name) {

        String topicArn = "";
        CreateTopicResponse response = snsClient.createTopic(CreateTopicRequest.builder().name(name).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            topicArn = response.topicArn();
            log.info("Topic created topicArn: {}", topicArn);
        } else {
            log.error("Could not create topic, topicArn: {}", topicArn);
        }
        return topicArn;
    }

    public int listTopics() {

        int count = 0;
        ListTopicsResponse response = snsClient.listTopics();
        if (response.sdkHttpResponse().isSuccessful()) {
            count = response.topics().size();
            log.info("List topics");
        } else {
            log.error("Could not list topics");
        }
        return count;
    }

    public String subscribe(String topicArn, String queueUrl, String protocol) {

        SubscribeResponse response = snsClient.subscribe(SubscribeRequest.builder().topicArn(topicArn).protocol(protocol).endpoint(queueUrl).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Subscribed to queue, topicArn: {}, queueUrl: {}, protocol: {}", topicArn, queueUrl, protocol);
            return response.subscriptionArn();
        } else {
            log.error("Could not subscribe to endpoint, topicArn: {}, queueUrl: {}, protocol: {}", topicArn, queueUrl, protocol);
        }
        return "";
    }

    public void unsubscribe(String subscriptionArn) {

        UnsubscribeResponse response = snsClient.unsubscribe(UnsubscribeRequest.builder().subscriptionArn(subscriptionArn).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Unsubscribed from endpoint, subscriptionArn: {}", subscriptionArn);
        } else {
            log.error("Could not unsubscribe from endpoint, subscriptionArn: {}", subscriptionArn);
        }
    }

    public void deleteTopic(String topicArn) {

        DeleteTopicResponse response = snsClient.deleteTopic(DeleteTopicRequest.builder().topicArn(topicArn).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Delete topic, topicArn: {}", topicArn);
        } else {
            log.error("Could not delete topic, topicArn: {}", topicArn);
        }
    }
}
