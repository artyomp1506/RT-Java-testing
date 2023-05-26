import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestClass {
    private static String petId;
    @Test
    @Order(1)
    public void testGet() {

        Response response = given()

                .baseUri("https://petstore.swagger.io/v2/")

                .basePath("pet/findByStatus")

                .queryParam("status", "available")

                .header("Accept", "application/json")

                .header("Content-Type", "application/json;;charset=UTF-8")

                .header("Accept-Encoding", "gzip, deflate, br")

                .when()

                .get()

                .then()

                .extract().response();

        var json = response.jsonPath();
        System.out.println(json.getList("$").get(0));
        Assertions.assertEquals(200, response.statusCode(), "Статус код равен 200");
    }

    @Test
    @Order(2)
    public void testCreatePet()
    {
        var requestBody = getBody();
        var response = given()

                .baseUri("https://petstore.swagger.io/v2/")

                .basePath("pet")

                .header("Accept", "application/json")

                .header("Content-Type", "application/json;;charset=UTF-8")

                .body( requestBody.toString() )

                .when()

                .post()

                .then()

                .extract().response();
        petId = response.jsonPath().get("id").toString();
        System.out.println(petId);
        Assertions.assertEquals(200, response.statusCode(), "Статус код равен 200");


    }
    @Test
    @Order(3)
    public  void testGetAfterCreate()
    {
        var response = given()

                .baseUri("https://petstore.swagger.io/v2/")

                .basePath(String.format("pet/%s", petId))

                .header("Accept", "application/json")

                .header("Content-Type", "application/json;;charset=UTF-8")



                .when()

                .get()

                .then()

                .extract().response();
        System.out.println(response.getBody().prettyPrint());
        Assertions.assertEquals(200, response.statusCode(), "Статус код равен 200");
    }

    private static JSONObject getBody() {
        var category = new JSONObject();
        category.put("id", 0);
        category.put("name", "dog654");
        var photoUrls = new ArrayList<String>();
        photoUrls.add("ttt5");
        photoUrls.add("ttt6");
        var tag = new JSONObject();
        tag.put("id", 0);
        tag.put("name", "healthy");
        var tags = new ArrayList<JSONObject>();
        tags.add(tag);
        var body = new JSONObject();
        body.put("name", "Rex");
        body.put("status", "available");
        body.put("tags",tags);
        body.put("category", category);
        body.put("photoUrls", photoUrls);
        return body;
    }
}
