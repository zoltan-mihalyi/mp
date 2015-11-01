package hu.zoltanmihalyi.mp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RoomStepDefinitions {
    private Room room;
    private User user;

    @Given("^a room$")
    public void a_room() {
        room = spy(new Room() {
            @Override
            protected void onJoin(Membership membership) {}
        });
    }

    @Given("^a user$")
    public void a_user() {
        user = new User();
    }

    @When("^a user is added to the room$")
    public void a_user_is_added_to_the_room() {
        room.addUser(user);
    }

    @Then("^the room should contain the user$")
    public void the_room_should_contain_the_user() {
        assertTrue(room.contains(user));
    }

    @Then("^the room should not contain the user$")
    public void the_room_should_not_contain_the_user() {
        assertFalse(room.contains(user));
    }

    @Then("^the room notifies the listener about the new membership$")
    public void the_room_notifies_the_listener_about_the_new_membership() {
        verify(room).onJoin(any());
    }
}
