package de.jensvogt.awsmock.springtest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public void createUserPool(String userPoolName) {

        CreateUserPoolResponse response = cognitoIdentityProviderClient.createUserPool(CreateUserPoolRequest.builder().poolName(userPoolName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User pool created, name: {}", userPoolName);
        } else {
            log.error("Could not create user pool, name: {}", userPoolName);
        }
    }

    public void listUserPools(int maxResults) {

        ListUserPoolsResponse response = cognitoIdentityProviderClient.listUserPools(ListUserPoolsRequest.builder().maxResults(maxResults).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("List user pools");
        } else {
            log.error("Could not list user pools");
        }
    }

    public void deleteUserPool(String userPoolId) {

        DeleteUserPoolResponse response = cognitoIdentityProviderClient.deleteUserPool(DeleteUserPoolRequest.builder().userPoolId(userPoolId).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User pool deleted, userPoolId: {}", userPoolId);
        } else {
            log.error("Could not delete user pool, userPoolId: {}", userPoolId);
        }
    }

    public void createUser(String userPoolId, String userName) {

        AdminCreateUserResponse response = cognitoIdentityProviderClient.adminCreateUser(AdminCreateUserRequest.builder().userPoolId(userPoolId).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User created, userPoolId: {}, userName: {}", userPoolId, userName);
        } else {
            log.error("Could not create user, userPoolId: {}, userName: {}", userPoolId, userName);
        }
    }

    public void deleteUser(String userPoolId, String userName) {

        AdminDeleteUserResponse response = cognitoIdentityProviderClient.adminDeleteUser(AdminDeleteUserRequest.builder().userPoolId(userPoolId).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User deleted, userPoolId: {}, userName: {}", userPoolId, userName);
        } else {
            log.error("Could not delete user, userPoolId: {}, userName: {}", userPoolId, userName);
        }
    }
}
