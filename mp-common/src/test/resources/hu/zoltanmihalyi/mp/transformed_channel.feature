Feature: Channel transformer
  To allow to create a wrapper channel which converts the messages and passes to the target channel.

  Scenario: Sending message to the channel
    Given a transformed channel with a converter which converts integers to string
    When a message is sent to the transformed channel with value 1
    Then the target channel receives the message "1"

  Scenario: Forwarding close events
    Given a transformed channel
    When the transformed channel receives a close event
    Then the target channel receives the close event

  Scenario: Forwarding error events
    Given a transformed channel
    When the transformed channel receives an error event
    Then the target channel receives the error event