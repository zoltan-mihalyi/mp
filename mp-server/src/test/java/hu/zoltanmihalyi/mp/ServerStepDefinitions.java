package hu.zoltanmihalyi.mp;

import static org.mockito.Mockito.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ServerStepDefinitions {
    private Server server;
    private Channel<?> channel;

    @Given("^a server$")
    public void a_server() {
        server = spy(new Server() {
            @Override
            protected void onConnect(User user) {}
        });
    }

    @Given("^a channel$")
    public void a_channel() {
        channel = mock(Channel.class);
    }

    @When("^a new channel is added to the server$")
    public void a_new_channel_is_added_to_the_server() {
        server.accept(channel);
    }

    @Then("^the server notifies the listener about the user$")
    public void the_server_notifies_the_listener_about_the_user() {
        verify(server).onConnect(any());
    }
}
