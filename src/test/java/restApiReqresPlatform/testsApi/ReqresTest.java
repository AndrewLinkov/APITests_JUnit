package restApiReqresPlatform.testsApi;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restApiReqresPlatform.data.Register;
import restApiReqresPlatform.data.SuccessReg;
import restApiReqresPlatform.data.UnSuccessReg;
import restApiReqresPlatform.data.UserData;
import restApiReqresPlatform.dataClasses.ColorsData;
import restApiReqresPlatform.specification.Spicifacations;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
public class ReqresTest {
    private static final String URL = "https://reqres.in/";

    @Test
    @DisplayName("Реализация ТК 1")
    public void checkAvatarAndIdTest() {
        Spicifacations.installSpecification(Spicifacations.requestSpec(URL), Spicifacations.responseSpecOK200());
        List<UserData> users = given()
                //ключевое слово когда
                .when()
                //Указываем эндпоинт куда надо отправить запрос
                .get("api/users?page=2")
                //ключевое слово где
                //вытащить логи (все)
                .then().log().all()
                //ответ нужно извлечь в Pojo класс
                //извлечь тело, в формате Json, списком. В скобках выбираем корень массива data и указать класс куда извлекаем
                .extract().body().jsonPath().getList("data", UserData.class);

        //Убедиться что имена файлов-аватаров пользоваталей совпадают;
        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        //Убедиться, что email пользователей имеет окончание reqres.in;
        List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        //Перебеираем списки
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    @DisplayName("Реализация ТК 2.1. Успешная регистрация")
    public void successRegTest() {
        Spicifacations.installSpecification(Spicifacations.requestSpec(URL), Spicifacations.responseSpecOK200());
        //Объявляем то что нам нужно получить. Данные, с которыми мы будем сравнивать ФР с ОР
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        //Создаем экземпляр класса с параметрами, которые уйдут на сервер
        Register user = new Register("eve.holt@reqres.in", "pistol");
        //Извлекаем ответ в виде класса
        SuccessReg successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessReg.class);
        //Проверка, что передается не пустой результат (не Null)
        Assert.assertNotNull(successReg.getId());
        Assert.assertNotNull(successReg.getToken());

        //Проверка по ID
        Assert.assertEquals(id, successReg.getId());
        //Проверка по токену
        Assert.assertEquals(token, successReg.getToken());
    }

    @Test
    @DisplayName("Реализация ТК 2.2. Неуспешная регистрация (без пароля)")
    public void unSuccesRegTest() {
        Spicifacations.installSpecification(Spicifacations.requestSpec(URL), Spicifacations.responseSpecError400());
        Register user = new Register("sydney@fife", "");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .post("/api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);
        //Проверка записи поля Error
        Assert.assertEquals("Missing password", unSuccessReg.getError());
    }

    @Test
    @DisplayName("Реализация ТК 3")
    public void sortedYearsTest() {
        Spicifacations.installSpecification(Spicifacations.requestSpec(URL), Spicifacations.responseSpecOK200());
        //Нужно получить список
        List<ColorsData> colors = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        //Создаем список с годами
        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        //Сортируем список по возрастаниюм
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(sortedYears, years);

        //Проверка параметров выводом на консоль
        System.out.println(years);
        System.out.println(sortedYears);
    }

    @Test
    @DisplayName("Реализация ТК 4")
    public void deleteUserTest() {
        Spicifacations.installSpecification(Spicifacations.requestSpec(URL), Spicifacations.responseSpecUnique(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }
}
