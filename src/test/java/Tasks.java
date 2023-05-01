import Model.ToDo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {

    /**
     * Task 2
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting into POJO
     */


    // url çalışmadı için task tamamlanmadı
    @Test
    public void test1() {

        ToDo toDo =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().body().as(ToDo.class);

        System.out.println("ToDo = " + toDo);
        System.out.println("toDo.getTitle() = " + toDo.getTitle());
    }

    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void test2() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;

        //  eğer Assert ile doğrulama yapacaksak oluşturduğumuz To-do classından nesne
        //  oluşturmamız gererkiyor. Nesne oluşturmamız şart değilse veya oluşturmadıysak
        // "equalTo" ile de pratik şekilde assertion yapılabilir.
        //üstteki soruda nesne oluşturmak şarttı
    }

    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     * extract completed field and testNG assertion
     */

    @Test
    public void test3() {

        //1. Yöntem
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("completed", equalTo(false))
                        ;
       // 2. Yöntem
        Boolean completed=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
                .extract().path("completed")
        ;
        Assert.assertFalse(completed);
    }
}
