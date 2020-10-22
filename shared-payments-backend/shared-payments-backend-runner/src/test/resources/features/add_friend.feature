Feature: Add friend to my group

  Scenario: Add a new friend to my group
    Given an existing friend group
    And an user with identifier 3 belonging to the friend group
    When the user adds 'Sergio Ramos' to the group
    Then the new friend 'Sergio Ramos' belongs to the same group than the user with identifier 3
