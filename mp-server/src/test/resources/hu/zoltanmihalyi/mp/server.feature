Feature: Server
  Scenario: A new user is connecting
    Given a server
    Given a channel
    When the channel is added to the server
    Then the server notifies the listener about the user

  Scenario: The user is added to a room
    Given a server
    Given a channel
    Given a room for server
    And the channel is added to the server
    When the user of the channel is added to the room
    Then the channel should be notified about the join event

  Scenario: The user is removed form a room
    Given a server
    Given a channel
    Given a room for server
    And the channel is added to the server
    And the user of the channel is added to the room
    When the user of the channel is removed from the room
    Then the channel should be notified about the leave event