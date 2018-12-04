package be.ucll.ewiuo.controllers;

import be.ucll.ewiuo.Application;
import be.ucll.ewiuo.model.Sandwich;
import be.ucll.ewiuo.repository.SandwichRepository;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static be.ucll.ewiuo.model.SandwichTestBuilder.aSandwich;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SandwichControllerIntegrationTest extends be.ucll.ewiuo.controllers.AbstractControllerIntegrationTest {

    @Autowired
    private SandwichRepository sandwichRepository;

    @Before
    public void setUpASavedSandwich() {
        sandwichRepository.deleteAll();
    }

    @Test
    public void testGetSandwiches_NoSavedSandwiches_EmptyList() throws JSONException {
        String actualSandwiches = httpGet("/sandwiches");
        String expectedSandwiches = "[]";

        assertThatJson(actualSandwiches).isEqualTo(expectedSandwiches);
    }

    @Test
    public void testPostSandwich() throws JSONException {
        Sandwich sandwich = aSandwich().withName("Americain").withIngredients("Vlees").withPrice(4.0).build();

        String actualSandwichAsJson = httpPost("/sandwiches", sandwich);
        String expectedSandwichAsJson = "{\"id\":\"${json-unit.ignore}\",\"name\":\"Americain\",\"ingredients\":\"Vlees\",\"price\":4}";

        assertThatJson(actualSandwichAsJson).isEqualTo(expectedSandwichAsJson);
    }

    @Test
    public void testPutSandwich() throws JSONException {
        Sandwich sandwich = aSandwich().withName("Visburger").withIngredients("Vis").withPrice(3.5).build();

        String actualSandwichAsJson = httpPut("/sandwiches", sandwich);
        String expectedSandwichAsJson = "{\"id\":\"${json-unit.ignore}\",\"name\":\"Visburger\",\"ingredients\":\"Vis\",\"price\":3.5}";

        assertThatJson(actualSandwichAsJson).isEqualTo(expectedSandwichAsJson);

        String actualSanwichesSecondGo = httpPut("/sandwiches", sandwich);
        assertThatJson(actualSandwichAsJson).isEqualTo(actualSanwichesSecondGo);
    }

    @Test
    public void testGetSandwiches_WithSavedSandwiches_ListWithSavedSandwich() throws JSONException {

        Sandwich s1 = new Sandwich("Smoske", "Kaas en Hesp", new BigDecimal(3.50));
        Sandwich s2 = new Sandwich("Gezond", "Groentjes", new BigDecimal(4.00));
        Sandwich s3 = new Sandwich("Americain", "Americain en Augurkjes", new BigDecimal(2.20));
        httpPost("/sandwiches", s1);
        httpPost("/sandwiches", s2);
        httpPost("/sandwiches", s3);
        String sandwiches = httpGet("/sandwiches");
        String expectedSandwiches = "[{\"id\":\"${json-unit.ignore}\",\"name\":\"Smoske\",\"ingredients\":\"Kaas en Hesp\",\"price\":3.50},{\"id\":\"${json-unit.ignore}\",\"name\":\"Gezond\",\"ingredients\":\"Groentjes\",\"price\":4.00},{\"id\":\"${json-unit.ignore}\",\"name\":\"Americain\",\"ingredients\":\"Americain en Augurkjes\",\"price\":2.20}]";

        assertThatJson(sandwiches).isEqualTo(expectedSandwiches);
    }
}
