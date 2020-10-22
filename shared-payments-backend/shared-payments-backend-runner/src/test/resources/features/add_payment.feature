Feature: Add payment

  Scenario: Add a new friend to my group
    Given an existing friend group
    And an user with identifier 3 belonging to the friend group
    When the user adds the payment with amount 24.45 and the description 'Supermarket'
    Then a new payment is added to the group