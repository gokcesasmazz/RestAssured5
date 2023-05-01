package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {

    Faker faker=new Faker();
    int userId;

    RequestSpecification reqSpec;
    @BeforeClass
    public void setup(){

        baseURI = "https://gorest.co.in/public/v2/users";

        //baseuri reqspec ten önce olmalı çünkü reqspec içinde baseuri set ediliyor

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer e4b22047188da067d3bd95431d94259f63896347f9864894a0a7013ee5f9c703")
                .setContentType(ContentType.JSON)
                .setBaseUri(baseURI)
                .build();

    }

    @Test (enabled = false)
    public void createUserJson(){
//ihtiyaç listesi
        // POST   https://gorest.co.in/public/v2/users
        // Authorization: Bearer  e52529ff94dba0e629f524bcf0e9ee25e19829534ae977e6fe8443ddb38d97b1
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}

        String rndFullname= faker.name().fullName() ;
        String rndEmail=faker.internet().emailAddress();


        userId=
                given()

                        .spec(reqSpec)
                        .body("{\"name\":\""+rndFullname+"\", \"gender\":\"male\", \"email\":\""+rndEmail+"\", \"status\":\"active\"}")
                        .log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;

    }
    @Test
    public void createUserMap(){
        String rndFullname= faker.name().fullName() ;
        String rndEmail=faker.internet().emailAddress();

        Map<String, String> newUser=new HashMap<>();
        newUser.put("name", rndFullname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userId=
                given()

                        .spec(reqSpec)
                        .body(newUser)
//                        .log().uri()
//                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }
    @Test (enabled = false)
    public void createUserClass() {
        String rndFullname = faker.name().fullName();
        String rndEmail = faker.internet().emailAddress();

        User newUser=new User();
        newUser.name=rndFullname;
        newUser.setGender("male");
        newUser.email=rndEmail;
        newUser.setStatus("active");

        userId =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        //.log().uri()
                        .log().body()

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
    }


    @Test (dependsOnMethods = "createUserMap") //burda create edilen user id si geleceği için bu test createuser testine bağlıdır
    public void getUserById(){
        //ihtiyaç listesi
        // POST   https://gorest.co.in/public/v2/users
        // Authorization:

        given()

                .spec(reqSpec)

                .when()
                .get(""+userId)


                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .body("id",equalTo(userId))
                ;
    }

    @Test (dependsOnMethods = "getUserById")
    public void updateUser(){

        Map<String,String> updateUser=new HashMap<>();
        updateUser.put("name","gökçe şaşmaz");
        given()

                .spec(reqSpec)
                .body(updateUser)

                .when()
                .put(""+userId)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userId))
                ;
    }

    @Test (dependsOnMethods = "updateUser")
    public void deleteUser(){

        given()
                .spec(reqSpec)

                .when()
                .delete(""+userId)

                .then()
                .log().body()
                .statusCode(204)
                ;

    }
    @Test (dependsOnMethods = "deleteUser")
    public void deleteUserNeg(){

        given()
                .spec(reqSpec)

                .when()
                .delete(""+userId)

                .then()
                .log().body()
                .statusCode(404)
        ;



    }
}
