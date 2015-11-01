Feature: Server
  Scenario: A new user is connecting
    Given a server
    Given a channel
    When a new channel is added to the server
    Then the server notifies the listener about the user
