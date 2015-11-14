Feature: Client
  Scenario: A client receives a room join event
    Given a client
    When a room join event is fired
    Then the annotated join method is called
    And the annotated join method with RemoteRoom parameter is called

  Scenario: A client receives a room leave event
    Given a client
    Given a room join event is fired
    When a room leave event is fired
    Then the annotated leave method is called