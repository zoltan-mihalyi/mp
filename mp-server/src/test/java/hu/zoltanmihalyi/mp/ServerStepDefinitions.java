package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hu.zoltanmihalyi.mp.event.ServerEvent;

import static org.mockito.Mockito.*;

public class ServerStepDefinitions {
    private Server server;
    private Channel<ServerEvent> channel;

    @Given("^a server$")
    public void a_server() {
        server = spy(new Server() {
            @Override
            protected void onConnect(User user) {
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
}
