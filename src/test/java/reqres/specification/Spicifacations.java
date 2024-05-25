package reqres.dataClasses.specification;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class Spicifacations {
    //Спецификация для запроса
    //Метод позволяет не прописать заново путь (базовый URL) и тип ответа (Json)
    public static RequestSpecification requestSpec(String url) {
        return new RequestSpecBuilder()
                //указываем по какой ссылке обращаемся
                .setBaseUri(url)
                //указываем формат Json
                .setContentType(ContentType.JSON)
                //метод сборки
                .build();
    }

    //Спецификация для ответа. Код 200
    public static ResponseSpecification responseSpecOK200() {
        return new ResponseSpecBuilder()
                //Ожидаем 200 статус ответа
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification responseSpecError400() {
        return new ResponseSpecBuilder()
                //Ожидаем 200 статус ответа
                .expectStatusCode(400)
                .build();
    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}
