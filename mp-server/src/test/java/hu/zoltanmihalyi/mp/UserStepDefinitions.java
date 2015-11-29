package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.event.InvocationEvent;
import hu.zoltanmihalyi.mp.event.ServerEvent;
import hu.zoltanmihalyi.mp.exception.MembershipNotFoundException;
import hu.zoltanmihalyi.mp.exception.PrivilegeNotFoundException;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class UserStepDefinitions {
    private Channel<ServerEvent> channel;
    private User user;
    private Membership membership;
    private MyPrivilegeImpl privilege;
    private int roomId;

    @SuppressWarnings("unchecked")
    @Given("^a channel for server events$")
    public void a_channel_for_server_events() {
        channel = new Channel<ServerEvent>() {
            @Override
            public void onMessage(ServerEvent message) {
                roomId = message.getRoomId();
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onError(Exception e) {
            }
        };
    }

    @Given("^a user for the channel$")
    public void a_user() {
        user = new User(channel);
    }

    @Given("^the user is added to a room$")
    public void the_user_is_added_to_a_room() {
        new Room() {
            @Override
            protected void onJoin(Membership membership) {
                UserStepDefinitions.this.membership = membership;
            }
        }.addUser(user);
    }

    @And("^a privilege is added to the membership$")
    public void a_privilege_is_added_to_the_membership() {
        privilege = spy(new MyPrivilegeImpl());
        membership.grant(MyPrivilege.class, privilege);
    }

    @When("^the privilege is marked not active$")
    public void a_privilege_is_marked_not_active() {
        privilege.setActive(false);
    }

    @When("^an invocation event is passed to the user's channel$")
    public void an_invocation_event_is_passed_to_the_users_channel() throws NoSuchMethodException {
        user.onMessage(new InvocationEvent(roomId, MyPrivilege.class.getMethod("doSomething"), new Object[0]));
    }

    @Then("^the privilege method should be called$")
    public void the_privilege_method_should_be_called() {
        verify(privilege).doSomething();
    }

    @Then("^an exception should be thrown$")
    public void an_exception_should_be_thrown() {

    }

    @Then("^passing an invocation event to the user's channel results a MembershipNotFound exception$")
    public void passing_an_invocation_event_to_the_user_s_channel_results_a_membership_not_found_exception() {
        Helper.verifyException(MembershipNotFoundException.class, this::an_invocation_event_is_passed_to_the_users_channel);
    }

    @Then("^passing an invocation event to the user's channel results a PrivilegeNotFound exception$")
    public void passing_an_invocation_event_to_the_user_s_channel_results_a_privilege_not_found_exception() {
        Helper.verifyException(PrivilegeNotFoundException.class, this::an_invocation_event_is_passed_to_the_users_channel);
    }

    private interface MyPrivilege {
        void doSomething();
    }

    private class MyPrivilegeImpl extends Privilege implements MyPrivilege {
        @Override
        public void doSomething() {
        }
    }
}
