Feature: Replication
  Scenario: setting a replicator for a membership
    Given a user added to a room
    Given a replicator is set for the membership
    When an update is called on the membership
    Then the user should receive the replication event

  Scenario: brute force replication first data
    Given a user added to a room
    Given a replicator is set for the membership
    When an update is called on the membership
    Then the replication event should contain the state

  Scenario: brute force replication: no change in state results no replication event
    Given a user added to a room
    Given a replicator is set for the membership
    When an update is called on the membership
    And another update is called on the membership
    Then no replication event should be received

  Scenario: brute force replication: change in state results replication event
    Given a user added to a room
    Given a replicator is set for the membership
    When an update is called on the membership
    And the state is changed
    And another update is called on the membership
    Then the user should receive the replication event
    And the replication event should contain the state