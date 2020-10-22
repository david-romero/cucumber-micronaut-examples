Feature: Fetch group balance

  Scenario: Fetch the balance of the group
    Given an existing friend group
    And an user with identifier 3 belonging to the friend group
    When the balance is fetched
    Then the balance result is the following
    | user | amount |
    | Julio Baptista | -25.3334 |
    | David Romero   | -15.3334 |
    | Juan Rios      | 40.6666  |