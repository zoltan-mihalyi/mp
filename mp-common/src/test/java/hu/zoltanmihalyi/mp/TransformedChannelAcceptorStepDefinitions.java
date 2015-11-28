package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransformedChannelAcceptorStepDefinitions {
    private ChannelAcceptor<Integer, Integer> channelAcceptor;
    private TransformedChannelAcceptor<String, String, Integer, Integer> transformedChannelAcceptor;
    private Channel<String> resultChannel;
    private Channel<Integer> receivedChannel;
    private Channel<Integer> originalResultChannel;
    private Channel<String> originalChannel;

    @Given("^a channel acceptor$")
    public void a_channel_acceptor() {
        channelAcceptor = spy(new MyChannelAcceptor());
    }

    @Given("^a transformed channel acceptor for the channel acceptor$")
    public void a_transformed_channel_acceptor_for_the_channel_acceptor() {
        Converter<String, Integer> toInteger = new SynchronousConverter<String, Integer>() {
            @Override
            public Integer convert(String s) throws ConversionFailureException {
                return Integer.parseInt(s);
            }
        };

        Converter<Object, String> toString = new SynchronousConverter<Object, String>() {
            @Override
            public String convert(Object o) throws ConversionFailureException {
                return o.toString();
            }
        };

        transformedChannelAcceptor = new TransformedChannelAcceptor<>(channelAcceptor, toString, toInteger);
    }

    @SuppressWarnings("unchecked")
    @When("^the transformed channel acceptor receives a connection$")
    public void the_transformed_channel_acceptor_receives_a_connection() {
        originalChannel = mock(Channel.class);
        resultChannel = transformedChannelAcceptor.accept(originalChannel);
    }

    @Then("^the channel acceptor should receive a connection$")
    public void the_channel_acceptor_should_receive_a_connection() {
        verify(channelAcceptor).accept(any());
    }

    @When("^a message is sent to the received channel$")
    public void a_message_is_sent_to_the_received_channel() {
        receivedChannel.onMessage(42);
    }

    @Then("^the original channel should receive the transformed message$")
    public void the_original_channel_should_receive_the_transformed_message() {
        verify(originalChannel).onMessage("42");
    }

    @When("^a message is sent to the result channel$")
    public void a_message_is_sent_to_the_result_channel() {
        resultChannel.onMessage("42");
    }

    @Then("^the original result channel should receive the transformed message$")
    public void the_original_result_channel_should_receive_the_transformed_message() {
        verify(originalResultChannel).onMessage(42);
    }

    private class MyChannelAcceptor implements ChannelAcceptor<Integer, Integer> {
        @Override
        @SuppressWarnings("unchecked")
        public Channel<Integer> accept(Channel<Integer> channel) {
            receivedChannel = channel;
            originalResultChannel = mock(Channel.class);
            return originalResultChannel;
        }
    }
}
