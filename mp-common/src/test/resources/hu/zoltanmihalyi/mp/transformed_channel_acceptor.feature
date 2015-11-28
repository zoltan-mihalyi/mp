Feature: transformed channel acceptor
  Scenario: A transformed channel acceptor receives a connection
    Given a channel acceptor
    Given a transformed channel acceptor for the channel acceptor
    When the transformed channel acceptor receives a connection
    Then the channel acceptor should receive a connection

  Scenario: Sending a message to the received channel
    Given a channel acceptor
    Given a transformed channel acceptor for the channel acceptor
    And the transformed channel acceptor receives a connection
    When a message is sent to the received channel
    Then the original channel should receive the transformed message

  Scenario: Sending a message to the result channel
    Given a channel acceptor
    Given a transformed channel acceptor for the channel acceptor
    And the transformed channel acceptor receives a connection
    When a message is sent to the result channel
    Then the original result channel should receive the transformed message