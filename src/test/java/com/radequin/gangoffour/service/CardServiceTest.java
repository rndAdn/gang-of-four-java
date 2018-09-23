package com.radequin.gangoffour.service;

import com.radequin.gangoffour.CucumberStepsDefinition;
import com.radequin.gangoffour.domain.Card;
import com.radequin.gangoffour.domain.CardColor;
import com.radequin.gangoffour.domain.CardValue;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@CucumberStepsDefinition
@ContextConfiguration("classpath:cucumber.xml")
public class CardServiceTest {

    @Autowired
    CardService cardService;
    private Card my_Card;
    private boolean resultBoolean;


    @When("^I check if my card is valid$")
    public void i_check_if_my_card_is_valid() {
        resultBoolean = cardService.isValidCard(my_Card);
    }

    @Then("^I should get (\\w+)")
    public void i_should_get_true(String expected) {
        Assert.assertEquals(resultBoolean, Boolean.parseBoolean(expected));
    }

    @Given("^My card is (\\d{1,2}[GYRM])$")
    public void myCardIs(String cardString) throws Throwable {
        int cardValue = Integer.parseInt(cardString.substring(0, cardString.length() - 1));
        char cardColor = cardString.charAt(cardString.length() - 1);
        my_Card = new Card(CardValue.findByValue(cardValue), CardColor.findByValue(cardColor));
    }
}