Feature: Replication
  Scenario: Updating all users with different replicators
    Given a room for replication
    And a membership in the room with a replicator
    And a membership in the room with a replicator
    When calling update on the room
    Then every user (2) was notified

  Scenario: Updating all users with the same replicator
    Given a room for replication
    Given a brute force replicator
    And a membership in the room with the brute force replicator
    And a membership in the room with the brute force replicator
    When calling update on the room
    Then every user (2) was notified
    And the replication data is the same

  Scenario: Updating every second user with the same replicator
    Given a room for replication
    Given a brute force replicator
    And an other brute force replicator
    And a membership in the room with the brute force replicator
    And a membership in the room with the brute force replicator
    And a membership in the room with the second brute force replicator
    When calling update on the room
    Then every user (3) was notified
    And the replication data is only same for the first two users

  Scenario: brute force replication first data
    Given a brute force replicator
    When getting replication data
    Then the data should contain the state

  Scenario: brute force replication: no change in state results no replication data
    Given a brute force replicator
    When getting replication data
    And getting replication data
    Then no data should be returned

  Scenario: brute force replication: change in state results replication data
    Given a brute force replicator
    When getting replication data
    And the state is changed
    And getting replication data
    Then the data should contain the state