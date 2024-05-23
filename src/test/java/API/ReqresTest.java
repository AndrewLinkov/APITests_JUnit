package API;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;


public class ReqresTest {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest() {
        List<UserData> users = given()
                //ключевое слово когда
                .when()
                //указываем формат чтения нашего запроса
                .contentType(ContentType.JSON)
                //Указываем ссылку на наш запрос + конкретный эндпоинт куда надо отправить запрос
                //Может быть указан любой метод PUT, DELETE и т.д.
                .get(URL + "api/users?page=2")
                //ключевое слово где
                //вытащить логи (все)
                .then().log().all()
                //ответ нужно извлечь в Pojo класс
                //извлечь тело, в формате Json, списком. В скобках выбираем корень массива data и указать класс куда извлекаем
                .extract().body().jsonPath().getList("data", UserData.class);

        //Вар. 1
        //Реализуем проверку ТК1.2
        //в метоже forEach происходит cравнение, что поле с аватаром содержит ID
        users.forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        //Реализуем проверку ТК1.3
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        //Вар. 2. Проверить списки на наличие по avatar и ID.
        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList());

        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }
}
