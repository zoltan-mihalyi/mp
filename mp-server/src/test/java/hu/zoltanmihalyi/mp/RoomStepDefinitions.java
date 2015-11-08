package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.exception.UserAlreadyAddedException;
import hu.zoltanmihalyi.mp.exception.UserNotFoundException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

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
    @SuppressWarnings("unchecked")
    public void a_user() {
        user = new User(mock(Channel.class));
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
    @SuppressWarnings("unchecked")
    public void another_user_is_added_to_the_room() {
        anotherUser = new User(mock(Channel.class));
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
    public void the_number_of_users_in_the_room_should_be(int size) {
        assertEquals(size, room.getUsersCount());
    }

    @Then("^getting the membership of the user in the room results the membership$")
    public void getting_the_membership_of_the_user_in_the_room_results_the_membership() {
        assertSame(membership, room.getMembershipOf(user));
    }

    @Then("^getting the membership of the user in the room results an error$")
    public void getting_the_membership_of_the_user_in_the_room_results_an_error() {
        Helper.verifyException(UserNotFoundException.class, () -> room.getMembershipOf(user));
    }

    @Then("^adding the user to the room again results an exception$")
    public void adding_the_user_to_the_room_again_results_an_exception() {
        Helper.verifyException(UserAlreadyAddedException.class, () -> room.addUser(user));
    }
}
