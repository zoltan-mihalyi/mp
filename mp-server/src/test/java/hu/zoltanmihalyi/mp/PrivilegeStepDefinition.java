package hu.zoltanmihalyi.mp;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.privilege.PrivilegeAlreadyGrantedException;
import hu.zoltanmihalyi.mp.privilege.PrivilegeNotFoundException;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class PrivilegeStepDefinition {
    private Membership membership;

    @Given("^a membership$")
    public void a_membership() {
        membership = new Membership();
    }

    @When("^a privilege with type T is granted$")
    public void a_privilege_with_type_T_is_granted() {
        membership.grant(T.class, new T() {});
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
        catchException(membership).grant(T.class, new T() {});
        assertTrue(caughtException() instanceof PrivilegeAlreadyGrantedException);
    }

    @Then("^checking for privilege T results \"(true|false)\"$")
    public void checking_for_privilege_T_results(boolean hasPrivilege)  {
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

    private interface T {

    }
}