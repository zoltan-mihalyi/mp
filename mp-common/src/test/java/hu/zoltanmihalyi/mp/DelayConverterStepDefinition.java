package hu.zoltanmihalyi.mp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.mockito.InOrder;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class DelayConverterStepDefinition {
    private DelayConverter<String> delayConverter;
    private Callback<String> callback;
    private Callback<ConversionFailureException> errback;

    @Given("^a delay converter with (\\d+) ms delay$")
    public void a_delay_converter_with_delay_ms(int delay) {
        delayConverter = new DelayConverter<>(delay);
        delayConverter.start();
    }

    @SuppressWarnings("unchecked")
    @When("^object (\\w+) is passed to the delay converter$")
    public void an_object_is_passed_to_the_delay_converter(String object) {
        callback = mock(Callback.class);
        errback = mock(Callback.class);
        delayConverter.convert(object, callback, errback);
    }

    @Then("^the callback should receive object (\\w+) after (\\d+) ms and before (\\d+) ms")
    public void the_callback_should_receive_object_within_ms(String object, int min, int max) {
        verify(callback, timeout(min).never()).call(object);
        verify(callback, timeout(max)).call(object);
    }

    @Then("^the errback should not receive any errors$")
    public void the_errback_should_not_receive_any_errors() {
        verify(errback, never()).call(any());
    }

    @When("^after (\\d+) ms object (\\w+) is passed to the delay converter")
    public void another_object_is_passed_to_the_delay_converter_after(int timeout, String object) {
        new Thread(() -> {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException ignored) {
            }
            delayConverter.convert(object, callback, errback);
        }).start();
    }

    @SuppressWarnings("unchecked")
    @When("^ten objects are passed to the delay converter$")
    public void ten_objects_are_passed_to_the_delay_converter() {
        callback = mock(Callback.class);
        errback = mock(Callback.class);

        for (int i = 0; i < 10; i++) {
            delayConverter.convert(String.valueOf(i), callback, errback);
        }
    }

    @Then("^the callback should receive the objects in the correct order within (\\d+) ms$")
    public void the_callback_should_receive_the_objects_in_the_correct_order_within_ms(int timeout) throws InterruptedException {
        Thread.sleep(timeout);
        InOrder inOrder = inOrder(callback);
        for (int i = 0; i < 10; i++) {
            inOrder.verify(callback).call(String.valueOf(i));
        }
    }

    @When("^the delay converter is stopped$")
    public void the_delay_converter_is_stopped() {
        delayConverter.stopImmediately();
    }

    @Then("^the callback should not receive anything within (\\d+) ms$")
    public void the_callback_should_not_receive_anything_within(int timeout) {
        verify(callback, timeout(timeout).never()).call(any());
    }

    @Then("^the delay converter thread stops in (\\d+) ms;$")
    public void the_delay_converter_thread_stops_in_ms(int timeout) throws InterruptedException {
        delayConverter.join(timeout);
        assertFalse(delayConverter.isAlive());
    }

    @Then("^passing an object to the delay converter causes an error$")
    public void passing_an_object_to_the_delay_converter_causes_an_error() {
        delayConverter.convert("test", callback, errback);
        verify(errback).call(any());
    }
}
