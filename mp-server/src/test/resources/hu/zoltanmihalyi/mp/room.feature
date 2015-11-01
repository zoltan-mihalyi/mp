Feature: Room
  Scenario: An user is added to a room
    Given a room
    Given a user
    When the user is added to the room
    Then the room notifies the listener about the new membership
    And the membership belongs to the user
    And the room should contain the user

  Scenario: A user is not added to a room
    Given a room
    Given a user
    Then the room should not contain the user

  Scenario: A user is removed from the room
    Given a room
    Given a user
    And the user is added to the room
    When the user is removed from the room
    Then the room should not contain the user

  Scenario: Revoking membership
    Given a room
    Given a user
    And the user is added to the room
    When the membership is revoked
    Then the room should not contain the user