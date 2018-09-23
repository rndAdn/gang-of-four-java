package com.radequin.gangoffour.service;

import com.radequin.gangoffour.CucumberStepsDefinition;
import com.radequin.gangoffour.domain.Card;
import com.radequin.gangoffour.domain.CardColor;
import com.radequin.gangoffour.domain.CardValue;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CucumberStepsDefinition
@ContextConfiguration("classpath:cucumber.xml")
@TestPropertySource(locations = "classpath:test.properties")
public class TestCardService {

    @Autowired
    CardService cardService;
    private Card my_Card;
    private List<Card> my_CardList;
    private boolean resultBoolean;
    private BigInteger cardValueBigItnteger;


    @When("^I check if my card is valid$")
    public void i_check_if_my_card_is_valid() {
        resultBoolean = cardService.isValidCard(my_Card);
    }

    @Then("^I should get (\\w+)")
    public void i_should_get_true(String expected) {
        Assert.assertEquals(Boolean.parseBoolean(expected), resultBoolean);
    }

    @Given("^My card is (\\d{1,2}[GYRM])$")
    public void myCardIs(String cardString) {
        my_Card = stringToCard(cardString);
    }

    @Given("^A list of card$")
    public void aListOfCard(List<String> cardStringList) {
        my_CardList = new ArrayList<>();
        for (String cardString : cardStringList) {
            my_CardList.add(stringToCard(cardString));
        }
    }


    @When("^I transfom my list of card to number$")
    public void iTransfomMyListOfCardToNumber() {
        cardValueBigItnteger = cardService.cardsToBigInteger(my_CardList);
    }

    @Then("^It should be equal to (\\w+)$")
    public void iShouldGert(String cardValue) {
        Assert.assertEquals(0, cardValueBigItnteger.compareTo(new BigInteger(cardValue)));
    }


    Card stringToCard(String cardString) {
        int cardValue = Integer.parseInt(cardString.substring(0, cardString.length() - 1));
        char cardColor = cardString.charAt(cardString.length() - 1);
        return new Card(CardValue.findByValue(cardValue), CardColor.findByValue(cardColor));
    }
}