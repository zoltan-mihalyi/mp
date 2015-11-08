package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.privilege.PrivilegeAlreadyGrantedException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeNotFoundException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeReuseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PrivilegeStepDefinition {
    private Membership membership;
    private Membership anotherMembership;
    private TImpl privilege;

    @Given("^a membership$")
    public void a_membership() {
        membership = createMembership();
    }

    @When("^a privilege with type T is granted$")
    public void a_privilege_with_type_T_is_granted() {
        privilege = new TImpl();
        membership.grant(T.class, privilege);
    }

    @Then("^getting the privilege with type T results the privilege$")
    public void getting_the_privilege_with_type_T_results_the_privilege() {
        assertNotNull(membership.getPrivilege(T.class));
    }

    @Then("^getting the privilege with type T results an error$")
    public void getting_the_privilege_with_type_T_results_an_error() {
        Helper.verifyException(PrivilegeNotFoundException.class, () -> membership.getPrivilege(T.class));
    }

    @Then("^granting a privilege with type T results an error$")
    public void granting_a_privilege_with_type_T_results_an_error() {
        Helper.verifyException(PrivilegeAlreadyGrantedException.class, () -> membership.grant(T.class, new TImpl()));
    }

    @Then("^checking for privilege T results \"(true|false)\"$")
    public void checking_for_privilege_T_results(boolean hasPrivilege) {
        assertEquals(hasPrivilege, membership.hasPrivilege(T.class));
    }

    @Then("^revoking privilege with type T results an error$")
    public void revoking_privilege_with_type_T_results_an_error() {
        Helper.verifyException(PrivilegeNotFoundException.class, () -> membership.revokePrivilege(T.class));
    }

    @When("^revoking privilege with type T$")
    public void revoking_privilege_with_type_T() {
        membership.revokePrivilege(T.class);
    }

    @Then("^the privilege belongs to the membership$")
    public void the_privilege_belongs_to_the_membership() {
        assertSame(membership, privilege.getMembership());
    }

    @Given("^an another membership$")
    public void an_another_membership() {
        anotherMembership = createMembership();
    }

    @Then("^granting the same privilege to the another membership results an error$")
    public void granting_the_same_privilege_to_the_another_membership_results_an_error() {
        Helper.verifyException(PrivilegeReuseException.class, () -> anotherMembership.grant(T.class, privilege));
    }

    private static Membership createMembership() {
        return new Membership(mock(User.class), mock(Room.class));
    }

    private interface T {
    }

    private class TImpl extends Privilege implements T {
    }
}
