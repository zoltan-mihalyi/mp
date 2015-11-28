Feature: delay converter

  Scenario: an object is passed to the delay converter
    Given a delay converter with 300 ms delay
    When object A is passed to the delay converter
    Then the callback should receive object A after 250 ms and before 350 ms
    And the errback should not receive any errors

  Scenario: two objects are passed with delay
    Given a delay converter with 300 ms delay
    When object A is passed to the delay converter
    And after 200 ms object B is passed to the delay converter
    Then the callback should receive object A after 250 ms and before 350 ms
    And the callback should receive object B after 450 ms and before 550 ms

  Scenario: multiple objects passed to the delay converter
    Given a delay converter with 100 ms delay
    When ten objects are passed to the delay converter
    Then the callback should receive the objects in the correct order within 150 ms

  Scenario: a delay converter is stopped
    Given a delay converter with 50 ms delay
    When object A is passed to the delay converter
    And the delay converter is stopped
    Then the callback should not receive anything within 100 ms
    And the delay converter thread stops in 20 ms;
    And passing an object to the delay converter causes an error