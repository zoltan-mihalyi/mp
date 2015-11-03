Feature: Room
  Scenario: An user is added to a room
    Given a room
    Given a user
    When the user is added to the room
    Then the room notifies the listener about the new membership
    And the membership belongs to the user
    And the room should contain the user
    And the number of users in the room should be 1

  Scenario: A user is not added to a room
    Given a room
    Given a user
    Then the room should not contain the user
    And the number of users in the room should be 0

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

  Scenario: Getting users from the room
    Given a room
    And a user
    When the user is added to the room
    And another user is added to the room
    Then getting the users should result the user and another user

  Scenario: Two users are added to a room
    Given a room
    And a user
    When the user is added to the room
    And another user is added to the room
    Then the number of users in the room should be 2