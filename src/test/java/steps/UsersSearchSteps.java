package steps;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import java.util.ArrayList;
import java.util.Map;
import static io.restassured.path.json.JsonPath.from;

public class UsersSearchSteps {
    private String USER_SEARCH = "http://bpdts-test-app-v2.herokuapp.com";
    private static String jsonAsString;
    private Response response;

    @Step
    public Response searchAllUsers() {
        response = SerenityRest.when().get(USER_SEARCH + "/users");
        return response;
    }

    @Step
    public Response searchUserById(int id) {
        response = SerenityRest.when().get(USER_SEARCH + "/user/" + id);
        return response;
    }

    @Step
    public Response searchUserByCity(String city) {
        response = SerenityRest.when().get(USER_SEARCH + "/city/" + city + "/users");
        return response;
    }

    @Step
    public ArrayList<Map<String, ?>> getAllUsersInArrayList() {
        jsonAsString = response.then().extract().response().asString();
        ArrayList<Map<String, ?>> jsonAsArrayList = from(jsonAsString).get("");
        return jsonAsArrayList;
    }
        //end of tests

}
