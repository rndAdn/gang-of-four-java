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
import java.util.Map;

@Slf4j
@CucumberStepsDefinition
@ContextConfiguration("classpath:cucumber.xml")
@TestPropertySource(locations = "classpath:test.properties")
public class TestCardService {

    @Autowired
    CardService cardService;
    private Card my_Card;
    private List<Card> my_CardList1;
    private List<Card> my_CardList2;
    private boolean resultBoolean;
    private BigInteger cardValueBigItnteger;
    private List<Card> deck;


    @When("^I check if my card is valid$")
    public void i_check_if_my_card_is_valid() {
        resultBoolean = cardService.isValidCard(my_Card);
    }

    @Then("^I should get boolean (\\w+)")
    public void i_should_get_true(String expected) {
        Assert.assertEquals(Boolean.parseBoolean(expected), resultBoolean);
    }

    @Given("^My card is (\\d{1,2}[GYRM])$")
    public void myCardIs(String cardString) {
        my_Card = stringToCard(cardString);
    }

    @Given("^A list of card$")
    public void aListOfCard(List<String> cardStringList) {
        my_CardList1 = new ArrayList<>();
        for (String cardString : cardStringList) {
            my_CardList1.add(stringToCard(cardString));
        }
    }


    @When("^I transfom my list of card to number$")
    public void iTransfomMyListOfCardToNumber() {
        cardValueBigItnteger = cardService.cardsToBigInteger(my_CardList1);
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

    @Given("^Two hand$")
    public void twoHand(Map<String, String> handsMap) throws Throwable {
        String[] hand1String = handsMap.get("hand1").split(",");
        String[] hand2String = handsMap.get("hand2").split(",");
        my_CardList1 = new ArrayList<>();
        my_CardList2 = new ArrayList<>();
        for (String s : hand1String) {
            my_CardList1.add(stringToCard(s));
        }
        for (String s : hand2String) {
            my_CardList2.add(stringToCard(s));
        }
    }

    @When("^I compare hand1 and hand2$")
    public void iCompareHandAndHand() throws Throwable {
        resultBoolean = cardService.compareHand(my_CardList2, my_CardList1);
    }

    @Given("^A string of number (\\w+) that represent a list of card$")
    public void aStringOfNumberThatRepresentAListOfCard(String number) throws Throwable {
        cardValueBigItnteger = new BigInteger(number);
    }

    @When("^I transfom my number to list of card$")
    public void iTransfomMyNumberToListOfCard() throws Throwable {
        my_CardList1 = cardService.bigIntegerToCards(cardValueBigItnteger);
    }

    @Then("^It should be equal to the list$")
    public void itShouldBeEqualToTheList(List<String> cardStringList) throws Throwable {
        my_CardList2 = new ArrayList<>();
        for (String cardString : cardStringList) {
            my_CardList2.add(stringToCard(cardString));
        }
        my_CardList1.sort(Card::compareTo);
        my_CardList2.sort(Card::compareTo);
        Assert.assertEquals(my_CardList2.size(), my_CardList1.size());
        for (int i = 0; i < my_CardList1.size(); i++) {
            Assert.assertEquals(my_CardList2.get(i), my_CardList1.get(i));
        }
    }

    @Given("^Nothing$")
    public void nothing() throws Throwable {
    }

    @When("^create a deck$")
    public void createADeck() throws Throwable {
        deck = cardService.createDeck();
    }

    @Then("^I should get a number of (\\d+) cards$")
    public void iShouldGetCard(int arg0) throws Throwable {
        Assert.assertEquals(arg0, deck.size());
    }
}