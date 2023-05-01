package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Countrytest {

    RequestSpecification reqSpec;
    String countryID;
    Faker faker=new Faker();


    Map<String, String> country=new HashMap<>();
    String countryName;


    @BeforeClass
    public void Login(){

        baseURI="https://test.mersys.io";


        Map<String, String>userCredential=new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");


        Cookies cookies=
        given()

                .contentType(ContentType.JSON)
                .body(userCredential)

                .when()
                .post("/auth/login")

                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies()
                ;

        reqSpec=new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build()
                ;

    }

    @Test
    public void createCountry(){


        countryName=faker.address().country()+faker.number().digits(5);
        country.put("name", countryName);
        country.put("code", faker.address().countryCode()+faker.number().digits(5));

        countryID=
        given()
                .spec(reqSpec)
                .body(country)
                .log().body()

                .when()
                .post("/school-service/api/countries")

                .then()
                //.log().body()
                .statusCode(201)
                .extract().path("id")
                ;

        System.out.println("countryID = " + countryID);


    }
    @Test (dependsOnMethods = "createCountry")
    public void createCountryNeg(){

        country.put("name", countryName);
        country.put("code", faker.address().countryCode());


                given()
                        .spec(reqSpec)
                        .body(country)
                        .log().body()

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)

        ;

        System.out.println("countryID = " + countryID);
    }
    @Test (dependsOnMethods = "createCountry")
    public void updateCountry(){

        country.put("id", countryID);
        countryName="Gökçe"+faker.number().digits(5);
        country.put("name",countryName);
        country.put("code", faker.address().countryCode());


                given()
                        .spec(reqSpec)
                        .body(country)
                        .log().body()

                        .when()
                        .put("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("name", equalTo(countryName))
        ;
    }
    @Test (dependsOnMethods = "updateCountry")
    public void deleteCountry(){

        given()
                .spec(reqSpec)
                .pathParam("countryID", countryID)
                .log().uri()

                .when()
                .delete("/school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(200)
        ;

    }
    @Test (dependsOnMethods = "deleteCountry")
    public void deleteCountryNeg(){

        given()
                .spec(reqSpec)
                .pathParam("countryID", countryID)
                .log().uri()

                .when()
                .delete("/school-service/api/countries/{countryID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found")) //body içindeki message kısmından alındı
        ;


    }

}
