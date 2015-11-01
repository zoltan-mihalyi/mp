package hu.zoltanmihalyi.mp;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.privilege.PrivilegeAlreadyGrantedException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeNotFoundException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeReuseException;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class PrivilegeStepDefinition {
    private Membership membership;
    private Membership anotherMembership;
    private TImpl privilege;

    @Given("^a membership$")
    public void a_membership() {
        membership = new Membership(new User());
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
        catchException(membership).getPrivilege(T.class);
        assertTrue(caughtException() instanceof PrivilegeNotFoundException);
    }

    @Then("^granting a privilege with type T results an error$")
    public void granting_a_privilege_with_type_T_results_an_error() {
        catchException(membership).grant(T.class, new TImpl());
        assertTrue(caughtException() instanceof PrivilegeAlreadyGrantedException);
    }

    @Then("^checking for privilege T results \"(true|false)\"$")
    public void checking_for_privilege_T_results(boolean hasPrivilege) {
        assertEquals(hasPrivilege, membership.hasPrivilege(T.class));
    }

    @Then("^revoking privilege with type T results an error$")
    public void revoking_privilege_with_type_T_results_an_error() {
        catchException(membership).revoke(T.class);
        assertTrue(caughtException() instanceof PrivilegeNotFoundException);
    }

    @When("^revoking privilege with type T$")
    public void revoking_privilege_with_type_T() {
        membership.revoke(T.class);
    }

    @Then("^the privilege belongs to the membership$")
    public void the_privilege_belongs_to_the_membership() {
        assertSame(membership, privilege.getMembership());
    }

    @Given("^an another membership$")
    public void an_another_membership() {
        anotherMembership = new Membership(new User());
    }

    @Then("^granting the same privilege to the another membership results an error$")
    public void granting_the_same_privilege_to_the_another_membership_results_an_error() {
        catchException(anotherMembership).grant(T.class, privilege);
        assertTrue(caughtException() instanceof PrivilegeReuseException);
    }

    private interface T {
    }

    private class TImpl extends Privilege implements T {
    }
}
