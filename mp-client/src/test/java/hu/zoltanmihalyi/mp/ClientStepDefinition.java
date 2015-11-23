package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.event.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

public class ClientStepDefinition {
    private MyClient client;
    private Channel<ServerEvent> serverEventChannel;
    private RemoteRoom remoteRoom;
    private Channel<ClientEvent> targetChannel;
    private ClientEvent event;

    @Given("^a client with a connection$")
    public void a_client_with_a_connection() {
        a_client();
        @SuppressWarnings("unchecked")
        Channel<ClientEvent> channel = mock(Channel.class);
        serverEventChannel = client.accept(channel);
    }

    @When("^a room join event is fired$")
    public void a_room_join_event_is_fired() {
        serverEventChannel.onMessage(new JoinEvent(0, "Room1"));
    }

    @Then("^the annotated join method is called$")
    public void the_annotated_join_method_is_called() {
        verify(client).onJoinRoom1();
    }

    @When("^a room leave event is fired$")
    public void a_room_leave_event_is_fired() {
        serverEventChannel.onMessage(new LeaveEvent(0));
    }

    @Then("^the annotated leave method is called$")
    public void the_annotated_leave_method_is_called() {
        verify(client).onLeaveRoom1();
    }

    @And("^the annotated join method with RemoteRoom parameter is called$")
    public void the_annotated_join_method_with_remote_room_parameter_is_called() {
        verify(client).onJoinRoom1(isA(RemoteRoom.class));
    }

    @Given("^a target channel set to the client$")
    @SuppressWarnings("unchecked")
    public void a_target_channel_set_to_the_client() {
        targetChannel = spy(new Channel<ClientEvent>() {
            @Override
            public void onMessage(ClientEvent message) {
                event = message;
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onError(Exception e) {
            }
        });
        client.accept(targetChannel);
    }

    @When("^a privilege method is called on the RemoteRoom$")
    public void a_privilege_method_is_called_on_the_remote_room() {
        remoteRoom.getPrivilege(MyPrivilege.class).doSomething();
    }

    @Then("^the client should send an invocation event$")
    public void the_client_should_send_an_invocation_event() {
        verify(targetChannel).onMessage(isA(InvocationEvent.class));
    }

    @And("^the event should contain the correct method and parameters$")
    public void the_event_should_contain_the_correct_class_method_and_parameters() throws NoSuchMethodException {
        InvocationEvent invocationEvent = (InvocationEvent) event;
        assertEquals(MyPrivilege.class.getMethod("doSomething"), invocationEvent.getMethod());
        assertEquals(0, invocationEvent.getArgumentsNumber());
    }

    @Given("^a client$")
    public void a_client() {
        client = spy(new MyClient());
    }

    @Then("^adding a null reference to the client as target channel causes an exception$")
    public void adding_a_null_reference_to_the_client_as_target_channel_causes_an_exception() {
        Helper.verifyException(NullPointerException.class, () -> client.accept(null));
    }

    @SuppressWarnings("unchecked")
    @Then("^adding a target channel again causes an exception$")
    public void adding_a_target_channel_again_causes_an_exception() {
        Helper.verifyException(IllegalStateException.class, () -> client.accept(mock(Channel.class)));
    }

    private class MyClient extends Client {
        @Event(type = EventType.JOIN, roomName = "Room1")
        public void onJoinRoom1() {

        }

        @Event(type = EventType.JOIN, roomName = "Room1")
        public void onJoinRoom1(RemoteRoom room) {
            remoteRoom = room;
        }

        @Event(type = EventType.LEAVE, roomName = "Room1")
        public void onLeaveRoom1() {

        }
    }

    private interface MyPrivilege {
        void doSomething();
    }
}
