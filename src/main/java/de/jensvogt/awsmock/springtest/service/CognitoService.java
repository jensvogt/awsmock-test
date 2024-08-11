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

    public void describeUserPool(String userPoolId) {

        DescribeUserPoolResponse response = cognitoIdentityProviderClient.describeUserPool(DescribeUserPoolRequest.builder().userPoolId(userPoolId).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Describe user pool, userPoolId: {}", userPoolId);
        } else {
            log.error("Could not describe user pool, userPoolId: {}", userPoolId);
        }
    }

    public void createUserPoolClient(String userPoolId, String clientName) {

        TokenValidityUnitsType tokenValidityUnitsType = TokenValidityUnitsType.builder()
                .accessToken(TimeUnitsType.DAYS)
                .idToken(TimeUnitsType.DAYS)
                .refreshToken(TimeUnitsType.DAYS)
                .build();
        CreateUserPoolClientResponse response = cognitoIdentityProviderClient.createUserPoolClient(CreateUserPoolClientRequest.builder().userPoolId(userPoolId).clientName(clientName).accessTokenValidity(1).tokenValidityUnits(tokenValidityUnitsType).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User pool client created, userPoolId: {}, clientName: {}", userPoolId, clientName);
        } else {
            log.error("Could not create user pool client, userPoolId: {}, clientName: {}", userPoolId, clientName);
        }
    }

    public void createUserPoolDomain(String userPoolId, String domainName) {

        CreateUserPoolDomainResponse response = cognitoIdentityProviderClient.createUserPoolDomain(CreateUserPoolDomainRequest.builder().userPoolId(userPoolId).domain(domainName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User pool domain created, userPoolId: {}, clientName: {}", userPoolId, domainName);
        } else {
            log.error("Could not create user pool domain, userPoolId: {}, clientName: {}", userPoolId, domainName);
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

    public void enableUser(String userPoolId, String userName) {

        AdminEnableUserResponse response = cognitoIdentityProviderClient.adminEnableUser(AdminEnableUserRequest.builder().userPoolId(userPoolId).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User enabled, userPoolId: {}, userName: {}", userPoolId, userName);
        } else {
            log.error("Could not enable user, userPoolId: {}, userName: {}", userPoolId, userName);
        }
    }

    public void disableUser(String userPoolId, String userName) {

        AdminDisableUserResponse response = cognitoIdentityProviderClient.adminDisableUser(AdminDisableUserRequest.builder().userPoolId(userPoolId).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User disabled, userPoolId: {}, userName: {}", userPoolId, userName);
        } else {
            log.error("Could not disable user, userPoolId: {}, userName: {}", userPoolId, userName);
        }
    }

    public String signupUser(String userName, String clientId, String password) {

        SignUpResponse response = cognitoIdentityProviderClient.signUp(SignUpRequest.builder().username(userName).clientId(clientId).password(password).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User sign up, userName: {}, clientId: {}", userName, clientId);
            return response.userSub();
        } else {
            log.error("Could not sign up, userName: {}, clientId: {}", userName, clientId);
        }
        return "";
    }

    public void deleteUser(String userPoolId, String userName) {

        AdminDeleteUserResponse response = cognitoIdentityProviderClient.adminDeleteUser(AdminDeleteUserRequest.builder().userPoolId(userPoolId).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User deleted, userPoolId: {}, userName: {}", userPoolId, userName);
        } else {
            log.error("Could not delete user, userPoolId: {}, userName: {}", userPoolId, userName);
        }
    }

    public void createUserGroup(String userPoolId, String groupName) {

        CreateGroupResponse response = cognitoIdentityProviderClient.createGroup(CreateGroupRequest.builder().userPoolId(userPoolId).groupName(groupName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User group created, userPoolId: {}, groupName: {}", userPoolId, groupName);
        } else {
            log.error("Could not create user group, userPoolId: {}, groupName: {}", userPoolId, groupName);
        }
    }

    public void addUserToGroup(String userPoolId, String groupName, String userName) {

        AdminAddUserToGroupResponse response = cognitoIdentityProviderClient.adminAddUserToGroup(AdminAddUserToGroupRequest.builder().userPoolId(userPoolId).groupName(groupName).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Added user to group, userPoolId: {}, groupName: {}, userName: {}", userPoolId, groupName, userName);
        } else {
            log.error("Could not add user to group, userPoolId: {}, groupName: {}, userName: {}", userPoolId, groupName, userName);
        }
    }

    public int listUsersInGroup(String userPoolId, String groupName) {

        ListUsersInGroupResponse response = cognitoIdentityProviderClient.listUsersInGroup(ListUsersInGroupRequest.builder().userPoolId(userPoolId).groupName(groupName).limit(10).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("List users in group, userPoolId: {}, groupName: {}, count: {}", userPoolId, groupName, response.users().size());
            return response.users().size();
        } else {
            log.error("Could not list users in group, userPoolId: {}, groupName: {}", userPoolId, groupName);
        }
        return 0;
    }

    public void removeUserFromGroup(String userPoolId, String groupName, String userName) {

        AdminRemoveUserFromGroupResponse response = cognitoIdentityProviderClient.adminRemoveUserFromGroup(AdminRemoveUserFromGroupRequest.builder().userPoolId(userPoolId).groupName(groupName).username(userName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("Remove user from group, userPoolId: {}, groupName: {}, userName: {}", userPoolId, groupName, userName);
        } else {
            log.error("Could not remove user from group, userPoolId: {}, groupName: {}, userName: {}", userPoolId, groupName, userName);
        }
    }

    public void deleteUserGroup(String userPoolId, String groupName) {

        DeleteGroupResponse response = cognitoIdentityProviderClient.deleteGroup(DeleteGroupRequest.builder().userPoolId(userPoolId).groupName(groupName).build());
        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("User group deleted, userPoolId: {}, groupName: {}", userPoolId, groupName);
        } else {
            log.error("Could not delete user group, userPoolId: {}, groupName: {}", userPoolId, groupName);
        }
    }
}
