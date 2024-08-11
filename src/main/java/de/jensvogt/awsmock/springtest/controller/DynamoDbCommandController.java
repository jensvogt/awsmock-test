package de.jensvogt.awsmock.springtest.controller;

import de.jensvogt.awsmock.springtest.service.DynamodbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/dynamodb", produces = MediaType.APPLICATION_JSON_VALUE)
public class DynamoDbCommandController {

    private final DynamodbService dynamodbService;

    @PostMapping(path = "/createTable", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createTable(@RequestParam("tableName") String tableName) {

        log.info("POST request, createTable, tableName: {}", tableName);
        dynamodbService.createTable(tableName);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/describeTable", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> describeTable(@RequestParam("tableName") String tableName) {

        log.info("GET request, describeTable, tableName: {}", tableName);
        dynamodbService.describeTable(tableName);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/listTables", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> listTables(@RequestParam("limit") int limit) {

        log.info("GET request, listTables, limit: {}", limit);
        dynamodbService.listTables(limit);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteTable", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteTables(@RequestParam("tableName") String tableName) {

        log.info("DELETE request, deleteTables, tableName: {}", tableName);
        dynamodbService.deleteTable(tableName);

        return ResponseEntity.ok().build();
    }
}
