package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.event.ReplicationEvent;
import hu.zoltanmihalyi.mp.event.ServerEvent;
import hu.zoltanmihalyi.mp.replication.BruteForceReplicator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ReplicationStepDefinitions {
    private Membership membership;
    private State state;
    private ServerEvent event;

    @Given("^a user added to a room$")
    public void a_membership_for_replication() {
        User user = new User(new Channel<ServerEvent>() {
            @Override
            public void onMessage(ServerEvent message) {
                event = message;
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onError(Exception e) {
            }
        });
        membership = new Room() {
            @Override
            protected void onJoin(Membership membership) {
            }
        }.addUser(user);
    }

    @Given("^a replicator is set for the membership$")
    public void a_replicator_is_set_for_the_membership() {
        state = new State("test");
        membership.setReplicator(new BruteForceReplicator<>(state, (State state) -> new State(state.data)));
    }

    @When("^an update is called on the membership$")
    public void an_update_is_called_on_the_membership() {
        membership.update();
    }

    @And("^another update is called on the membership$")
    public void another_update_is_called_on_the_membership() {
        event = null;
        membership.update();
    }

    @Then("^the user should receive the replication event$")
    public void the_user_should_receive_the_replication_event() {
        assertNotNull(event);
        assertTrue(event instanceof ReplicationEvent);
    }

    @Then("^the replication event should contain the state$")
    public void the_replication_event_should_contain_the_state() {
        ReplicationEvent replicationEvent = (ReplicationEvent) event;
        assertEquals(state, replicationEvent.getData());
    }

    @Then("^no replication event should be received$")
    public void no_replication_event_should_be_received() {
        assertNull(event);
    }

    @And("^the state is changed$")
    public void the_state_is_changed() {
        state.setData("test2");
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class State {
        private String data;
    }
}
