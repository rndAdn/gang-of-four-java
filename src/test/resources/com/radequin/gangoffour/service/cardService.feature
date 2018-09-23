Feature: Check Card Service

  Scenario: Is 1G a valid card
    Given My card is 1G
    When I check if my card is valid
    Then I should get true

  Scenario: Is 1M a valid card
    Given My card is 1M
    When I check if my card is valid
    Then I should get true

  Scenario: Is 2M a valid card
    Given My card is 2M
    When I check if my card is valid
    Then I should get false

  Scenario: Is Pheonix green a valid card
    Given My card is 11G
    When I check if my card is valid
    Then I should get true

  Scenario: Is Pheonix red a valid card
    Given My card is 11R
    When I check if my card is valid
    Then I should get false

  Scenario: Is Dragon red a valid card
    Given My card is 12R
    When I check if my card is valid
    Then I should get true

  Scenario: Is Dragon yellow a valid card
    Given My card is 12Y
    When I check if my card is valid
    Then I should get false

  Scenario: I want to transform my cards [2R] to a number
    Given A list of card
      | 2R |
    When I transfom my list of card to number
    Then It should be equal to 22


  Scenario: I want to transform my cards [12R] to a number
    Given A list of card
      | 12R |
    When I transfom my list of card to number
    Then It should be equal to 122


  Scenario: I want to transform my cards [1G, 1G, 1Y, 1Y, 1R, 1R, 1M] to a number
    Given A list of card
      | 1G |
      | 1G |
      | 1Y |
      | 1Y |
      | 1R |
      | 1R |
      | 1M |
    When I transfom my list of card to number
    Then It should be equal to 45357341850961

  Scenario: I want to transform my cards [11G, 5Y, 10R, 1G] to a number
    Given A list of card
      | 11G |
      | 5Y  |
      | 10R |
      | 1G  |
    When I transfom my list of card to number
    Then It should be equal to 206244811