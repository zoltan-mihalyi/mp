package hu.zoltanmihalyi.mp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TransformedChannelStepDefinitions {
    private Channel<Object> transformedChannel;
    Channel<String> targetChannel;

    @SuppressWarnings("unchecked")
    @Given("^a transformed channel with a converter which converts integers to string$")
    public void a_transformed_channel_with_a_converter_which_converts_integers_to_string()
            throws Throwable {
        targetChannel = mock(Channel.class);
        transformedChannel = new TransformedChannel<>(targetChannel, new ToStringConverter());
    }

    @When("^a message is sent to the transformed channel with value (\\d+)$")
    public void a_message_is_sent_to_the_transformed_channel(int value) throws Throwable {
        transformedChannel.onMessage(value);
    }

    @Then("^the target channel receives the message \"(.*)\"$")
    public void the_target_channel_receives_the_message(String message) throws Throwable {
        verify(targetChannel).onMessage(message);
    }

    @SuppressWarnings("unchecked")
    @Given("^a transformed channel$")
    public void a_transformed_channel() throws Throwable {
        Converter<Object, String> converter = mock(Converter.class);
        targetChannel = mock(Channel.class);
        transformedChannel = new TransformedChannel<>(targetChannel, converter);
    }

    @When("^the transformed channel receives a close event$")
    public void the_transformed_channel_receives_a_close_event() throws Throwable {
        transformedChannel.onClose();
    }

    @Then("^the target channel receives the close event$")
    public void the_target_channel_receives_the_close_event() throws Throwable {
        verify(targetChannel).onClose();
    }

    @When("^the transformed channel receives an error event$")
    public void the_transformed_channel_receives_an_error_event() throws Throwable {
        transformedChannel.onError(any());
    }

    @Then("^the target channel receives the error event$")
    public void the_target_channel_receives_the_error_event() throws Throwable {
        verify(targetChannel).onError(any());
    }

    private static class ToStringConverter extends SynchronousConverter<Object, String> {
        @Override
        public String convert(Object o) throws ConversionFailureException {
            return o.toString();
        }
    }
}
