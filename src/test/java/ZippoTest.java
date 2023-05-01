import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;



public class ZippoTest {

    @Test
    public void test() {

        given()
                // Hazırlık işlemleri: (token, send body, parametreler)

                .when()

                // end point (URL) metodu

                .then()

        // assertion, test, data işlemleri
        //burdan sonra gelenler için mi? mü? soru şeklinde cevap verir json mı statüs 200 mü gibi

        ;
    }

    @Test
    public void contentTypeTest() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()          // dönen body json datası, log.all
                .statusCode(200)    // dönüş kodu 200 mü?
                .contentType(ContentType.JSON)   // dönen sonuç JSON mı
        ;
    }

    @Test
    public void checkCountryInResponseBody() {

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()

                .log().body()          // dönen body json datası, log.all
                .statusCode(200)    // dönüş kodu 200 mü?
                .contentType(ContentType.JSON)   // dönen sonuç JSON mı
                .body("country", equalTo("United States"))
                // body nin country değişkeni united states e eşit mi

        // pm.response.json().id -> body.id      --> postman de bu şekilde bakıyorduk

        ;
    }

    // PM                            RestAssured
    //body.country                  body("country")
    //body.'post code'              body("post code")
    //body.places[0].'place name'   body("places[0].'place name'")
    //body.places.'place name'      body("places.'place name'")
    //bütün place nameleri bir arraylist olarak verir
    //
    //{
    //    "post code": "90210",
    //    "country": "United States",
    //    "country abbreviation": "US",
    //    "places": [
    //        {
    //            "place name": "Beverly Hills",
    //            "longitude": "-118.4065",
    //            "state": "California",
    //            "state abbreviation": "CA",
    //            "latitude": "34.0901"
    //        }
    //    ]
    //}

    @Test
    public void checkStateInResponseBody() {

        //https://jsonpathfinder.com/ sitesine gidip sol ekrana console daki body yapışırılır sağ ekrandan lazım olan ne ise . dan sonrası alınır
        // ve ordan kontrol edilir

        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()

                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))


        ;

    }
        @Test
        public void checkHasItem() {

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                //.log().body()      ----> hepsi gelmesn diye kapattık
                .statusCode(200)
                .body("places.'place name'", hasItem("Camuzcu Köyü"))
                //bütün place name lerin her hangi birinde camuzcu köyü var mı
               ;

        }
    @Test
    public void bodyArrayHasSizeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places.", hasSize(1))
        ;

    }
    @Test
    public void combiningTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1))  //sie 1 mi
                .body("places.state", hasItem("California"))   // verilen path deki list bu item a sahip mi
                .body("places[0].'place name'", equalTo("Beverly Hills"))   // verilen path deki değer buna eşit mi
        ;

    }
    @Test
    public void pathParam() {

        // http://api.zippopotam.us/us/90210    path PARAM
        //
        //https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9617  Query PARAM


        given()
                .pathParam("ulke", "us")
                .pathParam("postaKodu", 90210)
                .log().uri() //request link , giden url nasıl onu gösterir

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}")

                .then()
                .log().body()
                .statusCode(200)


        ;
    }

    @Test
    public void queryParamTest() {

        //https://gorest.co.in/public/v1/users?page=1 --> get() te verilen url ye .param içine yazılan parametre eklenir

        given()
                .param("page", 1)  //  ?page 1 şeklinde linke ekleniyor
                .log().uri() //request link

                .when()
                .get("https://gorest.co.in/public/v1/users") // reqest uri ?page=1 sonuna eklemiş şekilde gelir

                .then()
                .statusCode(200)
                .log().body()
                ;
    }
    @Test
    public void queryParamTest2() {

        //https://gorest.co.in/public/v1/users?page=3
        //bu linkteki 1 den 10 a kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        //çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1 ; i <10 ; i++) {
            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    //.log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
        // "meta": {
        //        "pagination": {
        //            "total": 2991,
        //            "pages": 300,

        // her bir "{" body de adres gönderirken "." olarak yazılır --> meta.paginaton.page gibi
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void Setup(){

        baseURI = "https://gorest.co.in/public/v1";  //burda http görünce devam classlarıdaki get ile birleştirip çalıştırıyor.

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void test1()
    {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page",1)  // ?page=1  şeklinde linke ekleniyor
                .spec(requestSpec)

                .when()
                .get("/users")  // ?page=1

                .then()
                .spec(responseSpec)
        ;
    }

    @Test
    public void extractingJsonPath(){


        String countryName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country")
                ;
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void extractingJsonPath2(){
        //placeName
        String placeName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0]['place name']")
                ;
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");
    }

    @Test
    public void  extractingJsonPath3(){
        //https://gorest.co.in/public/v1/users dönen değerdeki limit bilgisini yazdırınız

        int limit=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
    }
    @Test
    public void  extractingJsonPath4() {
        //https://gorest.co.in/public/v1/users dönen değerdeki tüm id leri yazdırınız


        List<Integer> idler=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("data.id")
        ;
        System.out.println("idler = " + idler);
    }
    @Test
    public void  extractingJsonPath5() {
        //https://gorest.co.in/public/v1/users dönen değerdeki tüm isim leri yazdırınız


        List<String> isimler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name")
                ;
        System.out.println("isimler = " + isimler);
    }

    @Test
    public void  extractingJsonResponseAll() {
        //https://gorest.co.in/public/v1/users dönen değerdeki tüm id leri yazdırınız


        Response donenData=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response(); //dönen tüm datayı verir

        List<Integer> idler = donenData.path("data.id");
        List<String> names = donenData.path("data.name");
        int limit= donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Gouranga Panicker"));
        Assert.assertTrue(idler.contains(1203767));
        Assert.assertEquals(limit, 10, "test sonucu hatalı");

    }
    @Test
    public void extractJsonAll_POJO(){
        // POJO: JSON NESNESİ : Location Nesnesi

        Location locationNesnesi=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .extract().body().as(Location.class) //location classına göre dönüştür

                ;
        System.out.println("locationNesnesi.getCountry() = " +
                locationNesnesi.getCountry());

        for (Place p: locationNesnesi.getPlaces())
            System.out.println("p = " + p);

        System.out.println("locationNesnesi.getPlaces().get(0).getPlacename() = " +
                locationNesnesi.getPlaces().get(0).getPlacename());
    }

    @Test
    public void extractPOJO_Soru() {
        // aşağıdaki endpointte Dörtağaç Köyüne ait diğer bilgileri yazdırınız

        Location locationNesnesi =
                given()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().body().as(Location.class);

        for (Place p : locationNesnesi.getPlaces())
            if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")) {
                System.out.println(p.getPlacename());
                System.out.println(p.getLongitude());
                System.out.println(p.getState());
                System.out.println(p.getStateabbreviation());
                System.out.println(p.getLatitude());
            }
    }
    }




















