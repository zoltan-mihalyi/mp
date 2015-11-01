Feature: Room
  Scenario: An user is added to a room
    Given a room
    Given a user
    When a user is added to the room
    Then the room notifies the listener about the new membership
    Then the room should contain the user

  Scenario: A user is not added to a room
    Given a room
    Given a user
    Then the room should not contain the user