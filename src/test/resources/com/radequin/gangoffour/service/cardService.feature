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
