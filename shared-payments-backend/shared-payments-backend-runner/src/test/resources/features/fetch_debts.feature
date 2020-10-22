Feature: Fetch group debts

  Scenario: Fetch the debts between the friends of the group
    Given an existing friend group
    And an user with identifier 3 belonging to the friend group
    When the debts are fetched
    Then the debts result is the following
    | payer          | recipient   | amount  |
    | Julio Baptista | Juan Rios   | 25.3334 |
    | David Romero   | Juan Rios   | 15.3332 |

  Scenario: Fetch the debts between the friends of the group (Acceptance criteria)
    Given an existing friend group
    And an user with identifier 5 belonging to the friend group
    When the debts are fetched
    Then the debts result is the following
      | payer                 | recipient         | amount |
      | Raul Gonzalez         | Francisco Buyo    | 40.8500  |
      | Jose Maria Gutierrez  | Alfonso Perez     | 22.5500  |
      | Jose Maria Gutierrez  | Francisco Buyo    | 18.3000  |
