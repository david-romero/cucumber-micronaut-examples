Feature: Show shared payments in my friend group

  Scenario: Show the existing payments in my friend group
    Given an existing friend group
    And an user with identifier 3 belonging to the friend group
    When the user show the shared payments
    Then the payments list contains valid values for the following fields
      | payer | amount | description | date |

  Scenario: Show the payments sorted by date
    Given an existing friend group
    And an user with identifier 3 belonging to the friend group
    When the user show the shared payments
    Then the payments list is sorted by date