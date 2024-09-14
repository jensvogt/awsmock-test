package de.jensvogt.awsmock.springtest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamodbService {

    private final DynamoDbClient dynamoDbClient;

    public void createTable(String tableName) {

        AttributeDefinition attributeDefinition = AttributeDefinition.builder().attributeName("orgaNr").attributeType(ScalarAttributeType.N).build();
        KeySchemaElement keySchemaElement = KeySchemaElement.builder().attributeName("orgaNr").keyType(KeyType.HASH).build();

        CreateTableResponse response = dynamoDbClient.createTable(CreateTableRequest.builder()
                .tableName(tableName)
                .attributeDefinitions(attributeDefinition)
                .keySchema(keySchemaElement)
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
                .build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Table created, tableName: {}", tableName);
        } else {
            log.error("Could not create table, name: {}", tableName);
        }
    }

    public void listTables(int limit) {

        ListTablesResponse response = dynamoDbClient.listTables(ListTablesRequest.builder().limit(limit).build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("List tables, limit: {}", limit);
        } else {
            log.error("Could not list tables, limit: {}", limit);
        }
    }

    public void describeTable(String tableName) {

        DescribeTableResponse response = dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName(tableName).build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Describe table created, tableName: {}", tableName);
        } else {
            log.error("Could not describe table, name: {}", tableName);
        }
    }

    public void deleteTable(String tableName) {

        DeleteTableResponse response = dynamoDbClient.deleteTable(DeleteTableRequest.builder().tableName(tableName).build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Table deleted, tableName: {}", tableName);
        } else {
            log.error("Could not delete table, name: {}", tableName);
        }
    }

    public void putItem(String tableName) {

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("orgaNr", AttributeValue.builder().n("1").build());
        PutItemResponse response = dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).item(item).build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Put item tableName: {}", tableName);
        } else {
            log.error("Could not put item table, name: {}", tableName);
        }
    }

    public void getItem(String tableName) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("orgaNr", AttributeValue.builder().n("1").build());
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder().tableName(tableName).key(key).build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Get item tableName: {}", tableName);
        } else {
            log.error("Could not get item table, name: {}", tableName);
        }
        assert (response.item().get("orgaNr").n().equalsIgnoreCase("1"));
    }

    public void scan(String tableName) {

        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder().tableName(tableName).build());

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Get item tableName: {}", tableName);
        } else {
            log.error("Could not get item table, name: {}", tableName);
        }
        assert (response.hasItems());
        assert (response.count() == 1);
    }
}
