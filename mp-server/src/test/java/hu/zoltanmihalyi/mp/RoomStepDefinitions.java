package hu.zoltanmihalyi.mp;

import static org.junit.Assert.*;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RoomStepDefinitions {
    private Room room;
    private User user;
    private User anotherUser;
    private Membership membership;

    @Given("^a room$")
    public void a_room() {
        room = spy(new Room() {
            @Override
            protected void onJoin(Membership membership) {
                RoomStepDefinitions.this.membership = membership;
            }
        });
    }

    @Given("^a user$")
    public void a_user() {
        user = new User();
    }

    @When("^the user is added to the room$")
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
        verify(room).onJoin(notNull(Membership.class));
    }

    @And("^the membership belongs to the user$")
    public void the_membership_belongs_to_the_user() {
        assertSame(user, membership.getUser());
    }

    @When("^the user is removed from the room$")
    public void the_user_is_removed_from_the_room() {
        room.removeUser(user);
    }

    @When("^the membership is revoked$")
    public void the_membership_is_revoked() {
        membership.revoke();
    }

    @And("^another user is added to the room$")
    public void another_user_is_added_to_the_room() {
        anotherUser = new User();
        room.addUser(anotherUser);
    }

    @Then("^getting the users should result the user and another user$")
    public void getting_the_users_should_result_the_user_and_another_user() {
        Set<User> expected = new HashSet<>();
        expected.add(user);
        expected.add(anotherUser);

        assertEquals(expected, new HashSet<>(room.getUsers()));
    }

    @And("^the number of users in the room should be (\\d+)$")
    public void the_number_of_users_in_the_room_should_be(int size) throws Throwable {
        assertEquals(size, room.getUsersCount());
    }
}
