Feature: Privilege
  Scenario: Getting added privilege
    Given a membership
    When a privilege with type T is granted
    Then getting the privilege with type T results the privilege

  Scenario: Getting not added privilege
    Given a membership
    Then getting the privilege with type T results an error

  Scenario: Adding a privilege two times
    Given a membership
    When a privilege with type T is granted
    Then granting a privilege with type T results an error

  Scenario: Checking for privilege which is not added before
    Given a membership
    Then checking for privilege T results "false"

  Scenario: Checking for privilege which is added before
    Given a membership
    When a privilege with type T is granted
    Then checking for privilege T results "true"

  Scenario: Revoking privilege which is not added
    Given a membership
    Then revoking privilege with type T results an error

  Scenario: Revoking privilege which is added
    Given a membership
    Given a privilege with type T is granted
    When revoking privilege with type T
    Then checking for privilege T results "false"