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
    Then passing an invocation event to the user's channel results a MembershipNotFound exception

  Scenario: A user is sending an invocation event to a not granted privilege
    Given a channel for server events
    Given a user for the channel
    And the user is added to a room
    Then passing an invocation event to the user's channel results a PrivilegeNotFound exception

  Scenario: A user is sending invocation event to a not active privilege
    Given a channel for server events
    Given a user for the channel
    And the user is added to a room
    And a privilege is added to the membership
    And the privilege is marked not active
    Then passing an invocation event to the user's channel results a PrivilegeNotFound exception