package de.jensvogt.awsmock.springtest.controller;

import de.jensvogt.awsmock.springtest.service.CognitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/cognito", produces = MediaType.APPLICATION_JSON_VALUE)
public class CognitoCommandController {

    private final CognitoService cognitoService;

    @PostMapping(path = "/createUserPool", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createUserPool(@RequestParam("name") String name) {

        log.info("POST request, createUserPool, name: {}", name);
        cognitoService.createUserPool(name);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/listUserPools", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> listUserPools(@RequestParam("maxResults") int maxResults) {

        log.info("GET request, listUserPools, maxResults: {}", maxResults);
        cognitoService.listUserPools(maxResults);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/describeUserPool", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> describeUserPool(@RequestParam("userPoolId") String userPoolId) {

        log.info("GET request, describeUserPool, userPoolId: {}", userPoolId);
        cognitoService.describeUserPool(userPoolId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteUserPool", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUserPool(@RequestParam("userPoolId") String userPoolId) {

        log.info("DELETE request, deleteUserPool, userPoolId: {}", userPoolId);
        cognitoService.deleteUserPool(userPoolId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createUser(@RequestParam("userPoolId") String userPoolId, @RequestParam("userName") String userName) {

        log.info("POST request, createUser, userPoolId: {}, userName: {}", userPoolId, userName);
        cognitoService.createUser(userPoolId, userName);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUser(@RequestParam("userPoolId") String userPoolId, @RequestParam("userName") String userName) {

        log.info("DELETE request, deleteUser, userPoolId: {}, userName: {}", userPoolId, userName);
        cognitoService.deleteUser(userPoolId, userName);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/createUserGroup", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createUserGroup(@RequestParam("userPoolId") String userPoolId, @RequestParam("groupName") String groupName) {

        log.info("POST request, createUserGroup, userPoolId: {}, groupName: {}", userPoolId, groupName);
        cognitoService.createUserGroup(userPoolId, groupName);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/addUserToGroup", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> addUserToGroup(@RequestParam("userPoolId") String userPoolId, @RequestParam("groupName") String groupName, @RequestParam("userName") String userName) {

        log.info("POST request, addUserToGroup, userPoolId: {}, groupName: {}, userName: {}", userPoolId, groupName, userName);
        cognitoService.addUserToGroup(userPoolId, groupName, userName);

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/removeUserFromGroup", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> removeUserFromGroup(@RequestParam("userPoolId") String userPoolId, @RequestParam("groupName") String groupName, @RequestParam("userName") String userName) {

        log.info("POST request, removeUserFromGroup, userPoolId: {}, groupName: {}, userName: {}", userPoolId, groupName, userName);
        cognitoService.removeUserFromGroup(userPoolId, groupName, userName);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteUserGroup", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteUserGroup(@RequestParam("userPoolId") String userPoolId, @RequestParam("groupName") String groupName) {

        log.info("DELETE request, deleteUserGroup, userPoolId: {}, groupName: {}", userPoolId, groupName);
        cognitoService.deleteUserGroup(userPoolId, groupName);

        return ResponseEntity.ok().build();
    }
}
