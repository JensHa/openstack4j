package org.openstack4j.api.identity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openstack4j.api.AbstractTest;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.identity.Role;
import org.testng.annotations.Test;

public class KeystoneRoleServiceTests extends AbstractTest {

    private static final String JSON_ROLES_EMPTY = "/identity/roles_empty.json";
    private static final String JSON_ROLES_ONE_ENTRY = "/identity/roles_one_entry.json";
    private static final String JSON_ROLES_MULTIPLE_ENTRIES = "/identity/roles_multiple_entries.json";
    private static final String JSON_ROLES_LIST = "/identity/roles_list.json";
    private static final String ROLE_NAME = "admin";
    private static final String USER_ID = "aa9f25defa6d4cafb48466df83106065";
    private static final String PROJECT_ID = "123ac695d4db400a9001b91bb3b8aa46";
    private static final String ROLE_ID = "aae88952465d4c32b0a1140a76601b68";
    private static final String USER_DOMAIN_ID = "default";

    @Override
    protected Service service() {
        return Service.IDENTITY;
    }

 // ------------ Role Tests ------------

    @Test(expectedExceptions = NullPointerException.class)
    public void roles_getByName_with_null_throws_NullPointerException() {
        os().identity().roles().getByName(null);
    }

    public void roles_empty_list_when_none_found() throws Exception {
        respondWith(JSON_ROLES_EMPTY);
        List<? extends Role> list = os().identity().roles().getByName(ROLE_NAME);
        assertTrue(list.isEmpty());
    }

    public void roles_one_entry_in_list_when_one_returned() throws Exception {
        respondWith(JSON_ROLES_ONE_ENTRY);
        List<? extends Role> list = os().identity().roles().getByName(ROLE_NAME);
        assertTrue(list.size()==1);
    }

    public void roles_list_with_multiple_entries_for_list_of_roles() throws Exception {
        respondWith(JSON_ROLES_MULTIPLE_ENTRIES);
        List<? extends Role> list = os().identity().roles().getByName(ROLE_NAME);
        assertTrue(list.size() > 1);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void addRoletoUserInProject_projectIdMustBeNonNull() throws Exception {
        os().identity().roles().grantProjectUserRole(null, "fake", "fake");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void addRoletoUserInProject_userIdMustBeNonNull() throws Exception {
        os().identity().roles().grantProjectUserRole("fake", null, "fake");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void addRoletoUserInProject_roleIdMustBeNonNull() throws Exception {
        os().identity().roles().grantProjectUserRole("fake", "fake", null);
    }

    public void addRoleToUserInProject_check_correct_response() throws Exception {
        respondWith(204);
        ActionResponse actionResponse = os().
                identity().
                roles().
                grantProjectUserRole("fake", "fake", "fake");
        assertTrue(actionResponse.isSuccess());
    }

    public void list_roles() throws Exception {
        respondWith(JSON_ROLES_LIST);
        List<? extends Role> list = os().identity().roles().list();
        assertEquals(list.size(), 6);
    }

    /**
     * checks if a user has a role in project context
     *
     * @throws Exception
     */
    public void checkProjectUserRole_success_Test() throws Exception {

        respondWith(204);

        ActionResponse response_success = os().identity().roles().checkProjectUserRole(PROJECT_ID, USER_ID, ROLE_ID);
        assertTrue(response_success.isSuccess());
    }

    /**
     * grants and revokes a role to/from a user in project context
     *
     * @throws Exception
     */
    public void grantRevokeProjectUserRole_Test() throws Exception {

        respondWith(204);

        ActionResponse result_grant = os().identity().roles().grantProjectUserRole(PROJECT_ID, USER_ID, ROLE_ID);
        assertTrue(result_grant.isSuccess());

        respondWith(204);

        ActionResponse result_revoke = os().identity().roles().revokeProjectUserRole(PROJECT_ID, USER_ID, ROLE_ID);
        assertTrue(result_revoke.isSuccess());

    }

    /**
     * checks if a user has a role in domain context
     *
     * @throws Exception
     */
    public void checkDomainUserRole_success_Test() throws Exception {

        respondWith(204);

        ActionResponse response_success = os().identity().roles().checkDomainUserRole(USER_DOMAIN_ID, USER_ID, ROLE_ID);
        assertTrue(response_success.isSuccess());
    }

    /**
     * grants and revokes a role to/from a user in domain context
     *
     * @throws Exception
     */
    public void grantRevokeDomainUserRole_Test() throws Exception {

        respondWith(204);

        ActionResponse result_grant = os().identity().roles().grantDomainUserRole(USER_DOMAIN_ID, USER_ID, ROLE_ID);
        assertTrue(result_grant.isSuccess());

        respondWith(204);

        ActionResponse result_revoke = os().identity().roles().revokeDomainUserRole(USER_DOMAIN_ID, USER_ID, ROLE_ID);
        assertTrue(result_revoke.isSuccess());

    }


}
