package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.event.ServerEvent;

import static org.mockito.Mockito.*;

public class ServerStepDefinitions {
    private Server server;
    private Channel<ServerEvent> channel;
    private User user;

    @Given("^a server$")
    public void a_server() {
        server = spy(new Server() {
            @Override
            protected void onConnect(User user) {
                ServerStepDefinitions.this.user = user;
            }
        });
    }

    @Given("^a channel$")
    @SuppressWarnings("unchecked")
    public void a_channel() {
        channel = mock(Channel.class);
    }

    @When("^the channel is added to the server$")
    public void the_channel_is_added_to_the_server() {
        server.accept(channel);
    }

    @Then("^the server notifies the listener about the user$")
    public void the_server_notifies_the_listener_about_the_user() {
        verify(server).onConnect(any());
    }

    @When("^the user of the channel is added to a room$")
    public void the_user_of_the_channel_is_added_to_a_room() {
        Room room = new Room() {
            @Override
            protected void onJoin(Membership membership) {

            }
        };
        room.addUser(user);
    }

    @Then("^the channel should be notified about the join event$")
    public void the_channel_should_be_notified_about_the_join_event() {
        verify(channel).onMessage(any());
    }
}
