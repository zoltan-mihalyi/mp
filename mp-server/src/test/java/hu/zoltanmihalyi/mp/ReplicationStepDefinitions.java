package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.replication.Replicator;
import hu.zoltanmihalyi.mp.event.ReplicationEvent;
import hu.zoltanmihalyi.mp.event.ServerEvent;
import hu.zoltanmihalyi.mp.replication.BruteForceReplicator;
import lombok.*;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class ReplicationStepDefinitions {
    private State state = new State("test");
    private Room room;
    private Map<User, Channel<ServerEvent>> users = new HashMap<>();
    private BruteForceReplicator<State> replicator;
    private BruteForceReplicator<State> replicator2;
    private State data;


    @Given("^a room for replication")
    public void a_room_for_replication() {
        room = new Room() {
            @Override
            protected void onJoin(Membership membership) {
            }
        };
    }

    @Given("^a membership in the room with a replicator$")
    public void a_membership_in_the_room_with_a_replicator() {
        addUserWithReplicator(createBruteForceReplicator());
    }

    private BruteForceReplicator<State> createBruteForceReplicator() {
        return new BruteForceReplicator<>(state, (State state1) -> new State(state1.data));
    }

    private void addUserWithReplicator(Replicator replicator) {
        @SuppressWarnings("unchecked") Channel<ServerEvent> channel = mock(Channel.class);
        User user = new User(channel);
        Membership membership = room.addUser(user);
        membership.setReplicator(replicator);
        users.put(user, channel);
    }

    @When("^calling update on the room$")
    public void calling_update_on_the_room() {
        room.update();
    }

    @Then("^every user \\((\\d*)\\) was notified$")
    public void every_user_was_notified(int num) {
        assertEquals(num, users.size());
        for (Channel<ServerEvent> channel : users.values()) {
            verify(channel, times(1)).onMessage(isA(ReplicationEvent.class));
        }
    }

    @Given("^a brute force replicator$")
    public void a_brute_force_replicator() {
        replicator = createBruteForceReplicator();
    }

    @When("^getting replication data$")
    public void getting_replication_data() {
        data = replicator.getData();
    }

    @Then("^the data should contain the state$")
    public void the_data_should_contain_the_state() {
        assertEquals(state, data);
    }

    @Then("^no data should be returned$")
    public void no_data_should_be_returned() {
        assertNull(data);
    }

    @And("^the state is changed$")
    public void the_state_is_changed() {
        state.setData("test2");
    }

    @And("^a membership in the room with the brute force replicator$")
    public void a_membership_in_the_room_with_the_brute_force_replicator() {
        addUserWithReplicator(replicator);
    }

    @And("^the replication data is the same$")
    public void the_replication_data_is_the_same() {
        verifyDifferentReplicationDataCount(1);
    }

    @And("^an other brute force replicator$")
    public void an_other_brute_force_replicator() {
        replicator2 = createBruteForceReplicator();
    }

    @And("^a membership in the room with the second brute force replicator$")
    public void a_membership_in_the_room_with_the_second_brute_force_replicator() {
        addUserWithReplicator(replicator2);
    }

    @And("^the replication data is only same for the first two users$")
    public void the_replication_data_is_only_same_for_the_first_two_users() {
        verifyDifferentReplicationDataCount(2);
    }

    private void verifyDifferentReplicationDataCount(int count) {
        Map<Object, Object> replicationDataObjects = new IdentityHashMap<>();
        for (Channel<ServerEvent> channel : users.values()) {
            ArgumentCaptor<ReplicationEvent> captor = ArgumentCaptor.forClass(ReplicationEvent.class);
            verify(channel, atLeastOnce()).onMessage(captor.capture());
            Object data = captor.getValue().getData();
            replicationDataObjects.put(data, null);
        }
        assertEquals(count, replicationDataObjects.size());
    }


    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class State {
        private String data;
    }
}
