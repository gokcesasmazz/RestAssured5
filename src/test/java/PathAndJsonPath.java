
import GoRest.User;
import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class PathAndJsonPath {

    @Test
    public void extractingPath(){

        int postCode=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().path("'post code'");

        System.out.println("postCode = " + postCode);

        // post code int görnümlü birstring tir. bu nedenle hata yapmaya açık haldedir. bunun önüne geçmek için JsonPath kullanılır.

    }
    @Test
    public void extractingJsonPath(){

        int postCode=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'");

        //tip dönüşümü otomatik, uygun tip verilmeli

        System.out.println("postCode = " + postCode);

    }
    @Test
    public void getUsers(){
        Response response=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .extract().response();

        int idPath= response.path("[2].id");
        int idJsonPath= response.jsonPath().getInt("[2].id");

        System.out.println("idPath = " + idPath);
        System.out.println("idJsonPath = " + idJsonPath);

        User[] usersPath=response.as(User[].class); // as nesne dönüşümünde (POJO) dizi destekli
        List<User> usersList= response.jsonPath().getList("", User.class); // JsonPath ise List olarak verebiliyor

        System.out.println("usersPath = " + Arrays.toString(usersPath));
        System.out.println("usersList = " + usersList);
    }
    @Test
    public void getUserV1(){

        Response body=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users") //v1 de meta ve data lar ayrı şekilde yapılmış
                                                                // bu şekilde dizayn edildiğinde as yeterli olmaz
                                                                // nokta atışı yapabilmek için jsonPath kullanılır

                .then()
                .log().body()
                .extract().response()
                ;

        List<User> dataUsers= body.jsonPath().getList("data", User.class);
        //JsonPath ile bir resonse içindeki bir parçayı nesneye dönüştürebiliriz
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde (as) Class dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi classa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

        //***********************************************************************************************//

        // Daha önceki örneklerde as ile Class dönüşümlerini yapıyorduk ve
        // tüm yapıya karşılık gelen classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath ile) aradaki bir veriyi clasa dönüştürerek bir list olarak alıyoruz
        // Böylece tek class ile veri alınmış oldu ve diğer classlara gerek kalmadı

        // path : class veya tip dönüşümüne imkan vermez, direkt veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek veriyi istediğimiz formatta verir.
    }

    @Test
    public void getZipCoed(){

        Response response=
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")


                .then()
                //.log().body()
                .extract().response()
                ;

        Location locPathAs= response.as(Location.class); // bütün classları yazdırır
        System.out.println("locPathAs.getPlaces() = " + locPathAs.getPlaces());

        // body deki places kısmına location ve place classlarına ihtiyaç duyarak ulaşabiliyoruz.
        // Jsonpath sayesinde sadece ulaşmak istediğimiznesne için class oluşturmamız yeterli oluyor

        List<Place> places= response.jsonPath().getList("places", Place.class); //nokta atışı istediğimiz nesneyi aldık
        System.out.println("places = " + places);
    }

}
