Feature: Privilege
  Scenario: Getting granted privilege
    Given a membership
    When a privilege with type T is granted
    Then the privilege belongs to the membership
    Then getting the privilege with type T results the privilege

  Scenario: Getting not granted privilege
    Given a membership
    Then getting the privilege with type T results an error

  Scenario: Adding a privilege two times
    Given a membership
    When a privilege with type T is granted
    Then granting a privilege with type T results an error

  Scenario: Checking for privilege which is not granted
    Given a membership
    Then checking for privilege T results "false"

  Scenario: Checking for privilege which is granted
    Given a membership
    When a privilege with type T is granted
    Then checking for privilege T results "true"

  Scenario: Revoking privilege which is not granted
    Given a membership
    Then revoking privilege with type T results an error

  Scenario: Revoking privilege which is granted
    Given a membership
    Given a privilege with type T is granted
    When revoking privilege with type T
    Then checking for privilege T results "false"

  Scenario: Adding the same privilege to two membership
    Given a membership
    Given an another membership
    When a privilege with type T is granted
    Then granting the same privilege to the another membership results an error