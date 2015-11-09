package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.event.JoinEvent;
import hu.zoltanmihalyi.mp.event.LeaveEvent;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ClientStepDefinition {
    private MyClient client;

    @Given("^a client$")
    public void a_client() {
        client = spy(new MyClient());
    }

    @When("^a room join event is fired$")
    public void a_room_join_event_is_fired() {
        client.onMessage(new JoinEvent(0, "Room1"));
    }

    @Then("^the annotated join method is called$")
    public void the_annotated_join_method_is_called() {
        verify(client).onJoinRoom1();
    }

    @When("^a room leave event is fired$")
    public void a_room_leave_event_is_fired() {
        client.onMessage(new LeaveEvent(0));
    }

    @Then("^the annotated leave method is called$")
    public void the_annotated_leave_method_is_called() {
        verify(client).onLeaveRoom1();
    }

    private static class MyClient extends Client {
        @Event(type = EventType.JOIN, roomName = "Room1")
        public void onJoinRoom1() {

        }

        @Event(type = EventType.LEAVE, roomName = "Room1")
        public void onLeaveRoom1() {

        }
    }
}
