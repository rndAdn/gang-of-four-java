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


  Scenario: I want to transform my string of cards [1G] to a list of cards
    Given A string of number 10 that represent a list of card
    When I transfom my number to list of card
    Then It should be equal to the list
      | 1G |

  Scenario: I want to transform my string of cards [1G, 1G, 1Y, 1Y, 1R, 1R, 1M] to a list of cards
    Given A string of number 45357341850961 that represent a list of card
    When I transfom my number to list of card
    Then It should be equal to the list
      | 1G |
      | 1G |
      | 1Y |
      | 1Y |
      | 1R |
      | 1R |
      | 1M |

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

  Scenario: I want to know if my hand1 [1Y, 1Y] beat this hand2 [1G, 1G]
    Given Two hand
      | hand1 | 1Y,1Y |
      | hand2 | 1G,1G |
    When I compare hand1 and hand2
    Then I should get true

  Scenario: I want to know if my hand1 [1Y, 1Y] beat this hand2 [1R, 1R]
    Given Two hand
      | hand1 | 1Y,1Y |
      | hand2 | 1R,1R |
    When I compare hand1 and hand2
    Then I should get false

  Scenario: I want to know if my hand1 [1Y,1Y,1R,1R] beat this hand2 [4Y,4Y,5Y,5Y,5R]
    Given Two hand
      | hand1 | 1Y,1Y,1R,1R    |
      | hand2 | 4Y,4Y,5Y,5Y,5R |
    When I compare hand1 and hand2
    Then I should get true


  Scenario: I want to know if my hand1 [1Y,1Y,1R,1R,1M] beat this hand2 [4Y,4Y,5Y,5Y,5R]
    Given Two hand
      | hand1 | 1Y,1Y,1R,1R,1M |
      | hand2 | 4Y,4Y,5Y,5Y,5R |
    When I compare hand1 and hand2
    Then I should get true

  Scenario: I want to know if my hand1 [1Y,1Y,1R,1R,1M] beat this hand2 [4Y,4Y,5Y,5Y,5R]
    Given Two hand
      | hand1 | 1G,1Y,1Y,1R,1R,1M |
      | hand2 | 4Y,4Y,5Y,5Y,5R    |
    When I compare hand1 and hand2
    Then I should get true

  Scenario: I want to know if my hand1 [1Y,1Y,1R,1R,1M] beat this hand2 [4Y,4Y,5Y,5Y,5R]
    Given Two hand
      | hand1 | 1G,1G,1Y,1Y,1R,1R,1M |
      | hand2 | 4Y,4Y,5Y,5Y,5R       |
    When I compare hand1 and hand2
    Then I should get true

  Scenario: I want to create a deck of cards
    Given Nothing
    When create a deck
    Then I should get a number of 64 cards