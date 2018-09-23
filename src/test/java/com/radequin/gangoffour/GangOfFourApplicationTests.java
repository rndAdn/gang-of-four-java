package com.radequin.gangoffour;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "json:target/cucumber-report/json/cucumber.json", "html:target/cucumber-report//html"},
        features = {"src/test/resources/com/radequin/gangoffour/service/cardService.feature"}
)
public class GangOfFourApplicationTests {

}
