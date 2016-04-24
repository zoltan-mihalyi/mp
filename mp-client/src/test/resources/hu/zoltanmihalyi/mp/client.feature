Feature: Client
  Scenario: A client receives a room join event
    Given a client with a connection
    When a room join event is fired
    Then the annotated join method is called
    And the annotated join method with RemoteRoom parameter is called

  Scenario: A client receives a room leave event
    Given a client with a connection
    Given a room join event is fired
    When a room leave event is fired
    Then the annotated leave method is called

  Scenario: A client receives a replication event
    Given a client with a connection
    Given a room join event is fired
    Given a replicator is set for the room
    When a replication event is fired
    Then the replicator should be notified

  Scenario: A client receives a replication event when no replicator is set
    Given a client with a connection
    Given a room join event is fired
    When a replication event is fired
    Then an exception should be thrown

  Scenario: a privilege method is called
    Given a client with a connection
    Given a room join event is fired
    When a privilege method is called on the RemoteRoom
    Then the client should send an invocation event
    And the event should contain the correct class, method and parameters

  Scenario: A client receives a null reference as target channel
    Given a client
    Then adding a null reference to the client as target channel causes an exception

  Scenario: Adding target channel two times causes an exception
    Given a client with a connection
    Then adding a target channel again causes an exception