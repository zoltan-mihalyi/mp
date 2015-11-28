Feature: Server
  Scenario: A new user is connecting
    Given a server
    Given a channel
    When the channel is added to the server
    Then the server notifies the listener about the user
    And returns the user