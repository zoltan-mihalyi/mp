Feature: user

  Scenario: A user is sending an invocation event
    Given a channel for server events
    Given a user for the channel
    And the user is added to a room
    And a privilege is added to the membership
    When an invocation event is passed to the user's channel
    Then the privilege method should be called

  Scenario: A user is sending an invocation event to an invalid room
    Given a channel for server events
    Given a user for the channel
    Then passing an invocation event to the user's channel results an exception